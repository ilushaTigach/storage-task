package org.telatenko.storagesevicedomain.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.telatenko.storagesevicedomain.models.CreateProductRequest;
import org.telatenko.storagesevicedomain.models.ProductResponse;
import org.telatenko.storagesevicedomain.models.UpdateProductRequest;
import java.util.List;
import java.util.UUID;

@Tag(name = "product resource", description = "Интерфейс взаимодействия с Products")
@RequestMapping
public interface ProductResource {

    @Operation(summary = "Вызов всего списка товаров")
    @GetMapping("/api/v1/products")
    List<ProductResponse> getAllProducts();

    @Operation(summary = "Вызов товара по id")
    @GetMapping("/api/v1/product/{id}")
    ProductResponse findProductById(@PathVariable("id") UUID id);

    @Operation(summary = "Создание нового товара и добавление его в базу данных")
    @PostMapping("/api/v1/product")
    ProductResponse saveProduct(@RequestBody CreateProductRequest createProductRequest);

    @Operation(summary = "Удаление товара из базы данных по id")
    @DeleteMapping("/api/v1/product/{id}")
    void deleteProductById(@PathVariable("id") UUID id);

    @Operation(summary = "Изменение параметров товара")
    @PatchMapping("/api/v1/product/{id}")
    ProductResponse updateProduct(@PathVariable UUID id, @RequestBody UpdateProductRequest updateProductRequest);
}
