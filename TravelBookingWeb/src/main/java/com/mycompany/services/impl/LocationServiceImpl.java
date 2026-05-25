/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services.impl;

import com.mycompany.pojo.Locations;
import com.mycompany.repositories.LocationRepository;
import com.mycompany.services.LocationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguyen
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepo;

    @Override
    public List<Locations> getLocations() {
        return this.locationRepo.getLocations();
    }

    @Override
    public List<Locations> searchLocations(String keyword) {
        return this.locationRepo.searchLocations(keyword);
    }

    @Override
    public Locations getLocationById(Long id) {
        return this.locationRepo.getLocationById(id);
    }
}
