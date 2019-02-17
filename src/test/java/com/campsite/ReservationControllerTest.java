package com.campsite;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.campsite.controller.request.ReservationDTO;
import com.campsite.model.Reservation;
import com.campsite.repository.ReservationRepository;
import com.campsite.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ReservationControllerTest {

    @MockBean
    ReservationService reservationService;

    @MockBean
    ReservationRepository reservationRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldCreateReservation() throws Exception {
        LocalDateTime now = LocalDateTime.now().plusDays(2);
        LocalDateTime plus3Days = now.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateFrom = now.format(formatter);
        String dateTo = plus3Days.format(formatter);

        Mockito.when(reservationService.save(Mockito.any(ReservationDTO.class))).thenReturn("123456");
        ReservationDTO dto = ReservationDTO.builder()
                .name("test")
                .lastname("test")
                .email("seba@seba.com")
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"bookingId\":\"123456\",\"message\":\"Booking Created successfully\"}"));
    }


    @Test
    public void shouldCreateReservation_missing_name() throws Exception {
        ReservationDTO dto = ReservationDTO.builder()
                .lastname("test")
                .email("seba@seba.com")
                .dateFrom("2019-02-25 00:00:00")
                .dateTo("2019-02-28 00:00:00")
                .build();
        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("name"))
                .andExpect(jsonPath("$.message").value("must not be blank"));
    }

    @Test
    public void shouldFindByBookingIdReservation_ok() throws Exception {
        Reservation r = Reservation.builder()
                .name("test")
                .lastname("test")
                .email("seba@seba.com")
                .from(1551409200000L)
                .to(1551582000000L)
                .build();

        Mockito.when(reservationService.findByBookingId("1234")).thenReturn(r);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/book/")
                .param("bookingId", "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void should_not_create_same_date() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plus3Days = now.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateFrom = now.format(formatter);
        String dateTo = plus3Days.format(formatter);
        ReservationDTO dto = ReservationDTO.builder()
                .name("test")
                .lastname("test")
                .email("seba@seba.com")
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("dateFrom"))
                .andExpect(jsonPath("$.message").value("The campsite can be reserved minimum 1 day(s) ahead of arrival"));
    }

    @Test
    public void should_not_create_more_than_one_month() throws Exception {
        LocalDateTime now = LocalDateTime.now().plusMonths(2);
        LocalDateTime plus3Days = now.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateFrom = now.format(formatter);
        String dateTo = plus3Days.format(formatter);

        ReservationDTO dto = ReservationDTO.builder()
                .name("test")
                .lastname("test")
                .email("seba@seba.com")
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("dateFrom"))
                .andExpect(jsonPath("$.message").value("The campsite can be reserved up to 1 month in advance"));
    }

    @Test
    public void should_not_create_more_than_3_days_max() throws Exception {
        LocalDateTime now = LocalDateTime.now().plusDays(2);
        LocalDateTime plus3Days = now.plusDays(4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateFrom = now.format(formatter);
        String dateTo = plus3Days.format(formatter);

        ReservationDTO dto = ReservationDTO.builder()
                .name("test")
                .lastname("test")
                .email("seba@seba.com")
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("dateTo"))
                .andExpect(jsonPath("$.message").value("The campsite can be reserved for max 3 days"));
    }

}
