package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.CustomerEntity;
import com.finalpos.POSsystem.Model.DTO.CustomerDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T23:34:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDTO toDTO(CustomerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId( entity.getId() );
        customerDTO.setName( entity.getName() );
        customerDTO.setPhone( entity.getPhone() );
        customerDTO.setAddress( entity.getAddress() );
        customerDTO.setImage( entity.getImage() );

        return customerDTO;
    }

    @Override
    public CustomerEntity toEntity(CustomerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setId( dto.getId() );
        customerEntity.setName( dto.getName() );
        customerEntity.setPhone( dto.getPhone() );
        customerEntity.setAddress( dto.getAddress() );
        customerEntity.setImage( dto.getImage() );

        return customerEntity;
    }
}
