package com.example.electricity_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class HourlyElectricityPriceDto {

    private Double price;

    @JsonProperty("second_hour_price") // edge case when switching to winter time
    private Double secondHourPrice;


}
