
package com.mycompany.repositories.impl;

import com.mycompany.pojo.HotelRooms;
import com.mycompany.repositories.HotelRoomRepository;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HotelRoomRepositoryImpl implements HotelRoomRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<HotelRooms> getRoomsByHotelId(Long hotelId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<HotelRooms> query = session.createQuery(
                "FROM HotelRooms r WHERE r.hotelId.id = :hotelId ORDER BY r.pricePerNight", HotelRooms.class);
        query.setParameter("hotelId", hotelId);
        return query.getResultList();
    }

    @Override
    public HotelRooms getRoomById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(HotelRooms.class, id);
    }

    @Override
    public boolean hasAvailableRooms(Long roomId, Date checkInDate, Date checkOutDate, int numberOfRooms) {
        Session session = this.factory.getObject().getCurrentSession();
        HotelRooms room = this.getRoomById(roomId);
        if (room == null || room.getAvailableRooms() < numberOfRooms) {
            return false;
        }

        Query<Long> query = session.createQuery("""
                SELECT COALESCE(SUM(hb.numberOfRooms), 0)
                FROM HotelBookings hb
                JOIN hb.bookingId b
                WHERE hb.roomId.id = :roomId
                    AND b.status <> com.mycompany.enums.BookingStatus.CANCELLED
                    AND hb.checkInDate < :checkOutDate
                    AND hb.checkOutDate > :checkInDate
                """, Long.class);
        query.setParameter("roomId", roomId);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        Long bookedRooms = query.uniqueResult();
        return room.getAvailableRooms() - bookedRooms >= numberOfRooms;
    }

    @Override
    public HotelRooms addOrUpdateRoom(HotelRooms room) {
        Session session = this.factory.getObject().getCurrentSession();
        if (room.getId() == null) {
            session.persist(room);
            return room;
        }
        return session.merge(room);
    }

    @Override
    public void deleteRoom(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        HotelRooms room = this.getRoomById(id);
        if (room != null) {
            session.remove(room);
        }
    }
}
