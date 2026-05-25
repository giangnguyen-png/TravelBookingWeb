/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services.impl;

import com.mycompany.pojo.Flights;
import com.mycompany.repositories.FlightRepository;
import com.mycompany.services.CloudinaryService;
import com.mycompany.services.FlightService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguyen
 */
@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepo;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<Flights> searchFlights(Map<String, String> params) {
        return this.flightRepo.searchFlights(params);
    }

    @Override
    public List<Flights> getFlightsByProviderId(Long providerId) {
        return this.flightRepo.getFlightsByProviderId(providerId);
    }

    @Override
    public List<Flights> getFlightsByIds(List<Long> ids) {
        return this.flightRepo.getFlightsByIds(ids);
    }

    @Override
    public Flights getFlightById(Long id) {
        return this.flightRepo.getFlightById(id);
    }

    @Override
    public Flights addOrUpdateFlight(Flights flight) {
        ServiceValidator.validateFlight(flight);
        String thumbnail = this.cloudinaryService.upload(flight.getThumbnailFile(), "travel/flights");
        if (thumbnail != null) {
            flight.setThumbnail(thumbnail);
        }
        return this.flightRepo.addOrUpdateFlight(flight);
    }

    @Override
    public void deleteFlight(Long id) {
        this.flightRepo.deleteFlight(id);
    }
}
