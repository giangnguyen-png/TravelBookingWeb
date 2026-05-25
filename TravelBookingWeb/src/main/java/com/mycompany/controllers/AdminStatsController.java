/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.services.BookingService;
import com.mycompany.services.PaymentService;
import java.sql.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nguyen
 */
@Controller
@RequestMapping("/admin/statistics")
public class AdminStatsController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/provider")
    public String providerStats(Model model,
            @RequestParam(value = "providerId") Long providerId,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        Date from = fromDate != null && !fromDate.isBlank() ? Date.valueOf(fromDate) : null;
        Date to = toDate != null && !toDate.isBlank() ? Date.valueOf(toDate) : null;

        model.addAttribute("providerId", providerId);
        model.addAttribute("revenue", this.paymentService.sumRevenueByProvider(providerId, from, to));
        model.addAttribute("bookings", this.bookingService.getBookingsByProviderId(providerId, Map.of()));
        return "provider-stats";
    }
}
