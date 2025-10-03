package com.example.electricity_backend.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.electricity_backend.mapper.PriceMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.example.electricity_backend.dto.ElectricityPriceDto;

// Service to sync electricity prices from an external API into the local database

@Service
@RequiredArgsConstructor
public class ElectricityPriceSyncService {

    private final JdbcTemplate jdbcTemplate;
    private final ExternalPriceService externalPriceService;

    @Transactional
    public void syncNewBatch() {
        List<ElectricityPriceDto> dtos = externalPriceService.fetchDailyPrices();
        if (dtos.isEmpty()) return;

        String sql = """
            INSERT INTO prices (start_time, price_cents, resolution_minutes)
            VALUES (?, ?, ?)
            ON CONFLICT (start_time, resolution_minutes) DO NOTHING
            """;

        List<Object[]> batchArgs = dtos.stream()
            .map(dto -> PriceMapper.toEntity(dto, 15))
            .map(entity -> new Object[] {
                entity.getStartTime(),
                entity.getPrice(),
                entity.getResolutionMinutes()
            })
            .toList();

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}