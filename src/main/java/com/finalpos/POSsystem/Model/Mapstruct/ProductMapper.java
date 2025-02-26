package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.ProductEntity;
import com.finalpos.POSsystem.Model.DTO.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "barcode", target = "barcode")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "import_price", target = "import_price")
    @Mapping(source = "retail_price", target = "retail_price")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "creation_date", target = "creation_date")
    @Mapping(source = "purchase", target = "purchase")
    ProductDTO toDTO(ProductEntity entity);

    ProductEntity toEntity(ProductDTO dto);
}
