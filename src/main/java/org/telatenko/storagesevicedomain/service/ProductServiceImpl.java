package org.telatenko.storagesevicedomain.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telatenko.storagesevicedomain.annotation.MeasureExecutionTime;
import org.telatenko.storagesevicedomain.annotation.MeasureTransactionalExecutionTime;
import org.telatenko.storagesevicedomain.currency.service.ExchangeRateService;
import org.telatenko.storagesevicedomain.exeption.ArticleExistsExeption;
import org.telatenko.storagesevicedomain.exeption.DeleteObjectExeption;
import org.telatenko.storagesevicedomain.exeption.ProductNotFoundException;
import org.telatenko.storagesevicedomain.mapper.CreateProductMapper;
import org.telatenko.storagesevicedomain.mapper.FindAllProductsMapper;
import org.telatenko.storagesevicedomain.mapper.ReadProductMapper;
import org.telatenko.storagesevicedomain.dto.UpdateProductDto;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;
import org.telatenko.storagesevicedomain.persistence.ProductRepository;
import org.telatenko.storagesevicedomain.seach.criteria.SearchCriteria;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final EntityManager entityManager;
    private final ExchangeRateService exchangeRateService;


    /**
     * Получает список всех продуктов с пагинацией.
     *
     * @param pageable Объект пагинации.
     * @return Страница с продуктами в формате DTO.
     */
    @MeasureExecutionTime
    public Page<ProductDto> findAllProducts(Pageable pageable, String currency) {
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        return productPage.map(productEntity ->
                readProductMapper.convertToDto(productEntity, convertPrice(productEntity.getPrice(), currency)));
    }

    /**
     * Находит продукт по его идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Продукт в формате DTO.
     * @throws ProductNotFoundException если продукт не найден.
     */

    @MeasureTransactionalExecutionTime
    @Transactional(readOnly = true)
    public ProductDto findProductById(final UUID id, String currency) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("id", id.toString()));
        return readProductMapper.convertToDto(productEntity, convertPrice(productEntity.getPrice(), currency));
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
     * @throws ProductNotFoundException если продукт не найден.
     * @throws ArticleExistsExeption если артикул уже существует.
     */
    @Transactional
    public UUID updateProduct(final UUID id, final UpdateProductDto updateProductDto) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("id", id.toString()));

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

    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(List<SearchCriteria> searchCriteriaList, String currency) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> query = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> root = query.from(ProductEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : searchCriteriaList) {
            Predicate predicate = createPredicate(root, cb, criteria);
            predicates.add(predicate);
        }
        query.where(predicates.toArray(new Predicate[0]));
        List<ProductEntity> resultList = entityManager.createQuery(query).getResultList();
        return resultList.stream().map(productEntity ->
                readProductMapper.convertToDto(productEntity, convertPrice(productEntity.getPrice(), currency))).collect(Collectors.toList());
    }

    private <T> Predicate createPredicate(Root<ProductEntity> root, CriteriaBuilder cb, SearchCriteria<T> criteria) {
        switch (criteria.getOperation()) {
            case EQUALS:
                return criteria.getStrategy().getEqPattern(root.get(criteria.getField()), criteria.getValue(), cb);
            case LIKE:
                return criteria.getStrategy().getLikePattern(root.get(criteria.getField()), criteria.getValue(), cb);
            case GREATER_THAN_OR_EQUAL:
                return criteria.getStrategy().getLeftLimitPattern(root.get(criteria.getField()), criteria.getValue(), cb);
            case LESS_THAN_OR_EQUAL:
                return criteria.getStrategy().getRightLimitPattern(root.get(criteria.getField()), criteria.getValue(), cb);
            default:
                throw new IllegalArgumentException("Unsupported operation: " + criteria.getOperation());
        }
    }

    private BigDecimal convertPrice(BigDecimal price, String currency) {
        Map<String, BigDecimal> exchangeRates = exchangeRateService.getExchangeRates();
        BigDecimal rate = exchangeRates.getOrDefault(currency, BigDecimal.ONE);
        return price.multiply(rate);
    }
}

