package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SubscriptionsTop3Dto {
    private List<String> topSubscriptions = new ArrayList<>(3);
}
