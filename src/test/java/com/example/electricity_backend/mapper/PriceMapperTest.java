package com.example.electricity_backend.mapper;

import com.example.electricity_backend.dto.ElectricityPriceDto;
import com.example.electricity_backend.dto.Price;
import com.example.electricity_backend.model.PriceEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PriceMapperTest {

    @Test
    void toEntity_convertsApiDtoCorrectly() {
        ElectricityPriceDto dto = new ElectricityPriceDto("2024-01-01T12:00:00.000Z", 123.45);

        int resolutionMinutes = 15;

        PriceEntity entity = PriceMapper.toEntity(dto, resolutionMinutes);

        assertEquals(
                LocalDateTime.of(2024, 1, 1, 12, 0),
                entity.getStartTime()
        );
        assertEquals(
                BigDecimal.valueOf(123.45),
                entity.getPrice()
        );
        assertEquals(
                resolutionMinutes,
                entity.getResolutionMinutes()
        );
    }

    @Test
    void fromEntity_convertsEntityCorrectly() {
        PriceEntity entity = new PriceEntity();
        entity.setStartTime(LocalDateTime.of(2024, 1, 1, 12, 0));
        entity.setPrice(BigDecimal.valueOf(99.99));
        entity.setResolutionMinutes(15);

        Price price = PriceMapper.fromEntity(entity);

        assertEquals("2024-01-01T12:00", price.getTimestamp());
        assertEquals(99.99f, price.getValue(), 0.0001f);
        assertEquals("15", price.getResolutionMinutes());
    }
}
