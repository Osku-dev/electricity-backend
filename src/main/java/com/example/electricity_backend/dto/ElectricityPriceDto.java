package com.example.electricity_backend.dto;

import lombok.Value;

@Value
public class ElectricityPriceDto {
    private String startDate;
    private String endDate;
    private double price;
}
