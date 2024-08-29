package org.telatenko.storagesevicedomain.mappers;

import org.mapstruct.Mapper;
import org.telatenko.storagesevicedomain.models.ProductResponse;
import org.telatenko.storagesevicedomain.models.UpdateProductDto;
import org.telatenko.storagesevicedomain.models.UpdateProductRequest;
import org.telatenko.storagesevicedomain.models.ProductEntity;

@Mapper
public interface UpdateProductMapper {

    UpdateProductDto toDto(ProductEntity productEntity);

    UpdateProductDto requestToDto(UpdateProductRequest updateProductRequest);

    ProductResponse dtoToResponse(UpdateProductDto productDto);
}
