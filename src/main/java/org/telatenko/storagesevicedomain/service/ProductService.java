package org.telatenko.storagesevicedomain.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.dto.UpdateProductDto;
import java.util.UUID;

public interface ProductService {

    Page<ProductDto> findAllProducts(Pageable pageable);

    ProductDto findProductById(@Valid UUID id);

    UUID saveProduct(@Valid ProductDto product);

    UUID updateProduct(@Valid UUID id, @Valid UpdateProductDto updateProductDto);

    void deleteProductById(@Valid UUID id);

}
