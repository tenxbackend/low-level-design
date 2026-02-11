package org.example.lld.movie_seat_booking_system.repository.impl;

import org.example.lld.movie_seat_booking_system.model.Seat;
import org.example.lld.movie_seat_booking_system.model.SeatStatus;
import org.example.lld.movie_seat_booking_system.model.Show;
import org.example.lld.movie_seat_booking_system.repository.SeatRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SeatRepositoryImpl implements SeatRepository {
    private final Map<UUID, Seat> seatMap = new ConcurrentHashMap<>();

    @Override
    public Seat save(Seat seat) {
        if (seat.getSeatId() == null) {
            seat.setSeatId(UUID.randomUUID());
        }
        seatMap.put(seat.getSeatId(), seat);
        return seat;
    }

    @Override
    public Optional<Seat> findById(UUID seatId) {
        return Optional.ofNullable(seatMap.get(seatId));
    }



    @Override
    public List<Seat> findBySeatNumbersAndShow(List<String> seatNumbers, Show show) {
        return seatMap.values().stream()
                .filter(seat -> show.equals(seat.getShow()) && seatNumbers.contains(seat.getSeatNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Seat> getSeats(List<UUID> seatIds) {
        return seatMap.values().stream()
                .filter(seat -> seatIds.contains(seat.getSeatId()))
                .collect(Collectors.toList());
    }

}
