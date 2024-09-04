package org.telatenko.storagesevicedomain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.telatenko.storagesevicedomain.model.CreateProductRequest;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;

@Mapper
public interface CreateProductMapper {

    @Mapping(target = "id", ignore = true)
    ProductDto RequestToDto(CreateProductRequest createProductRequest);

    ProductEntity toEntity(ProductDto productDto);
}
