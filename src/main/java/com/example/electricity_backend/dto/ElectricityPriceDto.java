package com.example.electricity_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ElectricityPriceDto {
    private final String startDate;
    private final String endDate;
    private final double price;
}
