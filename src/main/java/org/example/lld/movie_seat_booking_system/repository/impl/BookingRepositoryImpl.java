package org.example.lld.movie_seat_booking_system.repository.impl;

import org.example.lld.movie_seat_booking_system.model.Booking;
import org.example.lld.movie_seat_booking_system.model.BookingStatus;
import org.example.lld.movie_seat_booking_system.model.User;
import org.example.lld.movie_seat_booking_system.model.Show;
import org.example.lld.movie_seat_booking_system.repository.BookingRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BookingRepositoryImpl implements BookingRepository {
    private final Map<UUID, Booking> bookingMap = new ConcurrentHashMap<>();

    @Override
    public Booking save(Booking booking) {
        if (booking.getBookingId() == null) {
            booking.setBookingId(UUID.randomUUID());
        }
        bookingMap.put(booking.getBookingId(), booking);
        return booking;
    }

    @Override
    public Optional<Booking> findById(UUID bookingId) {
        return Optional.ofNullable(bookingMap.get(bookingId));
    }

    @Override
    public List<Booking> findByUser(User user) {
        return bookingMap.values().stream()
                .filter(booking -> user.equals(booking.getUser()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByShow(Show show) {
        return bookingMap.values().stream()
                .filter(booking -> show.equals(booking.getShow()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        return bookingMap.values().stream()
                .filter(booking -> status.equals(booking.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Booking> findByPaymentId(UUID paymentId) {
        return bookingMap.values().stream()
                .filter(booking -> paymentId.equals(booking.getPaymentId()))
                .findFirst();
    }
}
