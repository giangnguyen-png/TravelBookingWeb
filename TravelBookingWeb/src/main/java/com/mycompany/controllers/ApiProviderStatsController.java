/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.services.BookingService;
import com.mycompany.services.PaymentService;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyen
 */
@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ApiProviderStatsController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/statistics")
    public ResponseEntity<?> statistics(@RequestParam(value = "providerId") Long providerId,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        Date from = fromDate != null && !fromDate.isBlank() ? Date.valueOf(fromDate) : null;
        Date to = toDate != null && !toDate.isBlank() ? Date.valueOf(toDate) : null;

        Map<String, Object> data = new HashMap<>();
        data.put("revenue", this.paymentService.sumRevenueByProvider(providerId, from, to));
        data.put("bookings", this.bookingService.getBookingsByProviderId(providerId, Map.of()).size());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
