package com.hotelapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Contacts {

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;
}
