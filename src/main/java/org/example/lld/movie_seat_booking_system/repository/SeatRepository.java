package org.example.lld.movie_seat_booking_system.repository;

import org.example.lld.movie_seat_booking_system.model.Seat;
import org.example.lld.movie_seat_booking_system.model.Show;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface SeatRepository {
    Seat save(Seat seat);
    Optional<Seat> findById(UUID seatId);
    List<Seat> findBySeatNumbersAndShow(List<String> seatNumbers, Show show);
    List<Seat> getSeats(List<UUID> seatIds);
}
