package org.telatenko.storagesevicedomain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.dto.UpdateProductDto;
import org.telatenko.storagesevicedomain.exeption.ArticleExistsExeption;
import org.telatenko.storagesevicedomain.exeption.ProductNotFoundException;
import org.telatenko.storagesevicedomain.mapper.CreateProductMapper;
import org.telatenko.storagesevicedomain.mapper.FindAllProductsMapper;
import org.telatenko.storagesevicedomain.mapper.ReadProductMapper;
import org.telatenko.storagesevicedomain.modelType.ProductType;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;
import org.telatenko.storagesevicedomain.persistence.ProductRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CreateProductMapper createProductMapper;

    @MockBean
    private ReadProductMapper readProductMapper;

    @MockBean
    private FindAllProductsMapper findAllProductsMapper;

    private ProductDto productDto;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto(UUID.randomUUID(), "Car", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100), OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));
        productEntity = new ProductEntity();
        productEntity.setId(UUID.randomUUID());
        productEntity.setArticle("777");
        productEntity.setTitle("Car");
        productEntity.setDescription("Simple description");
        productEntity.setProductType(ProductType.TECH);
        productEntity.setPrice(new BigDecimal(100));
        productEntity.setQuantity(new BigDecimal(100));
        productEntity.setLastModified(OffsetDateTime.parse("2024-08-27T16:41:40.581Z"));
        productEntity.setCreatedAd(LocalDate.parse("2024-08-27"));
    }

    @Test
    @DisplayName("Тест метода findAllProducts на возвращение списка продуктов")
    void testFindAllProducts() {
        Page<ProductEntity> productPage = new PageImpl<>(List.of(productEntity));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(findAllProductsMapper.toDto(productEntity)).thenReturn(productDto);

        Page<ProductDto> result = productServiceImpl.findAllProducts(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals(productDto, result.getContent().getFirst());
    }

    @Test
    @DisplayName("Тест метода findProductById на возвращение продукта по ID")
    void testFindProductById() {
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(readProductMapper.DtoToEntity(productEntity)).thenReturn(productDto);

        ProductDto result = productServiceImpl.findProductById(productEntity.getId());

        assertEquals(productDto, result);
    }

    @Test
    @DisplayName("Тест метода findProductById на выброс ProductNotFoundException")
    void testFindProductByIdThrowsException() {
        UUID nonExistentId = UUID.randomUUID();
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productServiceImpl.findProductById(nonExistentId);
        });

        String expectedMessage = String.format("ProductEntity with 'id' = %s not found", nonExistentId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Тест метода saveProduct на сохранение продукта")
    void testSaveProduct() {
        when(createProductMapper.toEntity(productDto)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        UUID result = productServiceImpl.saveProduct(productDto);

        assertEquals(productEntity.getId(), result);
        verify(productRepository).save(productEntity);
    }

    @Test
    @DisplayName("Тест метода saveProduct на выброс ArticleExistsExeption при дублировании артикула")
    void testSaveProductArticleExists() {
        when(createProductMapper.toEntity(productDto)).thenReturn(productEntity);
        when(productRepository.findByArticle(productDto.getArticle())).thenReturn(Optional.of(productEntity));

        ArticleExistsExeption exception = assertThrows(ArticleExistsExeption.class, () -> {
            productServiceImpl.saveProduct(productDto);
        });

        String expectedMessage = String.format("ProductEntity with 'article' = %s already exists. His 'id' = %s", productDto.getArticle(), productEntity.getId().toString());
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Тест метода saveProduct на сохранение продукта без артикула")
    void testSaveProductWithoutArticle() {
        ProductDto productDtoWithoutArticle = new ProductDto(
                productDto.getId(),
                productDto.getTitle(),
                null,
                productDto.getDescription(),
                productDto.getProductType(),
                productDto.getPrice(),
                productDto.getQuantity(),
                productDto.getLastModified(),
                productDto.getCreatedAd()
        );

        when(createProductMapper.toEntity(productDtoWithoutArticle)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        UUID result = productServiceImpl.saveProduct(productDtoWithoutArticle);

        assertEquals(productEntity.getId(), result);
        verify(productRepository).save(productEntity);
    }

    @Test
    @DisplayName("Тест метода saveProduct на проверку установки времени создания и последнего изменения")
    void testSaveProductSetsTimestamps() {
        when(createProductMapper.toEntity(productDto)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        UUID result = productServiceImpl.saveProduct(productDto);

        assertEquals(productEntity.getId(), result);
        assertNotNull(productEntity.getCreatedAd());
        assertNotNull(productEntity.getLastModified());
        verify(productRepository).save(productEntity);
    }

    @Test
    @DisplayName("Тест метода deleteProductById на удаление продукта")
    void testDeleteProductById() {
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        productServiceImpl.deleteProductById(productEntity.getId());

        verify(productRepository).deleteById(productEntity.getId());
    }

    @Test
    @DisplayName("Тест метода updateProduct на обновление продукта")
    void testUpdateProduct() {
        UpdateProductDto updateProductDto = new UpdateProductDto("Car", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100), OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        UUID result = productServiceImpl.updateProduct(productEntity.getId(), updateProductDto);

        assertEquals(productEntity.getId(), result);
        verify(productRepository).save(productEntity);
    }

    @Test
    @DisplayName("Тест метода updateProduct на выброс ProductNotFoundException, если продукт не найден")
    void testUpdateProductNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        UpdateProductDto updateProductDto = new UpdateProductDto("Car", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100), OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));

        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productServiceImpl.updateProduct(nonExistentId, updateProductDto);
        });

        String expectedMessage = String.format("ProductEntity with 'id' = %s not found", nonExistentId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Тест метода updateProduct на выброс ArticleExistsExeption, если артикул уже существует у другого продукта")
    void testUpdateProductArticleExists() {
        UpdateProductDto updateProductDto = new UpdateProductDto("Car", "new-article", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100), OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));

        ProductEntity existingProductEntity = new ProductEntity();
        existingProductEntity.setId(UUID.randomUUID());
        existingProductEntity.setArticle("new-article");

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productRepository.findByArticle(updateProductDto.getArticle())).thenReturn(Optional.of(existingProductEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        ArticleExistsExeption exception = assertThrows(ArticleExistsExeption.class, () -> {
            productServiceImpl.updateProduct(productEntity.getId(), updateProductDto);
        });

        String expectedMessage = String.format("ProductEntity with 'article' = %s already exists. His 'id' = %s", updateProductDto.getArticle(), existingProductEntity.getId().toString());
        assertEquals(expectedMessage, exception.getMessage());
    }
}

