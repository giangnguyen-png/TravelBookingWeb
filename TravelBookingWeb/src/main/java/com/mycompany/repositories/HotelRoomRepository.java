
package com.mycompany.repositories;

import com.mycompany.pojo.HotelRooms;
import java.util.Date;
import java.util.List;

public interface HotelRoomRepository {
    List<HotelRooms> getRoomsByHotelId(Long hotelId);
    HotelRooms getRoomById(Long id);
    boolean hasAvailableRooms(Long roomId, Date checkInDate, Date checkOutDate, int numberOfRooms);
    HotelRooms addOrUpdateRoom(HotelRooms room);
    void deleteRoom(Long id);
}
