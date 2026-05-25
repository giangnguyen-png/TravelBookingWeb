/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.repositories;

import com.mycompany.pojo.Flights;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyen
 */
public interface FlightRepository {
    List<Flights> searchFlights(Map<String, String> params);
    List<Flights> getFlightsByProviderId(Long providerId);
    List<Flights> getFlightsByIds(List<Long> ids);
    Flights getFlightById(Long id);
    Flights addOrUpdateFlight(Flights flight);
    void deleteFlight(Long id);
}
