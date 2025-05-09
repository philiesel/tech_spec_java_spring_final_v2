package com.example.demo.service;

import com.example.demo.dto.SubscriptionDto;
import com.example.demo.dto.UserCreateDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserCreateDto createUser(UserDto newUser) {
        log.debug("Создание пользователя с именем {}", newUser.getUsername());
        UserEntity user = new UserEntity();
        user.setUsername(newUser.getUsername());
        UserEntity saveUser = userRepository.save(user);
        log.debug("Пользователь создан: {}", saveUser.getUsername());
        UserCreateDto userDto = new UserCreateDto();
        userDto.setUsername(saveUser.getUsername());
        userDto.setId(saveUser.getId());
        return userDto;
    }

    private UserEntity getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Пользователя с id " + userId + " нет в бд"));
    }

    public UserInfoDto getUserInfo(Long userId) {
        UserEntity user = getUser(userId);
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUsername(user.getUsername());
        Set<SubscriptionDto> subscriptions = user.getSubscriptions().stream()
                .map(subscriptionEntity -> new SubscriptionDto(subscriptionEntity.getName())).collect(Collectors.toSet());
        userInfoDto.setSubscriptions(subscriptions);
        log.debug("Информация о пользователе с id: {}", userId);
        return userInfoDto;
    }
    @Transactional
    public UserDto updateUser(Long userId, UserDto userUpdateDate) {
        UserEntity user = getUser(userId);
        user.setUsername(userUpdateDate.getUsername());
        UserEntity userWithUpdateDate = userRepository.save(user);
        log.info("Данные пользователя обновлены {}", userId);
        UserDto userDto = new UserDto();
        userDto.setUsername(userWithUpdateDate.getUsername());
        return userDto;
    }

    @Transactional
    public void deleteUser(Long userId) {
        UserEntity user = getUser(userId);
        userRepository.delete(user);
        log.info("Пользователь с {} удален", userId);
    }
}
