/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services.impl;

import com.mycompany.enums.BookingStatus;
import com.mycompany.enums.BookingType;
import com.mycompany.enums.TransportType;
import com.mycompany.pojo.Bookings;
import com.mycompany.pojo.HotelBookings;
import com.mycompany.pojo.HotelRooms;
import com.mycompany.pojo.TourBookings;
import com.mycompany.pojo.Tours;
import com.mycompany.pojo.TransportBookings;
import com.mycompany.repositories.BusTripRepository;
import com.mycompany.repositories.BookingRepository;
import com.mycompany.repositories.FlightRepository;
import com.mycompany.repositories.HotelRoomRepository;
import com.mycompany.repositories.TourRepository;
import com.mycompany.repositories.UserRepository;
import com.mycompany.services.BookingService;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguyen
 */
@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private HotelRoomRepository roomRepo;
    @Autowired
    private TourRepository tourRepo;
    @Autowired
    private FlightRepository flightRepo;
    @Autowired
    private BusTripRepository busTripRepo;

    @Override
    public Bookings addOrUpdateBooking(Bookings booking) {
        return this.bookingRepo.addOrUpdateBooking(booking);
    }

    @Override
    public Bookings createBooking(Map<String, String> params) {
        BookingType bookingType = BookingType.valueOf(params.get("bookingType"));

        Bookings booking = new Bookings();
        booking.setBookingType(bookingType);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(new java.util.Date());
        booking.setCustomerId(this.userRepo.getUserById(Long.valueOf(params.get("customerId"))));

        switch (bookingType) {
            case HOTEL -> this.fillHotelBooking(booking, params);
            case TOUR -> this.fillTourBooking(booking, params);
            case TRANSPORT -> this.fillTransportBooking(booking, params);
        }

        return this.bookingRepo.addOrUpdateBooking(booking);
    }

    @Override
    public Bookings getBookingById(Long id) {
        return this.bookingRepo.getBookingById(id);
    }

    @Override
    public List<Bookings> getBookingsByCustomerId(Long customerId) {
        return this.bookingRepo.getBookingsByCustomerId(customerId);
    }

    @Override
    public List<Bookings> getBookingsByProviderId(Long providerId, Map<String, String> params) {
        return this.bookingRepo.getBookingsByProviderId(providerId, params);
    }

    @Override
    public void updateStatus(Long bookingId, BookingStatus status) {
        this.bookingRepo.updateStatus(bookingId, status);
    }

    private void fillHotelBooking(Bookings booking, Map<String, String> params) {
        Long roomId = Long.valueOf(params.get("roomId"));
        Date checkIn = Date.valueOf(params.get("checkInDate"));
        Date checkOut = Date.valueOf(params.get("checkOutDate"));
        int numberOfRooms = Integer.parseInt(params.get("numberOfRooms"));

        if (!this.roomRepo.hasAvailableRooms(roomId, checkIn, checkOut, numberOfRooms)) {
            throw new IllegalArgumentException("Room is not available");
        }

        HotelRooms room = this.roomRepo.getRoomById(roomId);
        long nights = Math.max(1, ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate()));
        booking.setTotalPrice(room.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights))
                .multiply(BigDecimal.valueOf(numberOfRooms)));

        HotelBookings hotelBooking = new HotelBookings();
        hotelBooking.setBookingId(booking);
        hotelBooking.setRoomId(room);
        hotelBooking.setCheckInDate(checkIn);
        hotelBooking.setCheckOutDate(checkOut);
        hotelBooking.setNumberOfRooms(numberOfRooms);
        booking.setHotelBookings(hotelBooking);
    }

    private void fillTourBooking(Bookings booking, Map<String, String> params) {
        Tours tour = this.tourRepo.getTourById(Long.valueOf(params.get("tourId")));
        int numberOfPeople = Integer.parseInt(params.get("numberOfPeople"));
        booking.setTotalPrice(tour.getPrice().multiply(BigDecimal.valueOf(numberOfPeople)));

        TourBookings tourBooking = new TourBookings();
        tourBooking.setBookingId(booking);
        tourBooking.setTourId(tour);
        tourBooking.setNumberOfPeople(numberOfPeople);
        booking.setTourBookings(tourBooking);
    }

    private void fillTransportBooking(Bookings booking, Map<String, String> params) {
        TransportType transportType = TransportType.valueOf(params.get("transportType"));
        Long serviceId = Long.valueOf(params.get("transportServiceId"));

        if (transportType == TransportType.FLIGHT) {
            booking.setTotalPrice(this.flightRepo.getFlightById(serviceId).getPrice());
        } else {
            booking.setTotalPrice(this.busTripRepo.getBusTripById(serviceId).getPrice());
        }

        TransportBookings transportBooking = new TransportBookings();
        transportBooking.setBookingId(booking);
        transportBooking.setTransportType(transportType);
        transportBooking.setTransportServiceId(serviceId);
        transportBooking.setSeatNumber(params.get("seatNumber"));
        booking.setTransportBookings(transportBooking);
    }
}
