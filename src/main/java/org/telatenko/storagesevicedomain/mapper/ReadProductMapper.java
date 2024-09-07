package org.telatenko.storagesevicedomain.mapper;

import org.mapstruct.Mapper;
import org.telatenko.storagesevicedomain.model.ProductResponse;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;
import java.math.BigDecimal;

@Mapper
public interface ReadProductMapper {
    ProductResponse DtoToResponse(ProductDto productDto, String currency);
    ProductDto DtoToEntity(ProductEntity productEntity);

    default ProductDto convertToDto(ProductEntity productEntity, BigDecimal convertedPrice) {
        return new ProductDto(
                productEntity.getId(),
                productEntity.getTitle(),
                productEntity.getArticle(),
                productEntity.getDescription(),
                productEntity.getProductType(),
                convertedPrice,
                productEntity.getQuantity(),
                productEntity.getLastModified(),
                productEntity.getCreatedAd()
        );
    }
}
