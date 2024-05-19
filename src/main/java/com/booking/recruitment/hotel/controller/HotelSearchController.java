package com.booking.recruitment.hotel.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.CityService;
import com.booking.recruitment.hotel.service.HotelService;


@RestController

public class HotelSearchController {
	@Autowired
    private CityService cityService;

    @Autowired
    private HotelService hotelService;

    @GetMapping("/search/{cityId}")
    public List<Hotel> searchHotels(@PathVariable Long cityId, @RequestParam(defaultValue = "distance") String sortBy) {
        City city = cityService.getCityById(cityId);

        List<Hotel> hotels = hotelService.getAllHotels();

        hotels.sort(Comparator.comparingDouble(hotel -> calculateDistance(city.getCityCentreLatitude(), city.getCityCentreLongitude(), hotel.getLatitude(), hotel.getLongitude())));

        return hotels.stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    private double calculateDistance(double cityLatitude, double cityLongitude, double hotelLatitude, double hotelLongitude) {
        double R = 6371; // Earth's radius in kilometers
        double lat1 = Math.toRadians(cityLatitude);
        double lat2 = Math.toRadians(hotelLatitude);
        double deltaLat = Math.toRadians(hotelLatitude - cityLatitude);
        double deltaLon = Math.toRadians(hotelLongitude - cityLongitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
