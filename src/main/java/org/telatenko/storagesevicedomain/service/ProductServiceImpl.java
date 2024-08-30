package org.telatenko.storagesevicedomain.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CreateProductMapper createProductMapper;
    private final ReadProductMapper readProductMapper;
    private final FindAllProductsMapper findAllProductsMapper;

    public Page<ProductDto> findAllProducts(Pageable pageable) {
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        return productPage.map(findAllProductsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ProductDto findProductById(final UUID id) {
        return readProductMapper.DtoToEntity(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException("id", id.toString())));
    }

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

    public void deleteProductById(final UUID id) {
        productRepository.findById(id).orElseThrow(() -> new DeleteObjectExeption());
        productRepository.deleteById(id);
    }

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

