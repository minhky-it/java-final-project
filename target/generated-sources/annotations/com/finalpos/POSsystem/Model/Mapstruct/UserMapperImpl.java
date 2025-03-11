package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.UserEntity;
import com.finalpos.POSsystem.Model.DTO.UserDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T23:34:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( entity.getId() );
        userDTO.setUsername( entity.getUsername() );
        userDTO.setEmail( entity.getEmail() );
        userDTO.setImage( entity.getImage() );
        userDTO.setRole( entity.getRole() );
        userDTO.setStatus( entity.getStatus() );
        userDTO.setCreated_at( entity.getCreated_at() );
        userDTO.setName( entity.getName() );
        userDTO.setPassword( entity.getPassword() );

        return userDTO;
    }

    @Override
    public UserEntity toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( dto.getId() );
        userEntity.setUsername( dto.getUsername() );
        userEntity.setName( dto.getName() );
        userEntity.setEmail( dto.getEmail() );
        userEntity.setPassword( dto.getPassword() );
        userEntity.setImage( dto.getImage() );
        userEntity.setRole( dto.getRole() );
        userEntity.setStatus( dto.getStatus() );
        userEntity.setCreated_at( dto.getCreated_at() );

        return userEntity;
    }
}
