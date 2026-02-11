package org.example.lld.movie_seat_booking_system.model;

public enum PaymentStatus{
    PENDING,
    SUCCESS,
    FAILED;
    
    
    public static PaymentStatus get(String status){
        switch (status){
            case "SUCCESS" -> {
                return PaymentStatus.SUCCESS;
            }
            case "FAILED" -> {
                return PaymentStatus.FAILED;
            }
            default -> {
                return PaymentStatus.PENDING;
            }
        }
    }
}
