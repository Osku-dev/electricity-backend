package com.example.electricity_backend.mapper;

import com.example.electricity_backend.dto.ElectricityPriceDto;
import com.example.electricity_backend.model.PriceEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceMapper {

    private PriceMapper() {
    
    }

    /**
     * Convert API DTO → JPA Entity.
     */
    public static PriceEntity toEntity(ElectricityPriceDto dto, int resolution) {
        PriceEntity entity = new PriceEntity();
        entity.setStartTime(LocalDateTime.parse(dto.getStartDate()));
        entity.setPrice(BigDecimal.valueOf(dto.getPrice()));
        entity.setResolutionMinutes(resolution);
        return entity;
    }

}
