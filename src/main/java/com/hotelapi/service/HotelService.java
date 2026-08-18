package com.hotelapi.service;

import com.hotelapi.HistogramParam;
import com.hotelapi.dto.HotelCreateRequest;
import com.hotelapi.dto.HotelDetailsResponse;
import com.hotelapi.dto.HotelSearchParams;
import com.hotelapi.dto.HotelShortResponse;
import com.hotelapi.entity.Hotel;
import com.hotelapi.exception.BadRequestException;
import com.hotelapi.exception.HotelNotFoundException;
import com.hotelapi.mapper.HotelMapper;
import com.hotelapi.repository.HotelRepository;
import com.hotelapi.repository.HotelSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional(readOnly = true)
    public List<HotelShortResponse> findAll() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toShort)
                .toList();
    }

    @Transactional(readOnly = true)
    public HotelDetailsResponse findById(Long id) {
        return hotelMapper.toDetails(getHotel(id));
    }

    @Transactional(readOnly = true)
    public List<HotelShortResponse> search(HotelSearchParams params) {
        return hotelRepository.findAll(HotelSpecifications.fromParams(
                        params.name(),
                        params.brand(),
                        params.city(),
                        params.country(),
                        params.amenities()
                )).stream()
                .map(hotelMapper::toShort)
                .toList();
    }

    @Transactional
    public HotelShortResponse create(HotelCreateRequest request) {
        Hotel saved = hotelRepository.save(hotelMapper.toEntity(request));
        return hotelMapper.toShort(saved);
    }

    @Transactional
    public List<String> addAmenities(Long id, List<String> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            throw new BadRequestException("Amenities list must not be empty");
        }
        Hotel hotel = getHotel(id);
        for (String amenity : amenities) {
            if (!hotel.getAmenities().contains(amenity)) {
                hotel.getAmenities().add(amenity);
            }
        }
        return List.copyOf(hotel.getAmenities());
    }

    @Transactional(readOnly = true)
    public Map<String, Long> histogram(String param) {
        List<Object[]> rows = switch (HistogramParam.from(param)) {
            case BRAND -> hotelRepository.countGroupedByBrand();
            case CITY -> hotelRepository.countGroupedByCity();
            case COUNTRY -> hotelRepository.countGroupedByCountry();
            case AMENITIES -> hotelRepository.countGroupedByAmenity();
        };

        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put(String.valueOf(row[0]), (Long) row[1]);
        }
        return result;
    }

    private Hotel getHotel(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
    }
}
