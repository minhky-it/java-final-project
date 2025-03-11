package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.OrderDetailEntity;
import com.finalpos.POSsystem.Entity.ProductCartEntity;
import com.finalpos.POSsystem.Model.DTO.OrderDetailDTO;
import java.util.ArrayList;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T23:34:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
public class OrderDetailMapperImpl implements OrderDetailMapper {

    @Override
    public OrderDetailDTO toDTO(OrderDetailEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

        orderDetailDTO.setId( entity.getOrder_id() );
        orderDetailDTO.setOrderNumber( entity.getOrderNumber() );
        ArrayList<ProductCartEntity> arrayList = entity.getProducts();
        if ( arrayList != null ) {
            orderDetailDTO.setProducts( new ArrayList<ProductCartEntity>( arrayList ) );
        }
        orderDetailDTO.setOrder_id( entity.getOrder_id() );

        return orderDetailDTO;
    }

    @Override
    public OrderDetailEntity toEntity(OrderDetailDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();

        orderDetailEntity.setId( dto.getId() );
        orderDetailEntity.setOrder_id( dto.getOrder_id() );
        orderDetailEntity.setOrderNumber( dto.getOrderNumber() );
        ArrayList<ProductCartEntity> arrayList = dto.getProducts();
        if ( arrayList != null ) {
            orderDetailEntity.setProducts( new ArrayList<ProductCartEntity>( arrayList ) );
        }

        return orderDetailEntity;
    }
}
