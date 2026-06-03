
package com.mycompany.controllers;

import com.mycompany.enums.BookingStatus;
import com.mycompany.pojo.Bookings;
import com.mycompany.services.BookingService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiBookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/bookings")
    public ResponseEntity<Bookings> create(@RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.bookingService.createBooking(params), HttpStatus.CREATED);
    }

    @GetMapping("/bookings/me")
    public ResponseEntity<?> myBookings(@RequestParam(value = "customerId") Long customerId) {
        return new ResponseEntity<>(this.bookingService.getBookingsByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<Bookings> details(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.bookingService.getBookingById(id), HttpStatus.OK);
    }

    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable(value = "id") Long id, @RequestBody Map<String, String> params) {
        this.bookingService.updateStatus(id, BookingStatus.valueOf(params.get("status")));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/provider/bookings")
    public ResponseEntity<?> providerBookings(@RequestParam(value = "providerId") Long providerId, @RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.bookingService.getBookingsByProviderId(providerId, params), HttpStatus.OK);
    }
}
