
package com.mycompany.controllers;

import com.mycompany.enums.BookingStatus;
import com.mycompany.pojo.Bookings;
import com.mycompany.services.BookingService;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ApiProviderStatsController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/statistics")
    public ResponseEntity<?> statistics(@RequestParam(value = "providerId") Long providerId,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        Date from = fromDate != null && !fromDate.isBlank() ? Date.valueOf(fromDate) : null;
        Date to = toDate != null && !toDate.isBlank() ? Date.valueOf(toDate) : null;

        Map<String, Object> data = new HashMap<>();
        List<Bookings> bookings = this.bookingService.getBookingsByProviderId(providerId, Map.of());
        BigDecimal revenue = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.PAID || b.getStatus() == BookingStatus.COMPLETED)
                .filter(b -> b.getCreatedAt() != null)
                .filter(b -> from == null || !b.getCreatedAt().before(from))
                .filter(b -> to == null || !b.getCreatedAt().after(to))
                .map(Bookings::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        data.put("revenue", revenue);
        data.put("bookings", bookings.size());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
