package org.example.lld.movie_seat_booking_system.repository;

import org.example.lld.movie_seat_booking_system.model.Payment;
import org.example.lld.movie_seat_booking_system.model.User;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID paymentId);
    Optional<Payment> findByPaymentIntentId(UUID paymentIntentId);
    List<Payment> findByUser(User user);
}
