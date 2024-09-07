package org.telatenko.storagesevicedomain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.modelType.ProductType;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;
import org.telatenko.storagesevicedomain.persistence.ProductRepository;
import org.telatenko.storagesevicedomain.seach.criteria.BigDecimalSearchCriteria;
import org.telatenko.storagesevicedomain.seach.criteria.LocalDateTimeSearchCriteria;
import org.telatenko.storagesevicedomain.seach.criteria.OperationType;
import org.telatenko.storagesevicedomain.seach.criteria.SearchCriteria;
import org.telatenko.storagesevicedomain.seach.criteria.StringSearchCriteria;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProductServiceImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgres::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

        productRepository.deleteAll();

        ProductEntity product1 = new ProductEntity();
        product1.setId(UUID.randomUUID());
        product1.setTitle("Product 1");
        product1.setDescription("Description");
        product1.setArticle("123");
        product1.setProductType(ProductType.TECH);
        product1.setPrice(BigDecimal.valueOf(100.00));
        product1.setQuantity(BigDecimal.valueOf(100.00));
        product1.setLastModified(OffsetDateTime.now());
        product1.setCreatedAd(LocalDate.now());

        ProductEntity product2 = new ProductEntity();
        product2.setId(UUID.randomUUID());
        product2.setTitle("Product 2");
        product2.setDescription("Description");
        product2.setArticle("321");
        product2.setProductType(ProductType.DRINK);
        product2.setPrice(BigDecimal.valueOf(200.00));
        product2.setQuantity(BigDecimal.valueOf(200.00));
        product2.setLastModified(OffsetDateTime.now());
        product2.setCreatedAd(LocalDate.now());

        productRepository.saveAll(Arrays.asList(product1, product2));

        List<ProductEntity> savedProducts = productRepository.findAll();
        savedProducts.forEach(product -> System.out.println("Saved product: " + product));
    }

    @Test
    @Transactional
    void testSearchProducts_Equals() {

        SearchCriteria<String> criteria = new StringSearchCriteria("title", OperationType.EQUALS, "Product 1");

        List<ProductDto> result = productService.searchProducts(List.of(criteria), "USD");

        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getTitle());
    }

    @Test
    @Transactional
    void testSearchProducts_Like() {

        SearchCriteria<String> criteria = new StringSearchCriteria("title", OperationType.LIKE, "Product%");

        List<ProductDto> result = productService.searchProducts(List.of(criteria), "USD");

        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    void testSearchProducts_GreaterThanOrEqual() {

        SearchCriteria<BigDecimal> criteria = new BigDecimalSearchCriteria("price", OperationType.GREATER_THAN_OR_EQUAL, BigDecimal.valueOf(150.0));

        List<ProductDto> result = productService.searchProducts(List.of(criteria), "USD");

        assertEquals(1, result.size());
        assertEquals("Product 2", result.get(0).getTitle());
    }

    @Test
    @Transactional
    void testSearchProducts_LessThanOrEqual() {

        SearchCriteria<BigDecimal> criteria = new BigDecimalSearchCriteria("price", OperationType.LESS_THAN_OR_EQUAL, BigDecimal.valueOf(150.0));

        List<ProductDto> result = productService.searchProducts(List.of(criteria), "USD");

        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getTitle());
    }

    @Test
    @Transactional
    void testSearchProducts_ComplexQuery() {

        SearchCriteria<BigDecimal> priceLessThanOrEqual = new BigDecimalSearchCriteria("price", OperationType.LESS_THAN_OR_EQUAL, BigDecimal.valueOf(1100.0));
        SearchCriteria<BigDecimal> priceGreaterThanOrEqual = new BigDecimalSearchCriteria("price", OperationType.GREATER_THAN_OR_EQUAL, BigDecimal.valueOf(240.1));
        SearchCriteria<LocalDateTime> createdAdEquals = new LocalDateTimeSearchCriteria("createdAd", OperationType.EQUALS, LocalDateTime.parse("2024-09-05T10:49:31"));
        SearchCriteria<String> titleLike = new StringSearchCriteria("title", OperationType.LIKE, "Watch");

        List<ProductDto> result = productService.searchProducts(List.of(priceLessThanOrEqual, priceGreaterThanOrEqual, createdAdEquals, titleLike), "USD");

        assertEquals(0, result.size());
    }
}