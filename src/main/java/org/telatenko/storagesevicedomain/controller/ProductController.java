package org.telatenko.storagesevicedomain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telatenko.storagesevicedomain.model.CreateProductRequest;
import org.telatenko.storagesevicedomain.model.ProductResponse;
import org.telatenko.storagesevicedomain.model.UpdateProductRequest;
import java.util.List;
import java.util.UUID;

@Tag(name = "product resource", description = "Интерфейс взаимодействия с Products")
@RequestMapping
public interface ProductController {

    @Operation(summary = "Вызов всего списка товаров")
    @GetMapping("/api/v1/products")
    List<ProductResponse> getAllProducts(@PageableDefault(size = 3) Pageable pageable);

    @Operation(summary = "Вызов товара по id")
    @GetMapping("/api/v1/product/{id}")
    ProductResponse findProductById(@Valid @PathVariable("id") UUID id);

    @Operation(summary = "Создание нового товара и добавление его в базу данных")
    @PostMapping("/api/v1/product")
    UUID saveProduct(@Valid @RequestBody CreateProductRequest createProductRequest);

    @Operation(summary = "Удаление товара из базы данных по id")
    @DeleteMapping("/api/v1/product/{id}")
    void deleteProductById(@Valid @PathVariable("id") UUID id);

    @Operation(summary = "Изменение параметров товара")
    @PatchMapping("/api/v1/product/{id}")
    UUID updateProduct(@Valid @PathVariable UUID id, @Valid @RequestBody UpdateProductRequest updateProductRequest);
}
