package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.OrderDetailEntity;
import com.finalpos.POSsystem.Model.DTO.OrderDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    @Mapping(source = "order_id", target = "id")
    @Mapping(source = "orderNumber", target = "orderNumber")
    @Mapping(source = "products", target = "products")
    OrderDetailDTO toDTO(OrderDetailEntity entity);

    OrderDetailEntity toEntity(OrderDetailDTO dto);
}
