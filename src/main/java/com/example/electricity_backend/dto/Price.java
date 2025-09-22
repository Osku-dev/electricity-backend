package com.example.electricity_backend.dto;

import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
// graphql dto
public class Price {
    @NonNull
    private String timestamp;

    private float value;

    @NonNull
    private String resolutionMinutes;

    
}
