
package com.mycompany.services.impl;

import com.mycompany.enums.RoomType;
import com.mycompany.pojo.HotelRooms;
import com.mycompany.repositories.HotelRepository;
import com.mycompany.repositories.HotelRoomRepository;
import com.mycompany.services.CloudinaryService;
import com.mycompany.services.HotelRoomService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelRoomServiceImpl implements HotelRoomService {

    @Autowired
    private HotelRoomRepository roomRepo;
    @Autowired
    private HotelRepository hotelRepo;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<HotelRooms> getRoomsByHotelId(Long hotelId) {
        return this.roomRepo.getRoomsByHotelId(hotelId);
    }

    @Override
    public HotelRooms getRoomById(Long id) {
        return this.roomRepo.getRoomById(id);
    }

    @Override
    public boolean hasAvailableRooms(Long roomId, Date checkInDate, Date checkOutDate, int numberOfRooms) {
        return this.roomRepo.hasAvailableRooms(roomId, checkInDate, checkOutDate, numberOfRooms);
    }

    @Override
    public HotelRooms addOrUpdateRoom(HotelRooms room) {
        ServiceValidator.validateRoom(room);
        String image = this.cloudinaryService.upload(room.getImageFile(), "travel/rooms");
        if (image != null) {
            room.setImage(image);
        }
        return this.roomRepo.addOrUpdateRoom(room);
    }

    @Override
    public HotelRooms addOrUpdateRoom(Long roomId, Map<String, String> params) {
        HotelRooms room = roomId != null ? this.roomRepo.getRoomById(roomId) : new HotelRooms();
        room.setRoomName(params.get("roomName"));
        room.setRoomType(RoomType.valueOf(params.get("roomType")));
        room.setPricePerNight(new BigDecimal(params.get("pricePerNight")));
        room.setAvailableRooms(Integer.parseInt(params.get("availableRooms")));
        room.setDescription(params.get("description"));
        room.setImage(params.get("image"));
        room.setHotelId(this.hotelRepo.getHotelById(Long.valueOf(params.get("hotelId"))));

        ServiceValidator.validateRoom(room);
        return this.roomRepo.addOrUpdateRoom(room);
    }

    @Override
    public void deleteRoom(Long id) {
        this.roomRepo.deleteRoom(id);
    }
}
