package org.telatenko.storagesevicedomain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telatenko.storagesevicedomain.exeption.ArticleExistsExeption;
import org.telatenko.storagesevicedomain.exeption.DeleteObjectExeption;
import org.telatenko.storagesevicedomain.mapper.CreateProductMapper;
import org.telatenko.storagesevicedomain.mapper.FindAllProductsMapper;
import org.telatenko.storagesevicedomain.mapper.ReadProductMapper;
import org.telatenko.storagesevicedomain.dto.UpdateProductDto;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.exeption.NotFoundProductException;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;
import org.telatenko.storagesevicedomain.persistence.ProductRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация сервиса для управления продуктами.
 * Этот сервис предоставляет методы для выполнения операций CRUD над продуктами.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CreateProductMapper createProductMapper;
    private final ReadProductMapper readProductMapper;
    private final FindAllProductsMapper findAllProductsMapper;

    /**
     * Получает список всех продуктов с пагинацией.
     *
     * @param pageable Объект пагинации.
     * @return Страница с продуктами в формате DTO.
     */
    public Page<ProductDto> findAllProducts(Pageable pageable) {
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        return productPage.map(findAllProductsMapper::toDto);
    }

    /**
     * Находит продукт по его идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Продукт в формате DTO.
     * @throws NotFoundProductException если продукт не найден.
     */
    @Transactional(readOnly = true)
    public ProductDto findProductById(final UUID id) {
        return readProductMapper.DtoToEntity(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException("id", id.toString())));
    }

    /**
     * Сохраняет новый продукт.
     *
     * @param productDto Продукт в формате DTO для сохранения.
     * @return Идентификатор сохраненного продукта.
     * @throws ArticleExistsExeption если артикул уже существует.
     */
    @Transactional
    public UUID saveProduct(final ProductDto productDto) {
        String article = productDto.getArticle();
        if (article != null) {
            Optional<ProductEntity> existingProduct = productRepository.findByArticle(article);
            if (existingProduct.isPresent()) {
                throw new ArticleExistsExeption("article", article, "id", existingProduct.get().getId().toString());
            }
        }

        ProductEntity productEntity = createProductMapper.toEntity(productDto);
        productEntity.setLastModified(OffsetDateTime.now());
        productEntity.setCreatedAd(LocalDate.now());
        return productRepository.save(productEntity).getId();
    }

    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param id Идентификатор продукта.
     * @throws DeleteObjectExeption если продукт не найден.
     */
    public void deleteProductById(final UUID id) {
        productRepository.findById(id).orElseThrow(() -> new DeleteObjectExeption());
        productRepository.deleteById(id);
    }

    /**
     * Обновляет информацию о продукте.
     *
     * @param id Идентификатор продукта.
     * @param updateProductDto Продукт в формате DTO для обновления.
     * @return Идентификатор обновленного продукта.
     * @throws NotFoundProductException если продукт не найден.
     * @throws ArticleExistsExeption если артикул уже существует.
     */
    @Transactional
    public UUID updateProduct(final UUID id, final UpdateProductDto updateProductDto) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException("id", id.toString()));

        String article = updateProductDto.getArticle();
        if (article != null && !article.equals(productEntity.getArticle())) {
            Optional<ProductEntity> existingProduct = productRepository.findByArticle(article);
            if (existingProduct.isPresent() && !existingProduct.get().getId().equals(id)) {
                throw new ArticleExistsExeption("article", article, "id", existingProduct.get().getId().toString());
            }
        }

        if (productEntity.getQuantity().compareTo(updateProductDto.getQuantity()) != 0) {
            productEntity.setLastModified(OffsetDateTime.now());
        }

        productEntity.setArticle(updateProductDto.getArticle());
        productEntity.setTitle(updateProductDto.getTitle());
        productEntity.setDescription(updateProductDto.getDescription());
        productEntity.setProductType(updateProductDto.getProductType());
        productEntity.setPrice(updateProductDto.getPrice());
        productEntity.setQuantity(updateProductDto.getQuantity());
        return productRepository.save(productEntity).getId();
    }
}

