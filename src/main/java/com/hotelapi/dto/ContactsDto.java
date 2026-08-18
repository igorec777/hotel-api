package com.hotelapi.dto;

import jakarta.validation.constraints.NotBlank;

public record ContactsDto(
        @NotBlank String phone,
        @NotBlank String email
) {
}
