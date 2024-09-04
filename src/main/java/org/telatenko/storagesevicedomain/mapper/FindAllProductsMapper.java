package org.telatenko.storagesevicedomain.mapper;

import org.mapstruct.Mapper;
import org.telatenko.storagesevicedomain.model.ProductResponse;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;
import java.util.List;

@Mapper
public interface FindAllProductsMapper {

    ProductDto toDto(ProductEntity productEntity);

    List<ProductResponse> toDtos(List<ProductDto> productDtos);
}
