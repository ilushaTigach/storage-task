package org.telatenko.storagesevicedomain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.dto.UpdateProductDto;
import org.telatenko.storagesevicedomain.mapper.CreateProductMapper;
import org.telatenko.storagesevicedomain.mapper.FindAllProductsMapper;
import org.telatenko.storagesevicedomain.mapper.ReadProductMapper;
import org.telatenko.storagesevicedomain.mapper.UpdateProductMapper;
import org.telatenko.storagesevicedomain.model.CreateProductRequest;
import org.telatenko.storagesevicedomain.model.UpdateProductRequest;
import org.telatenko.storagesevicedomain.modelType.ProductType;
import org.telatenko.storagesevicedomain.service.ProductServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@WebMvcTest(ProductControllerImpl.class)
@ActiveProfiles("test")
public class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productServiceImpl;

    @MockBean
    private CreateProductMapper createProductMapper;

    @MockBean
    private ReadProductMapper readProductMapper;

    @MockBean
    private UpdateProductMapper updateProductMapper;

    @MockBean
    private FindAllProductsMapper findAllProductsMapper;

    @Test
    @DisplayName("Тест метода getAllProducts на возвращение OK статуса")
    void givenPageable_whenGetAllProducts_thenReturnOkStatus() throws Exception {
        Page<ProductDto> productDtos = new PageImpl<>(Collections.emptyList());
        when(productServiceImpl.findAllProducts(any(Pageable.class))).thenReturn(productDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(productServiceImpl).findAllProducts(any(Pageable.class));
        verifyNoMoreInteractions(productServiceImpl);
    }

    @Test
    @DisplayName("Тест метода findProductById на возвращение OK статуса")
    void givenId_whenFindProductById_thenReturnOkStatus() throws Exception {
        UUID id = UUID.randomUUID();
        ProductDto productDto = new ProductDto(UUID.randomUUID(), "Car", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100), OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));
        when(productServiceImpl.findProductById(id)).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(productServiceImpl).findProductById(id);
        verifyNoMoreInteractions(productServiceImpl);
    }

    @Test
    @DisplayName("Тест метода saveProduct на возвращение OK статуса")
    void givenValidCreateProductRequest_whenSaveProduct_thenReturnOkStatus() throws Exception {
        CreateProductRequest request = createValidCreateProductRequest();
        ProductDto productDto = new ProductDto(UUID.randomUUID(), "Car", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100), OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));
        when(createProductMapper.RequestToDto(request)).thenReturn(productDto);
        when(productServiceImpl.saveProduct(productDto)).thenReturn(UUID.randomUUID());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(request)))
                .andExpect(status().isOk());
        verify(productServiceImpl).saveProduct(productDto);
        verifyNoMoreInteractions(productServiceImpl);
    }

    @Test
    @DisplayName("Тест метода deleteProductById на возвращение OK статуса")
    void givenId_whenDeleteProductById_thenReturnNoContentStatus() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/product/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(productServiceImpl).deleteProductById(id);
        verifyNoMoreInteractions(productServiceImpl);
    }

    @Test
    @DisplayName("Тест метода updateProduct на возвращение OK статуса")
    void givenValidUpdateProductRequest_whenUpdateProduct_thenReturnOkStatus() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateProductRequest request = createValidUpdateProductRequest();
        UpdateProductDto updateProductDto = new UpdateProductDto("Car", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100),  OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));
        when(updateProductMapper.requestToDto(request)).thenReturn(updateProductDto);
        when(productServiceImpl.updateProduct(id, updateProductDto)).thenReturn(id);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/product/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(request)))
                .andExpect(status().isOk());
        verify(productServiceImpl).updateProduct(id, updateProductDto);
        verifyNoMoreInteractions(productServiceImpl);
    }

    @Test
    @DisplayName("Тест метода saveProduct на возвращение BAD_REQUEST статуса")
    void givenInvalidCreateProductRequest_whenSaveProduct_thenReturnBadRequestStatus() throws Exception {
        CreateProductRequest request = new CreateProductRequest("", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100), OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Тест метода updateProduct на возвращение BAD_REQUEST статуса")
    void givenInvalidUpdateProductRequest_whenUpdateProduct_thenReturnBadRequestStatus() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateProductRequest request = new UpdateProductRequest(id, "", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100), OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/product/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Тест метода findProductById на возвращение UNPROCESSABLE_ENTITY статуса")
    void givenInvalidId_whenFindProductById_thenReturnUnprocessableEntityStatus() throws Exception {
        String invalidId = "invalid-id";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Тест метода deleteProductById на возвращение UNPROCESSABLE_ENTITY статуса")
    void givenInvalidId_whenDeleteProductById_thenReturnBadRequestStatus() throws Exception {
        String invalidId = "invalid-id";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/product/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    private CreateProductRequest createValidCreateProductRequest() {
        return new CreateProductRequest("Car", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100),  OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));
    }

    private UpdateProductRequest createValidUpdateProductRequest() {
        return new UpdateProductRequest(UUID.randomUUID(),"Car", "777", "Simple description", ProductType.TECH,
                new BigDecimal(100), new BigDecimal(100),  OffsetDateTime.parse("2024-08-27T16:41:40.581Z"),
                LocalDate.parse("2024-08-27"));
    }

    private String toJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}