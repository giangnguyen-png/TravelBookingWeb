/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.enums.PaymentStatus;
import com.mycompany.pojo.Payments;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author nguyen
 */
public interface PaymentService {
    Payments addOrUpdatePayment(Payments payment);
    Payments createPayment(Map<String, String> params);
    Payments getPaymentByBookingId(Long bookingId);
    void updatePaymentStatus(Long paymentId, PaymentStatus status);
    BigDecimal sumRevenueByProvider(Long providerId, Date fromDate, Date toDate);
}
