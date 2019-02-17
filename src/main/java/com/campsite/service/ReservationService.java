package com.campsite.service;

import com.campsite.controller.request.ReservationDTO;
import com.campsite.controller.response.AvailableResponse;
import com.campsite.controller.response.ReservationResponse;
import com.campsite.model.Reservation;
import com.campsite.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private static final int MAX_CAPACITY = 3;

    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> all() {
        return reservationRepository.findAll();
    }

    public AvailableResponse available(String dateFrom, String dateTo) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (!StringUtils.hasText(dateFrom) || !StringUtils.hasText(dateTo)) {
            LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime plusOne = now.plusMonths(1);
            return new AvailableResponse(now.format(formatter), plusOne.format(formatter), true);
        }
        LocalDateTime localDateTimeFrom = LocalDateTime.parse(dateFrom, formatter);
        LocalDateTime localDateTimeTo = LocalDateTime.parse(dateTo, formatter);

        long from = localDateTimeFrom.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long to = localDateTimeTo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        long qty = reservationRepository.countAllByFromGreaterThanEqualAndToLessThanEqual(from, to);
        if (qty < MAX_CAPACITY) {
            return new AvailableResponse(dateFrom, dateTo, true);
        }
        return new AvailableResponse(dateFrom, dateTo, false);
    }

    public String save(ReservationDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime from = LocalDateTime.parse(dto.getDateFrom(), formatter);
        LocalDateTime to = LocalDateTime.parse(dto.getDateTo(), formatter);

        Reservation reservation = Reservation.builder()
                .from(from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .to(to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .email(dto.getEmail())
                .name(dto.getName())
                .lastname(dto.getLastname())
                .bookingId(UUID.randomUUID().toString())
                .build();
        reservationRepository.save(reservation);
        return reservation.getBookingId();
    }

    public Reservation findByBookingId(final String bookingId) {
        Reservation r = reservationRepository.findByBookingId(bookingId);
        Assert.notNull(r, "Booking ID: " + bookingId + " Not Found");
        return r;
    }

    public ReservationResponse update(final ReservationDTO dto, final Reservation reservation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime from = LocalDateTime.parse(dto.getDateFrom(), formatter);
        LocalDateTime to = LocalDateTime.parse(dto.getDateTo(), formatter);

        Reservation updated = Reservation.builder()
                .from(from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .to(to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .email(dto.getEmail())
                .lastname(dto.getLastname())
                .name(dto.getName())
                .bookingId(reservation.getBookingId())
                ._id(reservation.getId())
                .build();
        reservationRepository.save(updated);
        return new ReservationResponse(updated.getBookingId(), "Updated successfully");
    }

    public ReservationResponse delete(String bookingId) {
        Reservation r = reservationRepository.findByBookingId(bookingId);
        Assert.notNull(r, "Reservation not found");
        reservationRepository.delete(r);
        return new ReservationResponse(bookingId, "Deleted successfully");
    }
}
