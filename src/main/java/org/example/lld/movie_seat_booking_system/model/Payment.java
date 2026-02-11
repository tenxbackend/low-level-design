package org.example.lld.movie_seat_booking_system.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Payment {
    private UUID paymentId;
    private UUID paymentIntentId;
    private UUID userId;
    private double amount;
    private UUID bookingId;
    private PaymentStatus status;

    public Payment() {}

    public Payment(UUID paymentId, UUID paymentIntentId, UUID userId, double amount, UUID bookingId ) {
        this.paymentId = paymentId;
        this.paymentIntentId = paymentIntentId;
        this.userId = userId;
        this.amount = amount;
        this.bookingId = bookingId;
        this.status = PaymentStatus.PENDING;
    }
}
