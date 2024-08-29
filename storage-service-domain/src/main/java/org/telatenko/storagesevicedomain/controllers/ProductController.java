package org.telatenko.storagesevicedomain.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.mappers.CreateProductMapper;
import org.telatenko.storagesevicedomain.mappers.FindAllProductsMapper;
import org.telatenko.storagesevicedomain.mappers.ReadProductMapper;
import org.telatenko.storagesevicedomain.mappers.UpdateProductMapper;
import org.telatenko.storagesevicedomain.resources.ProductResource;
import org.telatenko.storagesevicedomain.models.CreateProductRequest;
import org.telatenko.storagesevicedomain.models.ProductResponse;
import org.telatenko.storagesevicedomain.models.UpdateProductDto;
import org.telatenko.storagesevicedomain.models.UpdateProductRequest;
import org.telatenko.storagesevicedomain.services.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
public class ProductController implements ProductResource {

    private final ProductService productService;
    private final CreateProductMapper createProductMapper;
    private final ReadProductMapper readProductMapper;
    private final FindAllProductsMapper findAllProductsMapper;
    private final UpdateProductMapper updateProductMapper;

    public List<ProductResponse> getAllProducts() {
        List<ProductDto> productDtos = productService.findAllProducts();
        return findAllProductsMapper.toDtos(productDtos);
    }

    public ProductResponse findProductById(@PathVariable("id") UUID id) {
        ProductDto productDto = productService.findProductById(id);
        return readProductMapper.DtoToResponse(productDto);
    }

    public ProductResponse saveProduct(@RequestBody CreateProductRequest createProductRequest) {
        ProductDto productDto = createProductMapper.RequestToDto(createProductRequest);
        return createProductMapper.DtoToResponse(productService.saveProduct(productDto));
    }

    public void deleteProductById(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
    }

    public ProductResponse updateProduct(@PathVariable UUID id, @RequestBody UpdateProductRequest updateProductRequest) {
        UpdateProductDto updateProductDto = updateProductMapper.requestToDto(updateProductRequest);
        return updateProductMapper.dtoToResponse(productService.updateProduct(id, updateProductDto));
    }
}
