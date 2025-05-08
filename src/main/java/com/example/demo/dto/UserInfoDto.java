package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserInfoDto {
    @NotNull
    private String username;
    private Set<SubscriptionDto> subscriptions = new HashSet<>();
}
