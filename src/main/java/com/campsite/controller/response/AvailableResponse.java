package com.campsite.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableResponse implements Serializable {

    private String dateFrom;
    private String dateTo;
    private boolean available;
}
