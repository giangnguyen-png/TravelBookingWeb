/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.enums.RoomType;
import com.mycompany.enums.VerificationStatus;
import com.mycompany.pojo.BusTrips;
import com.mycompany.pojo.Flights;
import com.mycompany.pojo.HotelRooms;
import com.mycompany.pojo.Hotels;
import com.mycompany.pojo.Tours;
import com.mycompany.services.BusTripService;
import com.mycompany.services.FlightService;
import com.mycompany.services.HotelRoomService;
import com.mycompany.services.HotelService;
import com.mycompany.services.LocationService;
import com.mycompany.services.ProviderService;
import com.mycompany.services.TourService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nguyen
 */
@Controller
@RequestMapping("/admin/services")
public class AdminServiceController {

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
    @Autowired
    private LocationService locationService;
    @Autowired
    private ProviderService providerService;

    @GetMapping
    public String list(Model model) {
        this.addCommonData(model);
        this.addFormData(model, new Hotels(), new HotelRooms(), new Tours(), new Flights(), new BusTrips());
        return "services";
    }

    @GetMapping("/hotels/{id}")
    public String editHotel(Model model, @PathVariable(value = "id") Long id) {
        this.addCommonData(model);
        this.addFormData(model, this.hotelService.getHotelById(id), new HotelRooms(), new Tours(), new Flights(), new BusTrips());
        return "services";
    }

    @PostMapping("/hotels")
    public String saveHotel(Model model, @ModelAttribute(value = "hotel") Hotels hotel) {
        try {
            this.hotelService.addOrUpdateHotel(hotel);
            return "redirect:/admin/services";
        } catch (IllegalArgumentException ex) {
            return this.validationError(model, ex, hotel, new HotelRooms(), new Tours(), new Flights(), new BusTrips());
        }
    }

    @PostMapping("/hotels/{id}/delete")
    public String deleteHotel(@PathVariable(value = "id") Long id) {
        this.hotelService.deleteHotel(id);
        return "redirect:/admin/services";
    }

    @PostMapping("/rooms")
    public String saveRoom(Model model, @ModelAttribute(value = "room") HotelRooms room) {
        try {
            this.roomService.addOrUpdateRoom(room);
            return "redirect:/admin/services";
        } catch (IllegalArgumentException ex) {
            return this.validationError(model, ex, new Hotels(), room, new Tours(), new Flights(), new BusTrips());
        }
    }

    @PostMapping("/rooms/{id}/delete")
    public String deleteRoom(@PathVariable(value = "id") Long id) {
        this.roomService.deleteRoom(id);
        return "redirect:/admin/services";
    }

    @PostMapping("/tours")
    public String saveTour(Model model, @ModelAttribute(value = "tour") Tours tour) {
        try {
            this.tourService.addOrUpdateTour(tour);
            return "redirect:/admin/services";
        } catch (IllegalArgumentException ex) {
            return this.validationError(model, ex, new Hotels(), new HotelRooms(), tour, new Flights(), new BusTrips());
        }
    }

    @PostMapping("/tours/{id}/delete")
    public String deleteTour(@PathVariable(value = "id") Long id) {
        this.tourService.deleteTour(id);
        return "redirect:/admin/services";
    }

    @PostMapping("/flights")
    public String saveFlight(Model model, @ModelAttribute(value = "flight") Flights flight) {
        try {
            this.flightService.addOrUpdateFlight(flight);
            return "redirect:/admin/services";
        } catch (IllegalArgumentException ex) {
            return this.validationError(model, ex, new Hotels(), new HotelRooms(), new Tours(), flight, new BusTrips());
        }
    }

    @PostMapping("/flights/{id}/delete")
    public String deleteFlight(@PathVariable(value = "id") Long id) {
        this.flightService.deleteFlight(id);
        return "redirect:/admin/services";
    }

    @PostMapping("/bus-trips")
    public String saveBusTrip(Model model, @ModelAttribute(value = "busTrip") BusTrips busTrip) {
        try {
            this.busTripService.addOrUpdateBusTrip(busTrip);
            return "redirect:/admin/services";
        } catch (IllegalArgumentException ex) {
            return this.validationError(model, ex, new Hotels(), new HotelRooms(), new Tours(), new Flights(), busTrip);
        }
    }

    @PostMapping("/bus-trips/{id}/delete")
    public String deleteBusTrip(@PathVariable(value = "id") Long id) {
        this.busTripService.deleteBusTrip(id);
        return "redirect:/admin/services";
    }

    private void addCommonData(Model model) {
        Map<String, String> params = Map.of("size", "20");
        model.addAttribute("hotels", this.hotelService.searchHotels(params));
        model.addAttribute("tours", this.tourService.searchTours(params));
        model.addAttribute("flights", this.flightService.searchFlights(params));
        model.addAttribute("busTrips", this.busTripService.searchBusTrips(params));
        model.addAttribute("locations", this.locationService.getLocations());
        model.addAttribute("providers", this.providerService.getProvidersByStatus(VerificationStatus.APPROVED));
        model.addAttribute("roomTypes", RoomType.values());
    }

    private String validationError(Model model, IllegalArgumentException ex, Hotels hotel, HotelRooms room, Tours tour, Flights flight, BusTrips busTrip) {
        this.addCommonData(model);
        this.addFormData(model, hotel, room, tour, flight, busTrip);
        model.addAttribute("err", ex.getMessage());
        return "services";
    }

    private void addFormData(Model model, Hotels hotel, HotelRooms room, Tours tour, Flights flight, BusTrips busTrip) {
        model.addAttribute("hotel", hotel);
        model.addAttribute("room", room);
        model.addAttribute("tour", tour);
        model.addAttribute("flight", flight);
        model.addAttribute("busTrip", busTrip);
    }
}
