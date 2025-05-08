package com.example.demo.entity;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
@NoArgsConstructor
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionEntity)) return false;
        SubscriptionEntity subscription = (SubscriptionEntity) o;
        return Objects.equals(id, subscription.id);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
