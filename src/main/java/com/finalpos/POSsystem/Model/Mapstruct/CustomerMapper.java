package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.CustomerEntity;
import com.finalpos.POSsystem.Model.DTO.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "image", target = "image")
    CustomerDTO toDTO(CustomerEntity entity);

    CustomerEntity toEntity(CustomerDTO dto);

}
