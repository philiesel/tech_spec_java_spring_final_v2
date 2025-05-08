package com.example.demo.repository;

import com.example.demo.dto.SubscriptionDto;
import com.example.demo.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    Optional<SubscriptionEntity> findByName(String name);

    boolean existsByName(String name);

    @Query(nativeQuery = true, value = """
            SELECT id, name
            FROM
                (SELECT subscription_id, COUNT(user_id) as count_user
                FROM user_subscriptions
                GROUP BY subscription_id
                ORDER BY count_user DESC
                LIMIT 3) AS top
            JOIN subscriptions ON top.subscription_id = subscriptions.id
            """)
    List<SubscriptionEntity> top3subscription();
}