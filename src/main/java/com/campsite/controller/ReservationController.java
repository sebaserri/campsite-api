package com.campsite.controller;

import com.campsite.controller.exception.ReservationException;
import com.campsite.controller.model.ErrorResponse;
import com.campsite.controller.request.ReservationDTO;
import com.campsite.controller.response.AvailableResponse;
import com.campsite.controller.response.ReservationResponse;
import com.campsite.model.Reservation;
import com.campsite.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/book")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public List<Reservation> fetchAll() {
        return reservationService.all();
    }

    @GetMapping("/available")
    @ResponseBody
    public AvailableResponse available(@RequestParam(value = "dateFrom") final String dateFrom,
                                       @RequestParam(value = "dateTo") final String dateTo) {
        try {
            return reservationService.available(dateFrom, dateTo);
        } catch (Exception e) {
            log.error("Error finding available", e);
            throw new ReservationException(e.getMessage(), e);
        }
    }

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody ReservationDTO dto) {
        try {
            final String bookingId = reservationService.save(dto);
            return new ReservationResponse(bookingId, "Booking Created successfully");
        } catch (final Exception e) {
            throw new ReservationException(e.getMessage(), e);
        }
    }

    @GetMapping("/{bookingId}")
    public Reservation findBookingId(@PathVariable String bookingId) {
        Assert.hasText(bookingId, "Booking ID must not be null nor empty");
        return reservationService.findByBookingId(bookingId);
    }

    @PutMapping("/")
    public ReservationResponse update(@Valid @RequestBody ReservationDTO dto) {
        Assert.hasText(dto.getBookingId(), "Booking ID is required");
        try {
            Reservation r = reservationService.findByBookingId(dto.getBookingId());
            Assert.notNull(r, "Reservation not found");
            return reservationService.update(dto, r);
        } catch (Exception e) {
            throw new ReservationException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{bookingId}")
    public ReservationResponse delete(@PathVariable String bookingId) {
        try {
            return reservationService.delete(bookingId);
        } catch (Exception e) {
            throw new ReservationException(e.getMessage(), e);
        }
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors().stream()
                .map(e -> ErrorResponse.builder().field(e.getField()).message(e.getDefaultMessage()).build())
                .findFirst()
                .orElse(ErrorResponse.builder().message(exception.getMessage()).build());
    }
}
