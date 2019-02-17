package com.campsite.validators;

import com.campsite.controller.request.ReservationDTO;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IsDateFromGreaterThanDateToValidator implements ConstraintValidator<IsDateFromGreaterThanDateTo, ReservationDTO> {

    @Override
    public void initialize(final IsDateFromGreaterThanDateTo constraintAnnotation) {

    }

    @Override
    public boolean isValid(final ReservationDTO dto, final ConstraintValidatorContext context) {
        if (!StringUtils.hasText(dto.getDateFrom())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Date From should be null nor empty")
                    .addPropertyNode("dateFrom").addConstraintViolation();
            return false;
        }

        if (!StringUtils.hasText(dto.getDateTo())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Date To should be null nor empty")
                    .addPropertyNode("dateTo").addConstraintViolation();
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            LocalDateTime from = LocalDateTime.parse(dto.getDateFrom(), formatter);
            LocalDateTime to = LocalDateTime.parse(dto.getDateTo(), formatter);
            boolean result = from.isBefore(to);

            if (!result) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Date From should be before Date To")
                        .addPropertyNode("dateFrom").addConstraintViolation();
                return false;
            }
            return true;
        } catch (final Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode("dateFrom").addPropertyNode("dateTo").addConstraintViolation();
            return false;
        }
    }
}
