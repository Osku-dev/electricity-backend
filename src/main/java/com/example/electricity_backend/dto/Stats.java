package com.example.electricity_backend.dto;


public record Stats (
    double minPrice,
    double maxPrice,
    double avgPrice
) {}
