package com.example.demo.mapper;


import com.example.demo.dto.UserAndSubscriptionDto;
import com.example.demo.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAndSubscription {
    UserAndSubscription INSTANCE = Mappers.getMapper(UserAndSubscription.class);
    UserAndSubscriptionDto toDto(UserEntity user);
    UserEntity fromDto(UserAndSubscriptionDto userSubscription);
}
