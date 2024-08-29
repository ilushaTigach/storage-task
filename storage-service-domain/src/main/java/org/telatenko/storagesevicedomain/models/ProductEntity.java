package org.telatenko.storagesevicedomain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.telatenko.storagesevicedomain.dto.ProductType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @NotBlank(message = "Title cannot be blank")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Article cannot be blank")
    @Column(name = "article", unique = true)
    private String article;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @NotNull(message = "ProductEntity type cannot be null")
    @Column(name = "productType")
    private ProductType productType;

    @Min(value = 1, message = "Price cannot be negative")
    @Column(name = "price")
    private Integer price;

    @Min(value = 1, message = "Quantity cannot be negative")
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "created_ad")
    @CreationTimestamp
    private LocalDateTime createdAd;
}
