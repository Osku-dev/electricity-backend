package com.example.electricity_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Stats {
    private final Float minPrice;
    private final Float maxPrice;
    private final Float avgPrice;
}
