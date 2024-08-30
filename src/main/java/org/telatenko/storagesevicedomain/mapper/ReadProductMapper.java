package org.telatenko.storagesevicedomain.mapper;

import org.mapstruct.Mapper;
import org.telatenko.storagesevicedomain.model.ProductResponse;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;

@Mapper
public interface ReadProductMapper {

    ProductResponse DtoToResponse(ProductDto productDto);

    ProductDto DtoToEntity(ProductEntity productEntity);
}
