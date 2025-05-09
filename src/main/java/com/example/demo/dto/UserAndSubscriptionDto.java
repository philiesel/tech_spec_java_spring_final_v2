package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
public class UserAndSubscriptionDto {
    private String userName;
    private String subscriptions;
}
