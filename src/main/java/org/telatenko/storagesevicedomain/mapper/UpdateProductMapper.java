package org.telatenko.storagesevicedomain.mapper;

import org.mapstruct.Mapper;
import org.telatenko.storagesevicedomain.dto.UpdateProductDto;
import org.telatenko.storagesevicedomain.model.UpdateProductRequest;

@Mapper
public interface UpdateProductMapper {

    UpdateProductDto requestToDto(UpdateProductRequest updateProductRequest);

}
