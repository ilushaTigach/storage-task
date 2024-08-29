package org.telatenko.storagesevicedomain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность товара")
public class ProductDto {

    @Schema(description = "Идентификатор товара", example = "38d42f1b-4b11-49a6-9106-6ee486fb15aa")
    private UUID id;

    @Schema(description = "Название товара", example = "Iphone 15")
    private String title;

    @Schema(description = "Артикул товара", example = "A-123")
    private String article;

    @Schema(description = "Описание товара", example = "Good phone")
    private String description;

    @Schema(description = "Тип товара", example = "TECH")
    private ProductType productType;

    @Schema(description = "Цена товара", example = "1000")
    private Integer price;

    @Schema(description = "Количество товара", example = "100")
    private Integer quantity;

    @Schema(description = "Дата и время изменения товара")
    private LocalDateTime lastModified;

    @Schema(description = "Дата и время создания товара")
    private LocalDateTime createdAd;
}
