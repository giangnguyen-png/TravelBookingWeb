
package com.mycompany.controllers;

import com.mycompany.enums.VerificationStatus;
import com.mycompany.services.BusTripService;
import com.mycompany.services.FlightService;
import com.mycompany.services.HotelService;
import com.mycompany.services.ProviderService;
import com.mycompany.services.TourService;
import com.mycompany.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AdminHomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private TourService tourService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private BusTripService busTripService;

    @RequestMapping("/")
    public String index(Model model){
        this.addDashboardData(model);
        return "index";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        this.addDashboardData(model);
        return "index";
    }

    private void addDashboardData(Model model) {
        Map<String, String> params = Map.of("size", "20");
        model.addAttribute("usersCount", this.userService.getUsers().size());
        model.addAttribute("pendingProvidersCount", this.providerService.getProvidersByStatus(VerificationStatus.PENDING).size());
        model.addAttribute("hotelsCount", this.hotelService.searchHotels(params).size());
        model.addAttribute("toursCount", this.tourService.searchTours(params).size());
        model.addAttribute("flightsCount", this.flightService.searchFlights(params).size());
        model.addAttribute("busTripsCount", this.busTripService.searchBusTrips(params).size());
    }
}
