package org.example.lld.movie_seat_booking_system.model;


import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@ToString
public class Booking {
    private UUID bookingId;
    private Show show;
    private User user;
    private BookingStatus status;
    private UUID paymentId;
    private List<Seat> seats;

    public Booking() {}

    public Booking(UUID bookingId, Show show, User user,List<Seat> seats) {
        this.bookingId = bookingId;
        this.show = show;
        this.user = user;
        this.status = BookingStatus.PENDING;
        this.seats = seats;
    }

}
