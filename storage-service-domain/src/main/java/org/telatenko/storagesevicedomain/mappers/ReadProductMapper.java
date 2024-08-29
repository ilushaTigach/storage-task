package org.telatenko.storagesevicedomain.mappers;

import org.mapstruct.Mapper;
import org.telatenko.storagesevicedomain.models.ProductResponse;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.models.ProductEntity;

@Mapper
public interface ReadProductMapper {

    ProductResponse DtoToResponse(ProductDto productDto);

    ProductDto DtoToEntity(ProductEntity productEntity);
}
