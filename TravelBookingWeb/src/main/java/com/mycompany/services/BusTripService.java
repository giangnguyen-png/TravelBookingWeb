/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.pojo.BusTrips;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyen
 */
public interface BusTripService {
    List<BusTrips> searchBusTrips(Map<String, String> params);
    List<BusTrips> getBusTripsByProviderId(Long providerId);
    List<BusTrips> getBusTripsByIds(List<Long> ids);
    BusTrips getBusTripById(Long id);
    BusTrips addOrUpdateBusTrip(BusTrips busTrip);
    void deleteBusTrip(Long id);
}
