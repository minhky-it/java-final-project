package com.finalpos.POSsystem.Model.Mapstruct;

import com.finalpos.POSsystem.Entity.UserEntity;
import com.finalpos.POSsystem.Model.DTO.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "created_at", target = "created_at")
    UserDTO toDTO(UserEntity entity);

    UserEntity toEntity(UserDTO dto);
}
