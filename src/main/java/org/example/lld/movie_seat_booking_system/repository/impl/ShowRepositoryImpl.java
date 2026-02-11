package org.example.lld.movie_seat_booking_system.repository.impl;

import org.example.lld.movie_seat_booking_system.model.Show;
import org.example.lld.movie_seat_booking_system.repository.ShowRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShowRepositoryImpl implements ShowRepository {
    private final Map<UUID, Show> showMap = new ConcurrentHashMap<>();

    @Override
    public Show save(Show show) {
        if (show.getShowId() == null) {
            show.setShowId(UUID.randomUUID());
        }
        showMap.put(show.getShowId(), show);
        return show;
    }

    @Override
    public Optional<Show> findById(UUID showId) {
        return Optional.ofNullable(showMap.get(showId));
    }

}
