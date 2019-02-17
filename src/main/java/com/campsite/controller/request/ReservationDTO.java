package com.campsite.controller.request;

import com.campsite.validators.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@IsDateFromGreaterThanDateTo
@OneDayBefore
@MaxTimeReserved
@UpToOneMonth
public class ReservationDTO implements Serializable {

    private String bookingId;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @Email
    private String email;

    private String dateFrom;
    private String dateTo;
}
