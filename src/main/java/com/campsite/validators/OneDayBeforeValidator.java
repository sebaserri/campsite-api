package com.campsite.validators;

import com.campsite.controller.request.ReservationDTO;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class OneDayBeforeValidator implements ConstraintValidator<OneDayBefore, ReservationDTO> {
   public void initialize(OneDayBefore constraint) {
   }

   public boolean isValid(ReservationDTO dto, ConstraintValidatorContext context) {
      if (!StringUtils.hasText(dto.getDateFrom())) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate("Date From should be null nor empty")
                 .addPropertyNode("dateFrom").addConstraintViolation();
         return false;
      }
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      try {
         LocalDate from = LocalDate.parse(dto.getDateFrom(), formatter);
         LocalDate to = LocalDate.now();
         long result = ChronoUnit.DAYS.between(from, to);
         if (result >= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The campsite can be reserved minimum 1 day(s) ahead of arrival")
                    .addPropertyNode("dateFrom").addConstraintViolation();
            return false;
         }
         return true;
      } catch (final Exception e) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate(e.getMessage())
                 .addPropertyNode("dateFrom").addConstraintViolation();
         return false;
      }
   }
}
