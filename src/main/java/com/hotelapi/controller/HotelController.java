package com.hotelapi.controller;

import com.hotelapi.dto.HotelCreateRequest;
import com.hotelapi.dto.HotelDetailsResponse;
import com.hotelapi.dto.HotelSearchParams;
import com.hotelapi.dto.HotelShortResponse;
import com.hotelapi.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    public List<HotelShortResponse> getHotels() {
        return hotelService.findAll();
    }

    @GetMapping("/hotels/{id}")
    public HotelDetailsResponse getHotel(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    @GetMapping("/search")
    public List<HotelShortResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenities
    ) {
        return hotelService.search(new HotelSearchParams(name, brand, city, country, amenities));
    }

    @PostMapping("/hotels")
    public HotelShortResponse create(@Valid @RequestBody HotelCreateRequest request) {
        return hotelService.create(request);
    }

    @PostMapping("/hotels/{id}/amenities")
    public List<String> addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        return hotelService.addAmenities(id, amenities);
    }

    @GetMapping("/histogram/{param}")
    public Map<String, Long> histogram(@PathVariable String param) {
        return hotelService.histogram(param);
    }
}
