
package com.mycompany.controllers;

import com.mycompany.pojo.Payments;
import com.mycompany.services.PaymentService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiPaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<Payments> create(@RequestBody Map<String, String> params) {
        return new ResponseEntity<>(this.paymentService.createPayment(params), HttpStatus.CREATED);
    }

    @GetMapping("/payments/booking/{bookingId}")
    public ResponseEntity<Payments> getByBooking(@PathVariable(value = "bookingId") Long bookingId) {
        return new ResponseEntity<>(this.paymentService.getPaymentByBookingId(bookingId), HttpStatus.OK);
    }
}
