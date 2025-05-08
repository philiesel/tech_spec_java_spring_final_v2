package com.example.demo.service;

import com.example.demo.dto.SubscriptionDto;
import com.example.demo.dto.UserSubscriptionDto;
import com.example.demo.entity.SubscriptionEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exceptions.DuplicateSubscriptionException;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    private boolean checkSubscription(String nameSubscription) {
        if (subscriptionRepository.existsByName(nameSubscription)) {
            throw new DuplicateSubscriptionException("Подписка с названием '" + nameSubscription + "' уже существует");
        }
        return true;
    }

    private SubscriptionEntity createSubscriptionEntity(String nameSubscription) {
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setName(nameSubscription);
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public SubscriptionDto createSubscription(SubscriptionDto newSubscription) {
        String nameSubscription = newSubscription.getName();
        checkSubscription(nameSubscription);
        SubscriptionEntity subscription = createSubscriptionEntity(nameSubscription);
        return new SubscriptionDto(subscription.getId(), subscription.getName());
    }

    private UserEntity getUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Пользователя с данным " + userId + " не существует"));
        return user;
    }

    private SubscriptionEntity getSubscription(String nameSubscription) throws NoSuchElementException {
        return subscriptionRepository.findByName(nameSubscription).orElseThrow(
                () -> new NoSuchElementException("Подписки с данным названием " + nameSubscription + " не существует"));
    }

    @Transactional  // TODO при добавлении подписка уже существует
    public UserSubscriptionDto addSubscriptionToUser(Long userId, SubscriptionDto subscriptionReq) {
        UserEntity user = getUser(userId);
        String nameSubscription = subscriptionReq.getName();
        SubscriptionEntity subscription = getSubscription(nameSubscription);
        try {
            user.getSubscriptions().add(subscription);
        } catch (NoSuchElementException ex) {
            SubscriptionEntity newSubscription = createSubscriptionEntity(nameSubscription);
            user.getSubscriptions().add(newSubscription);
        }
        userRepository.save(user);
        UserSubscriptionDto subscriptionResponse = new UserSubscriptionDto(user.getUsername(), subscription.getName());  // TODO поменять на mapstruct
        return subscriptionResponse;
    }

    public List<UserSubscriptionDto> getAllSubscriptionsByUser(Long userId) {
        UserEntity user = getUser(userId);
        Set<SubscriptionEntity> subscription = user.getSubscriptions();
        return subscription.stream().map(
                subscript -> new UserSubscriptionDto(user.getUsername(), subscript.getName()) // TODO поменять на mapstruct
        ).collect(Collectors.toList());
    }

    private SubscriptionEntity getSubscription(Long subId) {
        return subscriptionRepository.findById(subId).orElseThrow(
                () -> new NoSuchElementException("Подписки с данным id " + subId + " не существует"));
    }

    @Transactional
    public void deleteSubscriptionsByUser(Long userId, Long subId) {
        UserEntity user = getUser(userId);
        SubscriptionEntity subscription = getSubscription(subId);
        Set<SubscriptionEntity> subscriptions = user.getSubscriptions();
        if (subscriptions.contains(subscription)) {
            subscriptions.remove(subscription);
        } else {
            throw new IllegalArgumentException("У User нет данной подписки");
        }
    }

    public List<SubscriptionDto> getTopSubscriptions() {
        List<SubscriptionEntity> top3subscriptions = subscriptionRepository.top3subscription();
        return top3subscriptions.stream()
                .map(subscription -> new SubscriptionDto(subscription.getName()))
                .collect(Collectors.toList());
    }
}
