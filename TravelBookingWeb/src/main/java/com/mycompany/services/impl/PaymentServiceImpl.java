/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services.impl;

import com.mycompany.enums.BookingStatus;
import com.mycompany.enums.PaymentMethod;
import com.mycompany.enums.PaymentStatus;
import com.mycompany.pojo.Bookings;
import com.mycompany.pojo.Payments;
import com.mycompany.repositories.BookingRepository;
import com.mycompany.repositories.PaymentRepository;
import com.mycompany.services.PaymentService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguyen
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private BookingRepository bookingRepo;

    @Override
    public Payments addOrUpdatePayment(Payments payment) {
        return this.paymentRepo.addOrUpdatePayment(payment);
    }

    @Override
    public Payments createPayment(Map<String, String> params) {
        Bookings booking = this.bookingRepo.getBookingById(Long.valueOf(params.get("bookingId")));

        Payments payment = new Payments();
        payment.setBookingId(booking);
        payment.setPaymentMethod(PaymentMethod.valueOf(params.get("paymentMethod")));
        payment.setPaymentStatus(PaymentStatus.valueOf(params.getOrDefault("paymentStatus", PaymentStatus.PENDING.name())));
        payment.setAmount(booking.getTotalPrice());

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            payment.setPaidAt(new Date());
            this.bookingRepo.updateStatus(booking.getId(), BookingStatus.PAID);
        }

        return this.paymentRepo.addOrUpdatePayment(payment);
    }

    @Override
    public Payments getPaymentByBookingId(Long bookingId) {
        return this.paymentRepo.getPaymentByBookingId(bookingId);
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        this.paymentRepo.updatePaymentStatus(paymentId, status);
    }

    @Override
    public BigDecimal sumRevenueByProvider(Long providerId, Date fromDate, Date toDate) {
        return this.paymentRepo.sumRevenueByProvider(providerId, fromDate, toDate);
    }
}
