package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionDto {
    private Long id;
    @NotNull
    private String name;


    public SubscriptionDto() {}

    public SubscriptionDto(@NotNull String name) {
        this.name = name;
    }

    public SubscriptionDto(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }
}
