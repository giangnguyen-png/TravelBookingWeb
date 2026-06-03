
package com.mycompany.repositories;

import com.mycompany.enums.PaymentStatus;
import com.mycompany.pojo.Payments;
import java.math.BigDecimal;
import java.util.Date;

public interface PaymentRepository {
    Payments addOrUpdatePayment(Payments payment);
    Payments getPaymentByBookingId(Long bookingId);
    void updatePaymentStatus(Long paymentId, PaymentStatus status);
    BigDecimal sumRevenueByProvider(Long providerId, Date fromDate, Date toDate);
}
