package com.example.electricity_backend.model;

import java.util.List;

import lombok.Data;

// Wrapper class for the response from the electricity price API
@Data
public class ElectricityPriceResponse {
    private List<ElectricityPrice> prices;
}
