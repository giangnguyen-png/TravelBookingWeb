/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.pojo.Hotels;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyen
 */
public interface HotelService {
    List<Hotels> searchHotels(Map<String, String> params);
    List<Hotels> getHotelsByProviderId(Long providerId);
    List<Hotels> getHotelsByIds(List<Long> ids);
    Hotels getHotelById(Long id);
    Hotels addOrUpdateHotel(Hotels hotel);
    void deleteHotel(Long id);
}
