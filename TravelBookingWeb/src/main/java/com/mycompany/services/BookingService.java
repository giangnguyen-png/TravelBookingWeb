
package com.mycompany.services;

import com.mycompany.enums.BookingStatus;
import com.mycompany.pojo.Bookings;
import java.util.List;
import java.util.Map;

public interface BookingService {
    Bookings addOrUpdateBooking(Bookings booking);
    Bookings createBooking(Map<String, String> params);
    Bookings getBookingById(Long id);
    List<Bookings> getBookingsByCustomerId(Long customerId);
    List<Bookings> getBookingsByProviderId(Long providerId, Map<String, String> params);
    void updateStatus(Long bookingId, BookingStatus status);
}
