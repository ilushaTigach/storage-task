package org.telatenko.storagesevicedomain.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

@Entity
@Table(name = "products")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @NotBlank(message = "Title cannot be blank")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Article cannot be blank")
    @Column(name = "article", unique = true, nullable = false)
    private String article;

    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;

    @NotNull(message = "ProductEntity type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "productType", nullable = false)
    private ProductType productType;

    @Positive(message = "Price cannot be negative")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Positive(message = "Quantity cannot be negative")
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "last_modified", nullable = false)
    private OffsetDateTime lastModified;

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
