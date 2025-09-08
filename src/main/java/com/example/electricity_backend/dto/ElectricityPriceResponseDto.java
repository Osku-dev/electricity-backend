package com.example.electricity_backend.dto;

import java.util.List;

import lombok.Value;

@Value
public class ElectricityPriceResponseDto {
    private List<ElectricityPriceDto> prices;
}
