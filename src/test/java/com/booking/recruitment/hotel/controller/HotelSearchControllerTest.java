package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.CityService;
import com.booking.recruitment.hotel.service.HotelService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HotelSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @MockBean
    private HotelService hotelService;

    @Test
    @DisplayName("Search 3 hotels closest to city centre")
    public void testSearchHotels() throws Exception {
        City city = new City(1L, "Test City", 52.0, 4.0);
        List<Hotel> hotels = Arrays.asList(
                new Hotel(1L, "Hotel A", 8.5, city, "Address A", 52.1, 4.1, false),
                new Hotel(2L, "Hotel B", 9.0, city, "Address B", 52.2, 4.2, false),
                new Hotel(3L, "Hotel C", 7.8, city, "Address C", 52.3, 4.3, false),
                new Hotel(4L, "Hotel D", 8.2, city, "Address D", 52.4, 4.4, false)
        );

        when(cityService.getCityById(1L)).thenReturn(city);
        when(hotelService.getAllHotels()).thenReturn(hotels);

        mockMvc.perform(get("/search/1?sortBy=distance"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].name").value("Hotel A"))
        .andExpect(jsonPath("$[1].name").value("Hotel B"))
        .andExpect(jsonPath("$[2].name").value("Hotel C"));
    }
}