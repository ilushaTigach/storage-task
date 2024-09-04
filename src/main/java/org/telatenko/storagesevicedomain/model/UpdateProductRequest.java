package org.telatenko.storagesevicedomain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telatenko.storagesevicedomain.modelType.ProductType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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
    private BigDecimal price;

    @Min(value = 1, message = "Quantity cannot be negative")
    private BigDecimal quantity;

    private OffsetDateTime lastModified;

    private LocalDate createdAd;
}

