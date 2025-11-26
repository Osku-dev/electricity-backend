package com.example.electricity_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HourlyElectricityPriceDto {

    private final Double price;

    @JsonProperty("second_hour_price") // edge case when switching to winter time
    private final Double secondHourPrice;


}
