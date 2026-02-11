package org.example.lld.movie_seat_booking_system.repository;

import org.example.lld.movie_seat_booking_system.model.Show;
import java.util.UUID;
import java.util.Optional;

public interface ShowRepository {
    Show save(Show show);
    Optional<Show> findById(UUID showId);
}
