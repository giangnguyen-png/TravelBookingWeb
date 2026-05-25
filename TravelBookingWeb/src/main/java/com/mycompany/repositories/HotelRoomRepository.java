/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.repositories;

import com.mycompany.pojo.HotelRooms;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nguyen
 */
public interface HotelRoomRepository {
    List<HotelRooms> getRoomsByHotelId(Long hotelId);
    HotelRooms getRoomById(Long id);
    boolean hasAvailableRooms(Long roomId, Date checkInDate, Date checkOutDate, int numberOfRooms);
    HotelRooms addOrUpdateRoom(HotelRooms room);
    void deleteRoom(Long id);
}
