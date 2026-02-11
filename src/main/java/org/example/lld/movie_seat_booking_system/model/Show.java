package org.example.lld.movie_seat_booking_system.model;

import lombok.ToString;

import java.util.List;
import java.util.UUID;

@ToString
public class Show {
    private UUID showId;
    private String showName;

    public Show() {}

    public Show(UUID showId, String showName) {
        this.showId = showId;
        this.showName = showName;
    }

    public UUID getShowId() {
        return showId;
    }

    public void setShowId(UUID showId) {
        this.showId = showId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }
}
