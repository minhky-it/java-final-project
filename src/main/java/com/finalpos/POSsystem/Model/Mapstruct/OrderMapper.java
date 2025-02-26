package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.OrderEntity;
import com.finalpos.POSsystem.Model.DTO.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "orderNumber", target = "orderNumber")
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "staffId", target = "staffId")
    @Mapping(source = "taxrate", target = "taxrate")
    @Mapping(source = "sub_total", target = "sub_total")
    @Mapping(source = "cash", target = "cash")
    @Mapping(source = "change", target = "change")
    @Mapping(source = "total", target = "total")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "created_date", target = "created_date")
    OrderDTO toDTO(OrderEntity entity);

    OrderEntity toEntity(OrderDTO dto);
}
