package org.telatenko.storagesevicedomain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telatenko.storagesevicedomain.modelType.ProductType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class UpdateProductDto {

    final private String title;

    final private String article;

    final private String description;

    final private ProductType productType;

    final private BigDecimal price;

    final private BigDecimal quantity;

    final private OffsetDateTime lastModified;

    final private LocalDate createdAd;
}
