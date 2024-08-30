package org.telatenko.storagesevicedomain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.mapper.CreateProductMapper;
import org.telatenko.storagesevicedomain.mapper.FindAllProductsMapper;
import org.telatenko.storagesevicedomain.mapper.ReadProductMapper;
import org.telatenko.storagesevicedomain.mapper.UpdateProductMapper;
import org.telatenko.storagesevicedomain.model.CreateProductRequest;
import org.telatenko.storagesevicedomain.model.ProductResponse;
import org.telatenko.storagesevicedomain.dto.UpdateProductDto;
import org.telatenko.storagesevicedomain.model.UpdateProductRequest;
import org.telatenko.storagesevicedomain.service.ProductServiceImpl;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductServiceImpl productServiceImpl;
    private final CreateProductMapper createProductMapper;
    private final ReadProductMapper readProductMapper;
    private final FindAllProductsMapper findAllProductsMapper;
    private final UpdateProductMapper updateProductMapper;

    public List<ProductResponse> getAllProducts(@PageableDefault(size = 3) Pageable pageable) {
        Page<ProductDto> productDtos = productServiceImpl.findAllProducts(pageable);
        return findAllProductsMapper.toDtos(productDtos.getContent());
    }

    public ProductResponse findProductById(@Valid @PathVariable("id") UUID id) {
        ProductDto productDto = productServiceImpl.findProductById(id);
        return readProductMapper.DtoToResponse(productDto);
    }

    public UUID saveProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        ProductDto productDto = createProductMapper.RequestToDto(createProductRequest);
        return productServiceImpl.saveProduct(productDto);
    }

    public void deleteProductById(@Valid @PathVariable("id") UUID id) {
        productServiceImpl.deleteProductById(id);
    }

    public UUID updateProduct(@Valid @PathVariable UUID id, @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        UpdateProductDto updateProductDto = updateProductMapper.requestToDto(updateProductRequest);
        return productServiceImpl.updateProduct(id, updateProductDto);
    }
}
