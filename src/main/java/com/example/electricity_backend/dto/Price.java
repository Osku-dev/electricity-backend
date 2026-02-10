package com.example.electricity_backend.dto;

import org.springframework.lang.NonNull;


public record Price(
    @NonNull String timestamp,
    float value,
    @NonNull String resolutionMinutes
) {}

