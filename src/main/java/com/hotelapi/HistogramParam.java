package com.hotelapi;

import com.hotelapi.exception.BadRequestException;

import java.util.Locale;

public enum HistogramParam {
    BRAND,
    CITY,
    COUNTRY,
    AMENITIES;

    public static HistogramParam from(String raw) {
        try {
            return valueOf(raw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestException("Unsupported histogram param: " + raw);
        }
    }
}
