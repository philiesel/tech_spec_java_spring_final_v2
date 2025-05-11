package com.example.demo.mapper;

import com.example.demo.dto.UserCreateDto;
import com.example.demo.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserCreateDto toDto(UserEntity userEntity);
    UserEntity fromDto(UserCreateDto userCreateDto);
}
