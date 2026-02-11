package org.example.lld.movie_seat_booking_system.model;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@ToString
@Data
public class Seat {
    private UUID seatId;
    private String seatNumber;
    private Show show;
    private SeatStatus status;
    private User bookedBy;

    public Seat() {
    }

    public Seat(UUID seatId, String seatNumber, Show show) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.show = show;
        this.status = SeatStatus.AVAILABLE;
    }

}
