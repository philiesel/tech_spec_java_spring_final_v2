package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
public class UserListBySubscriptionDto {
    private String username;
    private Set<String> subscriptions = new HashSet<>();
}
