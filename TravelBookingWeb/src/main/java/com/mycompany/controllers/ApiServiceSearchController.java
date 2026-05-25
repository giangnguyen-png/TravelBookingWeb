/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.pojo.BusTrips;
import com.mycompany.pojo.Flights;
import com.mycompany.pojo.HotelRooms;
import com.mycompany.pojo.Hotels;
import com.mycompany.pojo.Tours;
import com.mycompany.services.BusTripService;
import com.mycompany.services.FlightService;
import com.mycompany.services.HotelRoomService;
import com.mycompany.services.HotelService;
import com.mycompany.services.TourService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class ApiServiceSearchController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelRoomService roomService;
    @Autowired
    private TourService tourService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private BusTripService busTripService;

    @GetMapping("/hotels")
    public ResponseEntity<List<Hotels>> hotels(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.hotelService.searchHotels(params), HttpStatus.OK);
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<Hotels> hotelDetails(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.hotelService.getHotelById(id), HttpStatus.OK);
    }

    @GetMapping("/hotels/{id}/rooms")
    public ResponseEntity<List<HotelRooms>> hotelRooms(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.roomService.getRoomsByHotelId(id), HttpStatus.OK);
    }

    @GetMapping("/tours")
    public ResponseEntity<List<Tours>> tours(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.tourService.searchTours(params), HttpStatus.OK);
    }

    @GetMapping("/tours/{id}")
    public ResponseEntity<Tours> tourDetails(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.tourService.getTourById(id), HttpStatus.OK);
    }

    @GetMapping("/flights")
    public ResponseEntity<List<Flights>> flights(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.flightService.searchFlights(params), HttpStatus.OK);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flights> flightDetails(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.flightService.getFlightById(id), HttpStatus.OK);
    }

    @GetMapping("/bus-trips")
    public ResponseEntity<List<BusTrips>> busTrips(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.busTripService.searchBusTrips(params), HttpStatus.OK);
    }

    @GetMapping("/bus-trips/{id}")
    public ResponseEntity<BusTrips> busTripDetails(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(this.busTripService.getBusTripById(id), HttpStatus.OK);
    }
}
