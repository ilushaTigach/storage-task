package org.telatenko.storagesevicedomain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telatenko.storagesevicedomain.modelType.ProductType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private String title;

    private String article;

    private String description;

    private ProductType productType;

    private BigDecimal price;

    private BigDecimal quantity;

    private OffsetDateTime lastModified;

    private LocalDate createdAd;

    private String currency;
}

