package com.hotelapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HotelApiApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createThenReadSearchAndHistogram() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "name", "DoubleTree by Hilton Minsk",
                "description", "193 rooms in Minsk",
                "brand", "Hilton",
                "address", Map.of(
                        "houseNumber", 9,
                        "street", "Pobediteley Avenue",
                        "city", "Minsk",
                        "country", "Belarus",
                        "postCode", "220004"
                ),
                "contacts", Map.of(
                        "phone", "+375 17 309-80-00",
                        "email", "doubletreeminsk.info@hilton.com"
                ),
                "arrivalTime", Map.of(
                        "checkIn", "14:00",
                        "checkOut", "12:00"
                )
        ));

        MvcResult created = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$.address").value("9 Pobediteley Avenue, Minsk, 220004, Belarus"))
                .andExpect(jsonPath("$.phone").value("+375 17 309-80-00"))
                .andReturn();

        long id = objectMapper.readTree(created.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.address.city").value("Minsk"))
                .andExpect(jsonPath("$.contacts.email").value("doubletreeminsk.info@hilton.com"))
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"));

        mockMvc.perform(post("/hotels/{id}/amenities", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of("Free WiFi", "Free parking"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItem("Free WiFi")));

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id));

        mockMvc.perform(get("/search").param("city", "minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(containsString("Hilton")));

        mockMvc.perform(get("/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(1));

        mockMvc.perform(get("/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Free WiFi']").value(1));
    }

    @Test
    void unknownHotelReturns404() throws Exception {
        mockMvc.perform(get("/hotels/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void unknownHistogramParamReturns400() throws Exception {
        mockMvc.perform(get("/histogram/stars"))
                .andExpect(status().isBadRequest());
    }
}
