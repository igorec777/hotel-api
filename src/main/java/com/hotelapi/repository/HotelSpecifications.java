package com.hotelapi.repository;

import com.hotelapi.entity.Hotel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class HotelSpecifications {

    private HotelSpecifications() {
    }

    public static Specification<Hotel> fromParams(String name, String brand, String city, String country, String amenities) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(brand)) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));
            }
            if (StringUtils.hasText(city)) {
                predicates.add(cb.equal(cb.lower(root.get("address").get("city")), city.toLowerCase()));
            }
            if (StringUtils.hasText(country)) {
                predicates.add(cb.equal(cb.lower(root.get("address").get("country")), country.toLowerCase()));
            }
            if (StringUtils.hasText(amenities)) {
                Join<Hotel, String> amenityJoin = root.join("amenities");
                predicates.add(cb.equal(cb.lower(amenityJoin), amenities.toLowerCase()));
                if (query != null) {
                    query.distinct(true);
                }
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
