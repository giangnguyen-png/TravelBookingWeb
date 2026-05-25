/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services.impl;

import com.mycompany.pojo.BusTrips;
import com.mycompany.repositories.BusTripRepository;
import com.mycompany.services.BusTripService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguyen
 */
@Service
public class BusTripServiceImpl implements BusTripService {

    @Autowired
    private BusTripRepository busTripRepo;

    @Override
    public List<BusTrips> searchBusTrips(Map<String, String> params) {
        return this.busTripRepo.searchBusTrips(params);
    }

    @Override
    public List<BusTrips> getBusTripsByProviderId(Long providerId) {
        return this.busTripRepo.getBusTripsByProviderId(providerId);
    }

    @Override
    public List<BusTrips> getBusTripsByIds(List<Long> ids) {
        return this.busTripRepo.getBusTripsByIds(ids);
    }

    @Override
    public BusTrips getBusTripById(Long id) {
        return this.busTripRepo.getBusTripById(id);
    }

    @Override
    public BusTrips addOrUpdateBusTrip(BusTrips busTrip) {
        ServiceValidator.validateBusTrip(busTrip);
        return this.busTripRepo.addOrUpdateBusTrip(busTrip);
    }

    @Override
    public void deleteBusTrip(Long id) {
        this.busTripRepo.deleteBusTrip(id);
    }
}
