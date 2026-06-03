
package com.mycompany.services.impl;

import com.mycompany.enums.VerificationStatus;
import com.mycompany.pojo.BusTrips;
import com.mycompany.pojo.Flights;
import com.mycompany.pojo.Hotels;
import com.mycompany.pojo.ProviderProfiles;
import com.mycompany.pojo.Tours;
import com.mycompany.repositories.BusTripRepository;
import com.mycompany.repositories.FlightRepository;
import com.mycompany.repositories.HotelRepository;
import com.mycompany.repositories.LocationRepository;
import com.mycompany.repositories.ProviderRepository;
import com.mycompany.repositories.TourRepository;
import com.mycompany.services.ProviderService;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepo;
    @Autowired
    private LocationRepository locationRepo;
    @Autowired
    private HotelRepository hotelRepo;
    @Autowired
    private TourRepository tourRepo;
    @Autowired
    private FlightRepository flightRepo;
    @Autowired
    private BusTripRepository busTripRepo;

    @Override
    public ProviderProfiles getProviderById(Long id) {
        return this.providerRepo.getProviderById(id);
    }

    @Override
    public ProviderProfiles getProviderByUserId(Long userId) {
        return this.providerRepo.getProviderByUserId(userId);
    }

    @Override
    public List<ProviderProfiles> getProvidersByStatus(VerificationStatus status) {
        return this.providerRepo.getProvidersByStatus(status);
    }

    @Override
    public ProviderProfiles addOrUpdateProvider(ProviderProfiles provider) {
        return this.providerRepo.addOrUpdateProvider(provider);
    }

    @Override
    public void updateVerificationStatus(Long providerId, VerificationStatus status) {
        this.providerRepo.updateVerificationStatus(providerId, status);
    }

    @Override
    public Object getProviderServices(Long providerId) {
        ProviderProfiles provider = this.providerRepo.getProviderById(providerId);

        return switch (provider.getBusinessType()) {
            case HOTEL -> this.hotelRepo.getHotelsByProviderId(providerId);
            case TOUR_COMPANY -> this.tourRepo.getToursByProviderId(providerId);
            case AIRLINE -> this.flightRepo.getFlightsByProviderId(providerId);
            case BUS_COMPANY -> this.busTripRepo.getBusTripsByProviderId(providerId);
        };
    }

    @Override
    public Object addOrUpdateService(Long serviceId, Map<String, String> params) {
        ProviderProfiles provider = this.providerRepo.getProviderById(Long.valueOf(params.get("providerId")));

        return switch (provider.getBusinessType()) {
            case HOTEL -> {
                Hotels hotel = this.buildHotel(serviceId, provider, params);
                ServiceValidator.validateHotel(hotel);
                yield this.hotelRepo.addOrUpdateHotel(hotel);
            }
            case TOUR_COMPANY -> {
                Tours tour = this.buildTour(serviceId, provider, params);
                ServiceValidator.validateTour(tour);
                yield this.tourRepo.addOrUpdateTour(tour);
            }
            case AIRLINE -> {
                Flights flight = this.buildFlight(serviceId, provider, params);
                ServiceValidator.validateFlight(flight);
                yield this.flightRepo.addOrUpdateFlight(flight);
            }
            case BUS_COMPANY -> {
                BusTrips busTrip = this.buildBusTrip(serviceId, provider, params);
                ServiceValidator.validateBusTrip(busTrip);
                yield this.busTripRepo.addOrUpdateBusTrip(busTrip);
            }
        };
    }

    @Override
    public void deleteService(Long serviceId, Long providerId) {
        ProviderProfiles provider = this.providerRepo.getProviderById(providerId);

        switch (provider.getBusinessType()) {
            case HOTEL -> this.hotelRepo.deleteHotel(serviceId);
            case TOUR_COMPANY -> this.tourRepo.deleteTour(serviceId);
            case AIRLINE -> this.flightRepo.deleteFlight(serviceId);
            case BUS_COMPANY -> this.busTripRepo.deleteBusTrip(serviceId);
        }
    }

    private Hotels buildHotel(Long id, ProviderProfiles provider, Map<String, String> params) {
        Hotels hotel = id != null ? this.hotelRepo.getHotelById(id) : new Hotels();
        hotel.setHotelName(params.get("hotelName"));
        hotel.setDescription(params.get("description"));
        hotel.setAddress(params.get("address"));
        hotel.setThumbnail(params.get("thumbnail"));
        hotel.setLocationId(this.locationRepo.getLocationById(Long.valueOf(params.get("locationId"))));
        hotel.setProviderId(provider);
        if (hotel.getCreatedAt() == null) {
            hotel.setCreatedAt(new Date());
        }
        return hotel;
    }

    private Tours buildTour(Long id, ProviderProfiles provider, Map<String, String> params) {
        Tours tour = id != null ? this.tourRepo.getTourById(id) : new Tours();
        tour.setTitle(params.get("title"));
        tour.setDescription(params.get("description"));
        tour.setDepartureDate(parseDateTime(params.get("departureDate")));
        tour.setDurationDays(Integer.parseInt(params.get("durationDays")));
        tour.setPrice(new BigDecimal(params.get("price")));
        tour.setAvailableSlots(Integer.parseInt(params.get("availableSlots")));
        tour.setThumbnail(params.get("thumbnail"));
        tour.setDepartureLocationId(this.locationRepo.getLocationById(Long.valueOf(params.get("departureLocationId"))));
        tour.setDestinationLocationId(this.locationRepo.getLocationById(Long.valueOf(params.get("destinationLocationId"))));
        tour.setProviderId(provider);
        if (tour.getCreatedAt() == null) {
            tour.setCreatedAt(new Date());
        }
        return tour;
    }

    private Flights buildFlight(Long id, ProviderProfiles provider, Map<String, String> params) {
        Flights flight = id != null ? this.flightRepo.getFlightById(id) : new Flights();
        flight.setFlightCode(params.get("flightCode"));
        flight.setDepartureTime(parseDateTime(params.get("departureTime")));
        flight.setArrivalTime(parseDateTime(params.get("arrivalTime")));
        flight.setPrice(new BigDecimal(params.get("price")));
        flight.setAvailableSeats(Integer.parseInt(params.get("availableSeats")));
        flight.setThumbnail(params.get("thumbnail"));
        flight.setDepartureLocationId(this.locationRepo.getLocationById(Long.valueOf(params.get("departureLocationId"))));
        flight.setArrivalLocationId(this.locationRepo.getLocationById(Long.valueOf(params.get("arrivalLocationId"))));
        flight.setProviderId(provider);
        return flight;
    }

    private BusTrips buildBusTrip(Long id, ProviderProfiles provider, Map<String, String> params) {
        BusTrips busTrip = id != null ? this.busTripRepo.getBusTripById(id) : new BusTrips();
        busTrip.setDepartureTime(parseDateTime(params.get("departureTime")));
        busTrip.setArrivalTime(parseDateTime(params.get("arrivalTime")));
        busTrip.setPrice(new BigDecimal(params.get("price")));
        busTrip.setAvailableSeats(Integer.parseInt(params.get("availableSeats")));
        busTrip.setDepartureLocationId(this.locationRepo.getLocationById(Long.valueOf(params.get("departureLocationId"))));
        busTrip.setArrivalLocationId(this.locationRepo.getLocationById(Long.valueOf(params.get("arrivalLocationId"))));
        busTrip.setProviderId(provider);
        return busTrip;
    }

    private Date parseDateTime(String value) {
        String normalized = value.replace("T", " ");
        if (normalized.length() == 16) {
            normalized += ":00";
        }
        return Timestamp.valueOf(normalized);
    }
}
