package org.telatenko.storagesevicedomain.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telatenko.storagesevicedomain.dto.ProductType;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    private UUID id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Article cannot be blank")
    private String article;

    private String description;

    @NotNull(message = "ProductEntity type cannot be null")
    private ProductType productType;

    @Min(value = 1, message = "Price cannot be negative")
    private Integer price;

    @Min(value = 1, message = "Quantity cannot be negative")
    private Integer quantity;

    private LocalDateTime lastModified;

    private LocalDateTime createdAd;
}

