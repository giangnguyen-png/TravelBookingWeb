package com.mycompany.services.impl;

import com.mycompany.pojo.BusTrips;
import com.mycompany.pojo.Flights;
import com.mycompany.pojo.HotelRooms;
import com.mycompany.pojo.Hotels;
import com.mycompany.pojo.Tours;
import java.math.BigDecimal;
import java.util.Date;

final class ServiceValidator {

    private ServiceValidator() {
    }

    static void validateHotel(Hotels hotel) {
        requireText(hotel.getHotelName(), "Tên khách sạn không được để trống");
        requireText(hotel.getAddress(), "Địa chỉ khách sạn không được để trống");
        requireNotNull(hotel.getLocationId(), "Địa điểm khách sạn không được để trống");
        requireNotNull(hotel.getProviderId(), "Provider khách sạn không được để trống");
    }

    static void validateRoom(HotelRooms room) {
        requireText(room.getRoomName(), "Tên phòng không được để trống");
        requireNotNull(room.getRoomType(), "Loại phòng không được để trống");
        requirePositive(room.getPricePerNight(), "Giá phòng phải lớn hơn 0");
        requireNonNegative(room.getAvailableRooms(), "Số phòng trống không được âm");
        requireNotNull(room.getHotelId(), "Khách sạn của phòng không được để trống");
    }

    static void validateTour(Tours tour) {
        requireText(tour.getTitle(), "Tên tour không được để trống");
        requireNotNull(tour.getDepartureDate(), "Ngày đi của tour không được để trống");
        requirePositive(tour.getDurationDays(), "Số ngày tour phải lớn hơn 0");
        requirePositive(tour.getPrice(), "Giá tour phải lớn hơn 0");
        requireNonNegative(tour.getAvailableSlots(), "Số chỗ trống của tour không được âm");
        requireNotNull(tour.getDepartureLocationId(), "Điểm đi của tour không được để trống");
        requireNotNull(tour.getDestinationLocationId(), "Điểm đến của tour không được để trống");
        requireNotNull(tour.getProviderId(), "Provider tour không được để trống");
    }

    static void validateFlight(Flights flight) {
        requireText(flight.getFlightCode(), "Mã chuyến bay không được để trống");
        requireNotNull(flight.getDepartureTime(), "Giờ đi chuyến bay không được để trống");
        requireNotNull(flight.getArrivalTime(), "Giờ đến chuyến bay không được để trống");
        requireAfter(flight.getArrivalTime(), flight.getDepartureTime(), "Giờ đến phải sau giờ đi");
        requirePositive(flight.getPrice(), "Giá vé máy bay phải lớn hơn 0");
        requireNonNegative(flight.getAvailableSeats(), "Số ghế trống không được âm");
        requireNotNull(flight.getDepartureLocationId(), "Điểm đi chuyến bay không được để trống");
        requireNotNull(flight.getArrivalLocationId(), "Điểm đến chuyến bay không được để trống");
        requireNotNull(flight.getProviderId(), "Provider chuyến bay không được để trống");
    }

    static void validateBusTrip(BusTrips busTrip) {
        requireNotNull(busTrip.getDepartureTime(), "Giờ đi chuyến xe không được để trống");
        requireNotNull(busTrip.getArrivalTime(), "Giờ đến chuyến xe không được để trống");
        requireAfter(busTrip.getArrivalTime(), busTrip.getDepartureTime(), "Giờ đến phải sau giờ đi");
        requirePositive(busTrip.getPrice(), "Giá vé xe phải lớn hơn 0");
        requireNonNegative(busTrip.getAvailableSeats(), "Số ghế trống không được âm");
        requireNotNull(busTrip.getDepartureLocationId(), "Điểm đi chuyến xe không được để trống");
        requireNotNull(busTrip.getArrivalLocationId(), "Điểm đến chuyến xe không được để trống");
        requireNotNull(busTrip.getProviderId(), "Provider chuyến xe không được để trống");
    }

    private static void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requireNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requirePositive(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requireNonNegative(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requirePositive(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void requireAfter(Date end, Date start, String message) {
        if (!end.after(start)) {
            throw new IllegalArgumentException(message);
        }
    }
}
