package com.example.electricity_backend.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// graphql dto
public class Price {
    @Column(nullable = false)
    private String timestamp;

    @Column(nullable = false)
    private float value;

    @Column(nullable = false)
    private String resolutionMinutes;

    
}
