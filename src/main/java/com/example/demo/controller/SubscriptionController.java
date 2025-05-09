package com.example.demo.controller;

import com.example.demo.dto.SubscriptionDto;
import com.example.demo.dto.UserAndSubscriptionDto;
import com.example.demo.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/create")
    public ResponseEntity<SubscriptionDto> createSubscription(
            @RequestBody SubscriptionDto subscription) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(subscription.getId())
                .toUri();
        SubscriptionDto subCreate = subscriptionService.createSubscription(subscription);
        return ResponseEntity.created(location).body(subCreate);
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<UserAndSubscriptionDto> addSubscriptionToUser(
            @PathVariable("id") Long userId,
            @RequestBody SubscriptionDto subscription) {
        UserAndSubscriptionDto sub = subscriptionService.addSubscriptionToUser(userId, subscription);
        return ResponseEntity.ok().body(sub);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<UserAndSubscriptionDto>> getAllSubscriptionsUser(
            @PathVariable("id") Long userId) {
        List<UserAndSubscriptionDto> listSubscription = subscriptionService.getAllSubscriptionsByUser(userId);
        return ResponseEntity.ok().body(listSubscription);
    }

    @DeleteMapping("/users/{id}/subscriptions/{sub_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscriptionsByUser(
            @PathVariable("id") Long userId,
            @PathVariable("sub_id") Long subId) {
        try {
            subscriptionService.deleteSubscriptionsByUser(userId, subId);
        } catch (IllegalArgumentException exep) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User с " + userId + "нет");
        }
    }

    @GetMapping("/favorite/top")
    public ResponseEntity<List<SubscriptionDto>> getTopSubscriptions() {
        List<SubscriptionDto> topList = subscriptionService.getTopSubscriptions();
        return ResponseEntity.ok().body(topList);
    }
}
