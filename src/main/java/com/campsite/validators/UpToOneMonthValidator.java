package com.campsite.validators;

import com.campsite.controller.request.ReservationDTO;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class UpToOneMonthValidator implements ConstraintValidator<UpToOneMonth, ReservationDTO> {
   public void initialize(final UpToOneMonth constraint) {
   }

   public boolean isValid(final ReservationDTO dto, final ConstraintValidatorContext context) {
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
         Period p = Period.between(to, from);
         int result = p.getMonths();
         if (result == 1) {
            result += p.getDays();
         }
         if (result > 1) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The campsite can be reserved up to 1 month in advance")
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
