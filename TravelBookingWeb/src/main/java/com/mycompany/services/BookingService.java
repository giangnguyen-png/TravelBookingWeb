/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.enums.BookingStatus;
import com.mycompany.pojo.Bookings;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyen
 */
public interface BookingService {
    Bookings addOrUpdateBooking(Bookings booking);
    Bookings createBooking(Map<String, String> params);
    Bookings getBookingById(Long id);
    List<Bookings> getBookingsByCustomerId(Long customerId);
    List<Bookings> getBookingsByProviderId(Long providerId, Map<String, String> params);
    void updateStatus(Long bookingId, BookingStatus status);
}
