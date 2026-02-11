package org.example.lld.movie_seat_booking_system;


import org.example.lld.movie_seat_booking_system.model.*;
import org.example.lld.movie_seat_booking_system.repository.*;
import org.example.lld.movie_seat_booking_system.repository.impl.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


record SeatBookingResult(UUID bookingId, UUID paymentIntentId) {
}

interface SeatBookingService {
    SeatBookingResult bookSeats(UUID showId, List<UUID> seatIds, UUID userId) throws Exception;
    // validations - seat status, show should exist , etc
    // create pending booking
    // initiate a payment [ paymentIntentId, BookingId ]

    Booking confirmBooking(UUID bookingId, UUID paymentId);
    // update the booking status to success
    // mark the seats from available to booked

    void markBookingFailed(UUID bookingId, Optional<UUID> paymentId);
    // update the booking status to failed
}


class SeatBookingServiceImpl implements SeatBookingService {
    private ShowRepository showRepository;
    private SeatRepository seatRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;

    public SeatBookingServiceImpl(ShowRepository showRepository, SeatRepository seatRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SeatBookingResult bookSeats(UUID showId, List<UUID> seatIds, UUID userId) throws Exception {
        Optional<Show> show = showRepository.findById(showId);
        if (show.isEmpty()) {
            throw new Exception("Show does not exist");
        }

        List<Seat> seats = seatRepository.getSeats(seatIds);
        synchronized (this) {
            for (Seat seat : seats) {
                if (seat.getStatus() != SeatStatus.AVAILABLE) {
                    throw new Exception("Seat " + seat.getSeatNumber() + " is not available ");
                }
            }

            // request or thread will update the available seats to LOCKED
            for (Seat seat : seats) {
                seat.setStatus(SeatStatus.LOCKED);
                seatRepository.save(seat);
            }
        }

        UUID bookingId = UUID.randomUUID();
        Optional<User> user = userRepository.findById(userId);

        // create a pending booking
        Booking booking = new Booking(bookingId, show.get(), user.get(), seats);
        bookingRepository.save(booking);



        // create payment intentId and return
        UUID paymentIntentId = UUID.randomUUID();
        SeatBookingResult seatBookingResult = new SeatBookingResult(bookingId, paymentIntentId);

        return seatBookingResult;
    }

    @Override
    public Booking confirmBooking(UUID bookingId, UUID paymentId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        List<Seat> seats = booking.getSeats();

        // update seat status to booked
        for(Seat seat : seats){
            seat.setStatus(SeatStatus.BOOKED);
            seat.setBookedBy(booking.getUser());
            seatRepository.save(seat);
        }

        // update the booking status also
        booking.setStatus(BookingStatus.SUCCESS);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public void markBookingFailed(UUID bookingId, Optional<UUID> paymentId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        // update the booking status also
        booking.setStatus(BookingStatus.FAILED);
        bookingRepository.save(booking);
    }
}


interface PaymentService {
    void initiatePayment(UUID paymentIntentId, UUID bookingId);
    // payment object create [ mimic -calling gateway ]

    void paymentCallback(UUID paymentIntentId, String status);
    // success or failed
    // success -> confirm booking
    // failed -> fail booking

}


class PaymentServiceImpl implements PaymentService {
    private PaymentRepository paymentRepository;
    private BookingRepository bookingRepository;
    private SeatBookingService seatBookingService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository, SeatBookingService seatBookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.seatBookingService = seatBookingService;
    }


    @Override
    public void initiatePayment(UUID paymentIntentId, UUID bookingId) {
        UUID paymentId = UUID.randomUUID();
        Booking booking = bookingRepository.findById(bookingId).get();
        UUID userId = booking.getUser().getUserId();
        Payment payment = new Payment(paymentId,paymentIntentId,userId,100, bookingId);
        booking.setPaymentId(paymentId);
        bookingRepository.save(booking);
        paymentRepository.save(payment);

        // we are mocking call to gateway and wait for its callback
    }



    @Override
    public void paymentCallback(UUID paymentIntentId, String status) {
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId).get();
        payment.setStatus(PaymentStatus.get(status));
        paymentRepository.save(payment);

        // if success -> confirm booking
        // if failed -> fail booking
        // instead of event based
        // we are directly calling from here
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            seatBookingService.confirmBooking(payment.getBookingId(), payment.getPaymentId());
        } else {
            seatBookingService.markBookingFailed(payment.getBookingId(), Optional.of(payment.getPaymentId()));
        }


    }
}


