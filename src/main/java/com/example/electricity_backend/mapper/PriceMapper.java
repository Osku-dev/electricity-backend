package com.example.electricity_backend.mapper;

import com.example.electricity_backend.dto.ElectricityPriceDto;
import com.example.electricity_backend.dto.Price;
import com.example.electricity_backend.model.PriceEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceMapper {

    private PriceMapper() {
    
    }

    /**
     * Convert API DTO → JPA Entity.
     */
    public static PriceEntity toEntity(ElectricityPriceDto dto, int resolutionMinutes) {
        String raw = dto.getStartDate();
        String cleaned = raw.replace("Z", "").replace(".000", "");

        PriceEntity entity = new PriceEntity();
        entity.setStartTime(LocalDateTime.parse(cleaned));
        entity.setPrice(BigDecimal.valueOf(dto.getPrice()));
        entity.setResolutionMinutes(resolutionMinutes);
        return entity;
    }

    /**
     * Convert JPA Entity → GraphQL DTO.
     */
    public static Price fromEntity(PriceEntity entity) {
    Price dto = new Price();
    dto.setTimestamp(entity.getStartTime().toString()); 
    dto.setValue(entity.getPrice().floatValue());
    dto.setResolutionMinutes(Integer.toString(entity.getResolutionMinutes()));
    return dto;
}


}
