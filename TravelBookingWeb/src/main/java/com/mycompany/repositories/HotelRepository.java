
package com.mycompany.repositories;

import com.mycompany.pojo.Hotels;
import java.util.List;
import java.util.Map;

public interface HotelRepository {
    List<Hotels> searchHotels(Map<String, String> params);
    List<Hotels> getHotelsByProviderId(Long providerId);
    List<Hotels> getHotelsByIds(List<Long> ids);
    Hotels getHotelById(Long id);
    Hotels addOrUpdateHotel(Hotels hotel);
    void deleteHotel(Long id);
}
