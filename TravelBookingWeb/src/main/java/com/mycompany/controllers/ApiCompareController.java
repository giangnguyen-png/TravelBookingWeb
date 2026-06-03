package com.mycompany.controllers;

import com.mycompany.pojo.BusTrips;
import com.mycompany.pojo.Flights;
import com.mycompany.pojo.HotelRooms;
import com.mycompany.pojo.Hotels;
import com.mycompany.pojo.Locations;
import com.mycompany.pojo.Tours;
import com.mycompany.services.BusTripService;
import com.mycompany.services.FlightService;
import com.mycompany.services.HotelRoomService;
import com.mycompany.services.HotelService;
import com.mycompany.services.TourService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCompareController {

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelRoomService hotelRoomService;
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
                .filter(id -> !id.isBlank())
                .map(Long::valueOf)
                .toList();

        List<Map<String, Object>> data = switch (type.toUpperCase()) {
            case "HOTEL" -> this.hotelService.getHotelsByIds(serviceIds).stream().map(this::hotelDto).toList();
            case "TOUR" -> this.tourService.getToursByIds(serviceIds).stream().map(this::tourDto).toList();
            case "FLIGHT" -> this.flightService.getFlightsByIds(serviceIds).stream().map(this::flightDto).toList();
            case "BUS" -> this.busTripService.getBusTripsByIds(serviceIds).stream().map(this::busDto).toList();
            default -> throw new IllegalArgumentException("Invalid compare type");
        };

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private Map<String, Object> hotelDto(Hotels hotel) {
        Map<String, Object> data = baseDto(hotel.getId(), hotel.getHotelName());
        data.put("description", hotel.getDescription());
        data.put("address", hotel.getAddress());
        data.put("location", locationName(hotel.getLocationId()));
        data.put("rooms", this.hotelRoomService.getRoomsByHotelId(hotel.getId()).stream().map(this::roomDto).toList());
        return data;
    }

    private Map<String, Object> roomDto(HotelRooms room) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", room.getId());
        data.put("roomName", room.getRoomName());
        data.put("roomType", room.getRoomType());
        data.put("pricePerNight", room.getPricePerNight());
        data.put("availableRooms", room.getAvailableRooms());
        data.put("description", room.getDescription());
        return data;
    }

    private Map<String, Object> tourDto(Tours tour) {
        Map<String, Object> data = baseDto(tour.getId(), tour.getTitle());
        data.put("price", tour.getPrice());
        data.put("time", DATE_TIME_FORMAT.format(tour.getDepartureDate()));
        data.put("quantity", tour.getAvailableSlots());
        data.put("durationDays", tour.getDurationDays());
        data.put("departureLocation", locationName(tour.getDepartureLocationId()));
        data.put("destinationLocation", locationName(tour.getDestinationLocationId()));
        return data;
    }

    private Map<String, Object> flightDto(Flights flight) {
        Map<String, Object> data = baseDto(flight.getId(), flight.getFlightCode());
        data.put("price", flight.getPrice());
        data.put("time", DATE_TIME_FORMAT.format(flight.getDepartureTime()) + " - " + DATE_TIME_FORMAT.format(flight.getArrivalTime()));
        data.put("quantity", flight.getAvailableSeats());
        data.put("departureLocation", locationName(flight.getDepartureLocationId()));
        data.put("destinationLocation", locationName(flight.getArrivalLocationId()));
        return data;
    }

    private Map<String, Object> busDto(BusTrips busTrip) {
        Map<String, Object> data = baseDto(busTrip.getId(), "Chuyến xe #" + busTrip.getId());
        data.put("price", busTrip.getPrice());
        data.put("time", DATE_TIME_FORMAT.format(busTrip.getDepartureTime()) + " - " + DATE_TIME_FORMAT.format(busTrip.getArrivalTime()));
        data.put("quantity", busTrip.getAvailableSeats());
        data.put("departureLocation", locationName(busTrip.getDepartureLocationId()));
        data.put("destinationLocation", locationName(busTrip.getArrivalLocationId()));
        return data;
    }

    private Map<String, Object> baseDto(Long id, String name) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", id);
        data.put("name", name);
        return data;
    }

    private String locationName(Locations location) {
        if (location == null) {
            return "";
        }
        return String.join(", ", location.getProvince(), location.getCountry());
    }
}
