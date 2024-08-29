package org.telatenko.storagesevicedomain.mappers;

import org.mapstruct.Mapper;
import org.telatenko.storagesevicedomain.models.CreateProductRequest;
import org.telatenko.storagesevicedomain.models.ProductResponse;
import org.telatenko.storagesevicedomain.dto.ProductDto;
import org.telatenko.storagesevicedomain.models.ProductEntity;

@Mapper
public interface CreateProductMapper {

    ProductResponse DtoToResponse(ProductDto productDto);

    ProductDto RequestToDto(CreateProductRequest createProductRequest);

    ProductDto toDto(ProductEntity productEntity);

    ProductEntity toEntity(ProductDto productDto);
}
