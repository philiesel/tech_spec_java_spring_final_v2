package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
public class UserSubscriptionDto {
    private String userName;
    private String nameSubscription;
}
