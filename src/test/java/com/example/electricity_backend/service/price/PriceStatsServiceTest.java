package com.example.electricity_backend.service.price;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.electricity_backend.dto.Price;
import com.example.electricity_backend.dto.Stats;

class PriceStatsServiceTest {

    private PriceStatsService service;
    private static final String FIXED_TIMESTAMP = "2026-01-01T00:00";
    private static final String RESOLUTION_15_MIN = "15";

    @BeforeEach
    void setUp() {
        service = new PriceStatsService();
    }

    @Test
    void returnsZerosForEmptyList() {
        Stats stats = service.computeStats(List.of());

        assertThat(stats.minPrice()).isEqualTo(0.00);
        assertThat(stats.maxPrice()).isEqualTo(0.00);
        assertThat(stats.avgPrice()).isEqualTo(0.00);
    }

    @Test
    void computesStatsForSinglePrice() {
        Stats stats = service.computeStats(List.of(price(10.123)));

        assertThat(stats.minPrice()).isEqualTo(10.12);
        assertThat(stats.maxPrice()).isEqualTo(10.12);
        assertThat(stats.avgPrice()).isEqualTo(10.12);
    }

    @Test
    void computesStatsForMultiplePrices() {
        Stats stats = service.computeStats(List.of(
            price(10.0),
            price(20.0),
            price(30.0)
        ));

        assertThat(stats.minPrice()).isEqualTo(10.00);
        assertThat(stats.maxPrice()).isEqualTo(30.00);
        assertThat(stats.avgPrice()).isEqualTo(20.00);
    }

    @Test
    void roundsCorrectlyToTwoDecimals() {
        Stats stats = service.computeStats(List.of(
            price(10.111),
            price(10.119)
        ));

        assertThat(stats.minPrice()).isEqualTo(10.11);
        assertThat(stats.maxPrice()).isEqualTo(10.12);
        assertThat(stats.avgPrice()).isEqualTo(10.12);
    }

    private Price price(double value) {
        return new Price(
            FIXED_TIMESTAMP,
            value,
            RESOLUTION_15_MIN
        );
    }
}