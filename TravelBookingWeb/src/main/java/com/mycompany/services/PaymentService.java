
package com.mycompany.services;

import com.mycompany.enums.PaymentStatus;
import com.mycompany.pojo.Payments;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface PaymentService {
    Payments addOrUpdatePayment(Payments payment);
    Payments createPayment(Map<String, String> params);
    Payments getPaymentByBookingId(Long bookingId);
    void updatePaymentStatus(Long paymentId, PaymentStatus status);
    BigDecimal sumRevenueByProvider(Long providerId, Date fromDate, Date toDate);
}
