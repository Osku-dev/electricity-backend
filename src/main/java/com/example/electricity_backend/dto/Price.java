package com.example.electricity_backend.dto;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// graphql dto
public class Price {
    @NonNull
    private final String timestamp;

    private final float value;

    @NonNull
    private final String resolutionMinutes;

    
}
