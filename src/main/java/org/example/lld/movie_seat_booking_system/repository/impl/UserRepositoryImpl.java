package org.example.lld.movie_seat_booking_system.repository.impl;

import org.example.lld.movie_seat_booking_system.model.User;
import org.example.lld.movie_seat_booking_system.repository.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {
    private final Map<UUID, User> userMap = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        if (user.getUserId() == null) {
            user.setUserId(UUID.randomUUID());
        }
        userMap.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

}
