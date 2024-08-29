package org.telatenko.storagesevicedomain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.telatenko.storagesevicedomain.models.UpdateProductDto;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.exeption.NotFoundProductException;
import org.telatenko.storagesevicedomain.mappers.*;
import org.telatenko.storagesevicedomain.models.ProductEntity;
import org.telatenko.storagesevicedomain.repositories.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final CreateProductMapper createProductMapper;
    private final ReadProductMapper readProductMapper;
    private final FindAllProductsMapper findAllProductsMapper;
    private final UpdateProductMapper updateProductMapper;

    public List<ProductDto> findAllProducts() {
        Pageable pageable = PageRequest.of(0, 3);
        return findAllProductsMapper.toDto(productRepository.findAll(pageable).getContent());
    }

    public ProductDto findProductById(UUID id) {
        return readProductMapper.DtoToEntity(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException("id", id.toString())));
    }

    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {

        String article = productDto.getArticle();
        if (article != null) {
            ProductEntity existingProduct = productRepository.findByArticle(article);
            if (existingProduct != null) {
                throw new IllegalArgumentException("Product with article " + article + " already exists.");
            }
        }

        ProductEntity productEntity = createProductMapper.toEntity(productDto);
        return createProductMapper.toDto(productRepository.save(productEntity));
    }

    public void deleteProductById(UUID id) {
        productRepository.findById(id).orElseThrow(() -> new NotFoundProductException("id", id.toString()));
        productRepository.deleteById(id);
    }

    @Transactional
    public UpdateProductDto updateProduct(UUID id, UpdateProductDto updateProductDto) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException("id", id.toString()));

        String article = updateProductDto.getArticle();
        if (article != null && !article.equals(productEntity.getArticle())) {
            ProductEntity existingProduct = productRepository.findByArticle(article);
            if (existingProduct != null && !existingProduct.getId().equals(id)) {
                throw new IllegalArgumentException("Product with article " + article + " already exists.");
            }
        }

        if (!Objects.equals(productEntity.getQuantity(), updateProductDto.getQuantity())) {
            productEntity.setLastModified(LocalDateTime.now());
        }

        productEntity.setArticle(updateProductDto.getArticle());
        productEntity.setTitle(updateProductDto.getTitle());
        productEntity.setDescription(updateProductDto.getDescription());
        productEntity.setProductType(updateProductDto.getProductType());
        productEntity.setPrice(updateProductDto.getPrice());
        productEntity.setQuantity(updateProductDto.getQuantity());
        return updateProductMapper.toDto(productRepository.save(productEntity));
    }
}

