/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.pojo.HotelRooms;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyen
 */
public interface HotelRoomService {
    List<HotelRooms> getRoomsByHotelId(Long hotelId);
    HotelRooms getRoomById(Long id);
    boolean hasAvailableRooms(Long roomId, Date checkInDate, Date checkOutDate, int numberOfRooms);
    HotelRooms addOrUpdateRoom(HotelRooms room);
    HotelRooms addOrUpdateRoom(Long roomId, Map<String, String> params);
    void deleteRoom(Long id);
}
