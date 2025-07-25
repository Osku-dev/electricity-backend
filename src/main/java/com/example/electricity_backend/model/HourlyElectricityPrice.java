package com.example.electricity_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HourlyElectricityPrice {

    private Double price;

    @JsonProperty("second_hour_price") // edge case when switching to winter time
    private Double secondHourPrice;


}
