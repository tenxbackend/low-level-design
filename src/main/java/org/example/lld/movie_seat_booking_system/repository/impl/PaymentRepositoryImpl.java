package org.example.lld.movie_seat_booking_system.repository.impl;

import org.example.lld.movie_seat_booking_system.model.Payment;
import org.example.lld.movie_seat_booking_system.model.User;
import org.example.lld.movie_seat_booking_system.repository.PaymentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PaymentRepositoryImpl implements PaymentRepository {
    private final Map<UUID, Payment> paymentMap = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
        if (payment.getPaymentId() == null) {
            payment.setPaymentId(UUID.randomUUID());
        }
        paymentMap.put(payment.getPaymentId(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(UUID paymentId) {
        return Optional.ofNullable(paymentMap.get(paymentId));
    }

    @Override
    public Optional<Payment> findByPaymentIntentId(UUID paymentIntentId) {
        return paymentMap.values().stream()
                .filter(payment -> paymentIntentId.equals(payment.getPaymentIntentId()))
                .findFirst();
    }

    @Override
    public List<Payment> findByUser(User user) {
        return paymentMap.values().stream()
                .filter(payment -> user.equals(payment.getUserId()))
                .collect(Collectors.toList());
    }
}
