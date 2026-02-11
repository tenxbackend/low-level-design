package org.example.lld.movie_seat_booking_system.repository;

import org.example.lld.movie_seat_booking_system.model.Booking;
import org.example.lld.movie_seat_booking_system.model.BookingStatus;
import org.example.lld.movie_seat_booking_system.model.User;
import org.example.lld.movie_seat_booking_system.model.Show;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID bookingId);
    List<Booking> findByUser(User user);
    List<Booking> findByShow(Show show);
    List<Booking> findByStatus(BookingStatus status);
    Optional<Booking> findByPaymentId(UUID paymentId);
}
