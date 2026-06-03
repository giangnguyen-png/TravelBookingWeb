
package com.mycompany.repositories.impl;

import com.mycompany.enums.PaymentStatus;
import com.mycompany.enums.TransportType;
import com.mycompany.pojo.Payments;
import com.mycompany.repositories.PaymentRepository;
import java.math.BigDecimal;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PaymentRepositoryImpl implements PaymentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Payments addOrUpdatePayment(Payments payment) {
        Session session = this.factory.getObject().getCurrentSession();
        if (payment.getId() == null) {
            session.persist(payment);
            return payment;
        }
        return session.merge(payment);
    }

    @Override
    public Payments getPaymentByBookingId(Long bookingId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Payments> query = session.createQuery(
                "FROM Payments p WHERE p.bookingId.id = :bookingId", Payments.class);
        query.setParameter("bookingId", bookingId);
        return query.uniqueResult();
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("UPDATE Payments p SET p.paymentStatus = :status WHERE p.id = :id");
        query.setParameter("status", status);
        query.setParameter("id", paymentId);
        query.executeUpdate();
    }

    @Override
    public BigDecimal sumRevenueByProvider(Long providerId, Date fromDate, Date toDate) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<BigDecimal> query = session.createQuery("""
                SELECT SUM(p.amount)
                FROM Payments p
                    JOIN p.bookingId b
                    LEFT JOIN b.hotelBookings hb
                    LEFT JOIN hb.roomId room
                    LEFT JOIN room.hotelId hotel
                    LEFT JOIN b.tourBookings tbk
                    LEFT JOIN tbk.tourId tour
                    LEFT JOIN b.transportBookings trb
                WHERE p.paymentStatus = :paidStatus
                    AND (:fromDate IS NULL OR p.paidAt >= :fromDate)
                    AND (:toDate IS NULL OR p.paidAt <= :toDate)
                    AND (
                        hotel.providerId.id = :providerId
                        OR tour.providerId.id = :providerId
                        OR EXISTS (
                            SELECT 1 FROM Flights f
                            WHERE trb.transportType = :flightType
                                AND trb.transportServiceId = f.id
                                AND f.providerId.id = :providerId
                        )
                        OR EXISTS (
                            SELECT 1 FROM BusTrips bt
                            WHERE trb.transportType = :busType
                                AND trb.transportServiceId = bt.id
                                AND bt.providerId.id = :providerId
                        )
                    )
                """, BigDecimal.class);
        query.setParameter("providerId", providerId);
        query.setParameter("paidStatus", PaymentStatus.PAID);
        query.setParameter("flightType", TransportType.FLIGHT);
        query.setParameter("busType", TransportType.BUS);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        BigDecimal revenue = query.uniqueResult();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
}
