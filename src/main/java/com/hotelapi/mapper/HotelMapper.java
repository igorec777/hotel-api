package com.hotelapi.mapper;

import com.hotelapi.dto.AddressDto;
import com.hotelapi.dto.ArrivalTimeDto;
import com.hotelapi.dto.ContactsDto;
import com.hotelapi.dto.HotelCreateRequest;
import com.hotelapi.dto.HotelDetailsResponse;
import com.hotelapi.dto.HotelShortResponse;
import com.hotelapi.entity.Address;
import com.hotelapi.entity.ArrivalTime;
import com.hotelapi.entity.Contacts;
import com.hotelapi.entity.Hotel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelCreateRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.name());
        hotel.setDescription(request.description());
        hotel.setBrand(request.brand());
        hotel.setAddress(toAddress(request.address()));
        hotel.setContacts(toContacts(request.contacts()));
        hotel.setArrivalTime(toArrivalTime(request.arrivalTime()));
        return hotel;
    }

    public HotelShortResponse toShort(Hotel hotel) {
        return new HotelShortResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getAddress().toLine(),
                hotel.getContacts().getPhone()
        );
    }

    public HotelDetailsResponse toDetails(Hotel hotel) {
        return new HotelDetailsResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getBrand(),
                toAddressDto(hotel.getAddress()),
                toContactsDto(hotel.getContacts()),
                toArrivalTimeDto(hotel.getArrivalTime()),
                new ArrayList<>(hotel.getAmenities())
        );
    }

    private Address toAddress(AddressDto dto) {
        Address address = new Address();
        address.setHouseNumber(dto.houseNumber());
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setCountry(dto.country());
        address.setPostCode(dto.postCode());
        return address;
    }

    private Contacts toContacts(ContactsDto dto) {
        Contacts contacts = new Contacts();
        contacts.setPhone(dto.phone());
        contacts.setEmail(dto.email());
        return contacts;
    }

    private ArrivalTime toArrivalTime(ArrivalTimeDto dto) {
        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(dto.checkIn());
        arrivalTime.setCheckOut(dto.checkOut());
        return arrivalTime;
    }

    private AddressDto toAddressDto(Address address) {
        return new AddressDto(
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getCountry(),
                address.getPostCode()
        );
    }

    private ContactsDto toContactsDto(Contacts contacts) {
        return new ContactsDto(contacts.getPhone(), contacts.getEmail());
    }

    private ArrivalTimeDto toArrivalTimeDto(ArrivalTime arrivalTime) {
        return new ArrivalTimeDto(arrivalTime.getCheckIn(), arrivalTime.getCheckOut());
    }
}
