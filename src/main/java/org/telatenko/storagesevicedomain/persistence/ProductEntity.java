package org.telatenko.storagesevicedomain.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.telatenko.storagesevicedomain.modelType.ProductType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Сущность, представляющая продукт в базе данных.
 */
@Entity
@Table(name = "products")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    /**
     * Идентификатор продукта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    /**
     * Название продукта. Не может быть пустым.
     */
    @NotBlank(message = "Title cannot be blank")
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Артикул продукта. Не может быть пустым и должен быть уникальным.
     */
    @NotBlank(message = "Article cannot be blank")
    @Column(name = "article", unique = true, nullable = false)
    private String article;

    /**
     * Описание продукта. Не может быть пустым.
     */
    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;

    /**
     * Тип продукта. Не может быть пустым.
     */
    @NotNull(message = "ProductEntity type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "productType", nullable = false)
    private ProductType productType;

    /**
     * Цена продукта. Должна быть положительной.Не может быть пустым.
     */
    @Positive(message = "Price cannot be negative")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * Количество продукта. Должно быть положительным.Не может быть пустым.
     */
    @Positive(message = "Quantity cannot be negative")
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    /**
     * Время последнего изменения продукта. Не может быть пустым.
     */
    @Column(name = "last_modified", nullable = false)
    private OffsetDateTime lastModified;

    /**
     * Дата создания продукта. Автоматически устанавливается при создании.
     */
    @Column(name = "created_ad", nullable = false)
    @CreationTimestamp
    private LocalDate createdAd;

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", article='" + article + '\'' +
                ", description='" + description + '\'' +
                ", productType=" + productType +
                ", price=" + price +
                ", quantity=" + quantity +
                ", lastModified=" + lastModified +
                ", createdAd=" + createdAd +
                '}';
    }
}
