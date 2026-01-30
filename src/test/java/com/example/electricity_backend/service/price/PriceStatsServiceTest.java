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

        assertThat(stats.getMinPrice()).isEqualTo(0.00f);
        assertThat(stats.getMaxPrice()).isEqualTo(0.00f);
        assertThat(stats.getAvgPrice()).isEqualTo(0.00f);
    }

    @Test
    void computesStatsForSinglePrice() {
        Stats stats = service.computeStats(List.of(price(10.123f)));

        assertThat(stats.getMinPrice()).isEqualTo(10.12f);
        assertThat(stats.getMaxPrice()).isEqualTo(10.12f);
        assertThat(stats.getAvgPrice()).isEqualTo(10.12f);
    }

    @Test
    void computesStatsForMultiplePrices() {
        Stats stats = service.computeStats(List.of(
            price(10.0f),
            price(20.0f),
            price(30.0f)
        ));

        assertThat(stats.getMinPrice()).isEqualTo(10.00f);
        assertThat(stats.getMaxPrice()).isEqualTo(30.00f);
        assertThat(stats.getAvgPrice()).isEqualTo(20.00f);
    }

    @Test
    void roundsCorrectlyToTwoDecimals() {
        Stats stats = service.computeStats(List.of(
            price(10.111f),
            price(10.119f)
        ));

        assertThat(stats.getMinPrice()).isEqualTo(10.11f);
        assertThat(stats.getMaxPrice()).isEqualTo(10.12f);
        assertThat(stats.getAvgPrice()).isEqualTo(10.12f);
    }

    private Price price(float value) {
        return new Price(
            FIXED_TIMESTAMP,
            value,
            RESOLUTION_15_MIN
        );
    }
}
