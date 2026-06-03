
package com.mycompany.services.impl;

import com.mycompany.pojo.Hotels;
import com.mycompany.repositories.HotelRepository;
import com.mycompany.services.CloudinaryService;
import com.mycompany.services.HotelService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepo;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<Hotels> searchHotels(Map<String, String> params) {
        return this.hotelRepo.searchHotels(params);
    }

    @Override
    public List<Hotels> getHotelsByProviderId(Long providerId) {
        return this.hotelRepo.getHotelsByProviderId(providerId);
    }

    @Override
    public List<Hotels> getHotelsByIds(List<Long> ids) {
        return this.hotelRepo.getHotelsByIds(ids);
    }

    @Override
    public Hotels getHotelById(Long id) {
        return this.hotelRepo.getHotelById(id);
    }

    @Override
    public Hotels addOrUpdateHotel(Hotels hotel) {
        if (hotel.getCreatedAt() == null) {
            hotel.setCreatedAt(new java.util.Date());
        }
        ServiceValidator.validateHotel(hotel);
        String thumbnail = this.cloudinaryService.upload(hotel.getThumbnailFile(), "travel/hotels");
        if (thumbnail != null) {
            hotel.setThumbnail(thumbnail);
        }
        return this.hotelRepo.addOrUpdateHotel(hotel);
    }

    @Override
    public void deleteHotel(Long id) {
        this.hotelRepo.deleteHotel(id);
    }
}
