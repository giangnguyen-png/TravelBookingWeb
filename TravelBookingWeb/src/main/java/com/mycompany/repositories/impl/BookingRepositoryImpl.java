/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repositories.impl;

import com.mycompany.enums.BookingStatus;
import com.mycompany.enums.TransportType;
import com.mycompany.pojo.Bookings;
import com.mycompany.repositories.BookingRepository;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyen
 */
@Repository
@Transactional
public class BookingRepositoryImpl implements BookingRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Bookings addOrUpdateBooking(Bookings booking) {
        Session session = this.factory.getObject().getCurrentSession();
        if (booking.getId() == null) {
            session.persist(booking);
            return booking;
        }
        return session.merge(booking);
    }

    @Override
    public Bookings getBookingById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Bookings.class, id);
    }

    @Override
    public List<Bookings> getBookingsByCustomerId(Long customerId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Bookings> query = session.createQuery(
                "FROM Bookings b WHERE b.customerId.id = :customerId ORDER BY b.createdAt DESC", Bookings.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    public List<Bookings> getBookingsByProviderId(Long providerId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = """
                SELECT DISTINCT b
                FROM Bookings b
                    LEFT JOIN b.hotelBookings hb
                    LEFT JOIN hb.roomId room
                    LEFT JOIN room.hotelId hotel
                    LEFT JOIN b.tourBookings tbk
                    LEFT JOIN tbk.tourId tour
                    LEFT JOIN b.transportBookings trb
                WHERE hotel.providerId.id = :providerId
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
                ORDER BY b.createdAt DESC
                """;

        Query<Bookings> query = session.createQuery(hql, Bookings.class);
        query.setParameter("providerId", providerId);
        query.setParameter("flightType", TransportType.FLIGHT);
        query.setParameter("busType", TransportType.BUS);
        RepositoryUtils.paginate(query, params);
        return query.getResultList();
    }

    @Override
    public void updateStatus(Long bookingId, BookingStatus status) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("UPDATE Bookings b SET b.status = :status WHERE b.id = :id");
        query.setParameter("status", status);
        query.setParameter("id", bookingId);
        query.executeUpdate();
    }
}
