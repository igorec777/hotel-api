package com.hotelapi.dto;

public record HotelSearchParams(
        String name,
        String brand,
        String city,
        String country,
        String amenities
) {
}
