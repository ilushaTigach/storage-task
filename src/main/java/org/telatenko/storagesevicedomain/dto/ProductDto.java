package org.telatenko.storagesevicedomain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.telatenko.storagesevicedomain.modelType.ProductType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность товара")
public class ProductDto {

    @Schema(description = "Идентификатор товара", example = "38d42f1b-4b11-49a6-9106-6ee486fb15aa")
    final private UUID id;

    @Schema(description = "Название товара", example = "Iphone 15")
    final private String title;

    @Schema(description = "Артикул товара", example = "A-123")
    final private String article;

    @Schema(description = "Описание товара", example = "Good phone")
    final private String description;

    @Schema(description = "Тип товара", example = "TECH")
    final private ProductType productType;

    @Schema(description = "Цена товара", example = "1000")
    final private BigDecimal price;

    @Schema(description = "Количество товара", example = "100")
    final private BigDecimal quantity;

    @Schema(description = "Дата и время изменения товара")
    final private OffsetDateTime lastModified;

    @Schema(description = "Дата и время создания товара")
    final private LocalDate createdAd;

}
