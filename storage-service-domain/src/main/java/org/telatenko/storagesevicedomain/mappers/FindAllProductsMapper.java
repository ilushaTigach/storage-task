package org.telatenko.storagesevicedomain.mappers;

import org.mapstruct.Mapper;
import org.telatenko.storagesevicedomain.models.ProductResponse;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.models.ProductEntity;

import java.util.List;

@Mapper
public interface FindAllProductsMapper {
    
    List<ProductResponse> toDtos(List<ProductDto> productDtos);


    List<ProductDto> toDto(List<ProductEntity> all);
}
