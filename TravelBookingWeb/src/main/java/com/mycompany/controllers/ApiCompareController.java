/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.services.BusTripService;
import com.mycompany.services.FlightService;
import com.mycompany.services.HotelService;
import com.mycompany.services.TourService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyen
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCompareController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private TourService tourService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private BusTripService busTripService;

    @GetMapping("/compare")
    public ResponseEntity<?> compare(@RequestParam(value = "type") String type, @RequestParam(value = "ids") String ids) {
        List<Long> serviceIds = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .toList();

        Object data = switch (type.toUpperCase()) {
            case "HOTEL" -> this.hotelService.getHotelsByIds(serviceIds);
            case "TOUR" -> this.tourService.getToursByIds(serviceIds);
            case "FLIGHT" -> this.flightService.getFlightsByIds(serviceIds);
            case "BUS" -> this.busTripService.getBusTripsByIds(serviceIds);
            default -> throw new IllegalArgumentException("Invalid compare type");
        };

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