public class BookMyCinemaApplication {


    public static void main(String[] args) throws InterruptedException {

        UserRepository userRepository = new UserRepositoryImpl();
        ShowRepository showRepository = new ShowRepositoryImpl();
        SeatRepository seatRepository = new SeatRepositoryImpl();
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        PaymentRepository paymentRepository = new PaymentRepositoryImpl();

        SeatBookingService seatBookingService = new SeatBookingServiceImpl(showRepository, seatRepository, bookingRepository, userRepository);
        PaymentService paymentService = new PaymentServiceImpl(paymentRepository, bookingRepository, seatBookingService);

        // create users
        User user1 = new User(UUID.randomUUID(), "user-1", "test@email");
        User user2 = new User(UUID.randomUUID(), "user-2", "test2@email");

        userRepository.save(user1);
        userRepository.save(user2);

        // create show
        Show show = new Show(UUID.randomUUID(), "Intersteller");
        showRepository.save(show);

        // create seats

        for(int i=1;i<=10;i++){
            Seat seat = new Seat(UUID.randomUUID(), "A" + i, show);
            seatRepository.save(seat);
        }

        List<UUID> seatIds = seatRepository.findBySeatNumbersAndShow(List.of("A1", "A2"), show).stream().map(seat -> seat.getSeatId()).collect(Collectors.toList());

        // below is when request are coming sequentially
//        try{
//            SeatBookingResult seatBookingResult = seatBookingService.bookSeats(show.getShowId(), seatIds, user1.getUserId());
//            // initiate payment
//            paymentService.initiatePayment(seatBookingResult.paymentIntentId(), seatBookingResult.bookingId());
//
//            // mock callback from gateway
//
//            paymentService.paymentCallback(seatBookingResult.paymentIntentId(), "SUCCESS");
//
//            // check seats
//            List<Seat> bookedSeats = seatRepository.findBySeatNumbersAndShow(List.of("A1", "A2"), show);
//            System.out.println("Seat booked checked " + bookedSeats);
//
//            // check bookings
//            Booking booking = bookingRepository.findById(seatBookingResult.bookingId()).get();
//            System.out.println("Booking checked " + booking);
//
//            // check payments
//            Payment payment = paymentRepository.findById(seatBookingResult.paymentIntentId()).get();
//            System.out.println("Payment checked " + payment);
//
//        }
//        catch (Exception e){
//
//        }

        // mimic cuncurrent requests

        Thread thread1 = new Thread(() -> {
            try{
             SeatBookingResult seatBookingResult =  seatBookingService.bookSeats(show.getShowId(), seatIds, user1.getUserId());
             paymentService.initiatePayment(seatBookingResult.paymentIntentId(), seatBookingResult.bookingId());
             paymentService.paymentCallback(seatBookingResult.paymentIntentId(), "SUCCESS");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try{
                SeatBookingResult seatBookingResult =  seatBookingService.bookSeats(show.getShowId(), seatIds, user2.getUserId());
                paymentService.initiatePayment(seatBookingResult.paymentIntentId(), seatBookingResult.bookingId());
                paymentService.paymentCallback(seatBookingResult.paymentIntentId(), "SUCCESS");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        // check double booking
        List<Booking> bookings = bookingRepository.findByShow(show);
        System.out.println("Bookings " + bookings.size());
        for(Booking booking : bookings){
            System.out.println("Booking " + booking);
        }


    }
}


// user [ userId, name]
// show [ showId, showName]
// seat [ seatId, seatNumber, showId, status - available, booked , userId [ bookedBy ]
// booking [ bookingId, showId, list<seat> , status - pending, pending_payment, success, failed]
// payment [ paymentId, paymentIntentId, userId, amount, bookingId]