package com.campsite.validators;

import com.campsite.controller.request.ReservationDTO;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MaxTimeReservedValidator implements ConstraintValidator<MaxTimeReserved, ReservationDTO> {
   public void initialize(final MaxTimeReserved constraint) {
   }

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
         long result = ChronoUnit.DAYS.between(from, to);
         if (result > 3) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The campsite can be reserved for max 3 days")
                    .addPropertyNode("dateTo").addConstraintViolation();
            return false;
         }
         return true;
      } catch (Exception e) {
         context.disableDefaultConstraintViolation();
         context.buildConstraintViolationWithTemplate(e.getMessage())
                 .addPropertyNode("dateTo").addConstraintViolation();
         return false;
      }
   }
}
