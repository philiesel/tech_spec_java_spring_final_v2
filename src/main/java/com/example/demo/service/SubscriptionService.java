package com.example.demo.service;

import com.example.demo.dto.SubscriptionDto;
import com.example.demo.dto.UserAndSubscriptionDto;
import com.example.demo.entity.SubscriptionEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exceptions.DuplicateSubscriptionException;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    private boolean checkSubscription(String nameSubscription) {
        log.info("Проверка уникальности подписки: name={}", nameSubscription);
        if (subscriptionRepository.existsByName(nameSubscription)) {
            log.warn("Обнаружен дубликат подписки");
            throw new DuplicateSubscriptionException("Подписка с названием '" + nameSubscription + "' уже существует");
        }
        log.info("Проверка уникальности пройдена успешно");
        return true;
    }

    private SubscriptionEntity createSubscriptionEntity(String nameSubscription) {
        log.info("Создание  подписки: name={}", nameSubscription);
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setName(nameSubscription);
        SubscriptionEntity createdSubscription = subscriptionRepository.save(subscription);
        log.info("Новая подписка создана: name={}", nameSubscription);
        return createdSubscription;
    }

    @Transactional
    public SubscriptionDto createSubscription(SubscriptionDto newSubscription) {
        String nameSubscription = newSubscription.getName();
        checkSubscription(nameSubscription);
        SubscriptionEntity subscription = createSubscriptionEntity(nameSubscription);
        return new SubscriptionDto(subscription.getId(), subscription.getName());
    }

    private UserEntity getUser(Long userId) {
        log.info("Получение пользователя с id: {} из бд ", String.valueOf(userId));
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Пользователя с данным " + userId + " не существует"));
        return user;
    }

    private SubscriptionEntity getSubscription(String nameSubscription) throws NoSuchElementException {
        return subscriptionRepository.findByName(nameSubscription).orElseThrow(
                () -> new EntityNotFoundException("Подписки с данным названием " + nameSubscription + " не существует"));
    }

    @Transactional
    public UserAndSubscriptionDto addSubscriptionToUser(Long userId, SubscriptionDto subscriptionReq) {
        log.info("User c id: {} добавление подписки {}", String.valueOf(userId), subscriptionReq.getName());
        String nameSubscription = subscriptionReq.getName();
        UserEntity user = getUser(userId); //TODO переопределить equals и haschcode
        SubscriptionEntity subscription = subscriptionRepository.findByName(nameSubscription)
                .orElseGet(() -> createSubscriptionEntity(nameSubscription));
        if (user.getSubscriptions().contains(subscription)) {
            log.info("У пользователя с id: {} уже есть подписка: {}", user.getId(), subscription.getName());
        } else {
            user.getSubscriptions().add(subscription);
            userRepository.save(user);
        }
        log.info("Пользователю с id: {} добавлена подписка: {} ", String.valueOf(userId), nameSubscription);
        return new UserAndSubscriptionDto(user.getUsername(), nameSubscription);
    }

    public List<UserAndSubscriptionDto> getAllSubscriptionsByUser(Long userId) {
        log.info("Просмотр подписок user_id: {} ", String.valueOf(userId));
        UserEntity user = getUser(userId);
        Set<SubscriptionEntity> subscriptions = user.getSubscriptions();
        return subscriptions.stream().map(
                subscript -> new UserAndSubscriptionDto(user.getUsername(), subscript.getName())
        ).collect(Collectors.toList());
    }

    private SubscriptionEntity getSubscription(Long subId) {
        return subscriptionRepository.findById(subId).orElseThrow(
                () -> new NoSuchElementException("Подписки с данным id " + subId + " не существует"));
    }

    @Transactional
    public void deleteSubscriptionsByUser(Long userId, Long subId) {
        log.info("Удаление подписки c subId: {} у userId: {} ", String.valueOf(subId), String.valueOf(userId));
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
        log.info("Просмотр Топ 3 подписок");
        List<SubscriptionEntity> top3subscriptions = subscriptionRepository.top3subscription();
        List<SubscriptionDto> listSubscription = top3subscriptions.stream()
                .map(subscription -> new SubscriptionDto(subscription.getName()))
                .collect(Collectors.toList());
        return listSubscription;
    }
}
