package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.OrderEntity;
import com.finalpos.POSsystem.Model.DTO.OrderDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T23:34:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDTO toDTO(OrderEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId( entity.getId() );
        orderDTO.setOrderNumber( entity.getOrderNumber() );
        orderDTO.setCustomerId( entity.getCustomerId() );
        orderDTO.setStaffId( entity.getStaffId() );
        orderDTO.setTaxrate( entity.getTaxrate() );
        orderDTO.setSub_total( entity.getSub_total() );
        orderDTO.setCash( entity.getCash() );
        orderDTO.setChange( entity.getChange() );
        orderDTO.setTotal( entity.getTotal() );
        orderDTO.setQuantity( entity.getQuantity() );
        orderDTO.setPaymentMethod( entity.getPaymentMethod() );
        orderDTO.setCreated_date( entity.getCreated_date() );
        orderDTO.setTaxfee( entity.getTaxfee() );

        return orderDTO;
    }

    @Override
    public OrderEntity toEntity(OrderDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setId( dto.getId() );
        orderEntity.setOrderNumber( dto.getOrderNumber() );
        orderEntity.setCustomerId( dto.getCustomerId() );
        orderEntity.setStaffId( dto.getStaffId() );
        orderEntity.setTaxrate( dto.getTaxrate() );
        orderEntity.setTaxfee( dto.getTaxfee() );
        orderEntity.setSub_total( dto.getSub_total() );
        orderEntity.setCash( dto.getCash() );
        orderEntity.setChange( dto.getChange() );
        orderEntity.setTotal( dto.getTotal() );
        orderEntity.setQuantity( dto.getQuantity() );
        orderEntity.setPaymentMethod( dto.getPaymentMethod() );
        orderEntity.setCreated_date( dto.getCreated_date() );

        return orderEntity;
    }
}
