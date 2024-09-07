package org.telatenko.storagesevicedomain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telatenko.storagesevicedomain.currency.provider.CurrencyProvider;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.mapper.CreateProductMapper;
import org.telatenko.storagesevicedomain.mapper.ReadProductMapper;
import org.telatenko.storagesevicedomain.mapper.UpdateProductMapper;
import org.telatenko.storagesevicedomain.model.CreateProductRequest;
import org.telatenko.storagesevicedomain.model.ProductResponse;
import org.telatenko.storagesevicedomain.dto.UpdateProductDto;
import org.telatenko.storagesevicedomain.model.UpdateProductRequest;
import org.telatenko.storagesevicedomain.seach.criteria.SearchCriteria;
import org.telatenko.storagesevicedomain.service.ProductServiceImpl;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация контроллера для управления продуктами.
 * Этот контроллер предоставляет RESTful API для выполнения операций CRUD над продуктами.
 */
@RestController
@Validated
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductServiceImpl productServiceImpl;
    private final CreateProductMapper createProductMapper;
    private final ReadProductMapper readProductMapper;
    private final UpdateProductMapper updateProductMapper;
    private final CurrencyProvider currencyProvider;

    /**
     * Получает список всех продуктов с пагинацией.
     *
     * @param pageable Объект пагинации, по умолчанию размер страницы равен 3.
     * @return Список ответов с информацией о продуктах.
     */
    public List<ProductResponse> getAllProducts(@PageableDefault(size = 3) Pageable pageable) {
        String currency = currencyProvider.getCurrency();
        Page<ProductDto> productDtos = productServiceImpl.findAllProducts(pageable, currency);
        return productDtos.getContent().stream()
                .map(productDto -> readProductMapper.DtoToResponse(productDto, currency))
                .collect(Collectors.toList());
    }

    /**
     * Находит продукт по его уникальному идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Ответ с информацией о продукте.
     */
    public ProductResponse findProductById(@Valid @PathVariable("id") UUID id) {
        String currency = currencyProvider.getCurrency();
        ProductDto productDto = productServiceImpl.findProductById(id, currency);
        return readProductMapper.DtoToResponse(productDto, currency);
    }

    /**
     * Сохраняет новый продукт.
     *
     * @param createProductRequest Запрос на создание продукта.
     * @return Идентификатор сохраненного продукта.
     */
    public UUID saveProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        ProductDto productDto = createProductMapper.RequestToDto(createProductRequest);
        return productServiceImpl.saveProduct(productDto);
    }

    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param id Идентификатор продукта.
     */
    public void deleteProductById(@Valid @PathVariable("id") UUID id) {
        productServiceImpl.deleteProductById(id);
    }

    /**
     * Обновляет информацию о продукте.
     *
     * @param id Идентификатор продукта.
     * @param updateProductRequest Запрос на обновление продукта.
     * @return Иmvn spring-boot:runдентификатор обновленного продукта.
     */
    public UUID updateProduct(@Valid @PathVariable UUID id, @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        UpdateProductDto updateProductDto = updateProductMapper.requestToDto(updateProductRequest);
        return productServiceImpl.updateProduct(id, updateProductDto);
    }

    /**
     * Поиск продуктов по заданным критериям.
     *
     * @param searchCriteriaList Список критериев поиска, которые определяют условия фильтрации продуктов.
     *                           Каждый критерий содержит поле, операцию и значение для сравнения.
     * @return Список объектов {@link ProductResponse}, представляющих найденные продукты,
     *         отфильтрованные по заданным критериям.
     */
    public List<ProductResponse> searchProducts(@RequestBody List<SearchCriteria> searchCriteriaList) {
        String currency = currencyProvider.getCurrency();
        List<ProductDto> productDtos = productServiceImpl.searchProducts(searchCriteriaList, currency);
        return productDtos.stream()
                .map(productDto -> readProductMapper.DtoToResponse(productDto, currency))
                .collect(Collectors.toList());
    }
}
