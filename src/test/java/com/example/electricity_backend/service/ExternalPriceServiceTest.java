package com.example.electricity_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.electricity_backend.dto.ElectricityPriceDto;
import com.example.electricity_backend.dto.ElectricityPriceResponseDto;
import com.example.electricity_backend.service.price.ExternalPriceService;

class ExternalPriceServiceTest {

    private RestTemplate restTemplate;
    private ExternalPriceService service;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        service = new ExternalPriceService(restTemplate, "http://test-url");

    }

    @Test
    void returnsReversedPricesWhenApiSucceeds() {
        ElectricityPriceDto p1 = new ElectricityPriceDto("2026-01-28T22:30:00.000Z",13.873 );
        ElectricityPriceDto p2 = new ElectricityPriceDto("2026-01-28T22:45:00.000Z",13.053);

        ElectricityPriceResponseDto body = new ElectricityPriceResponseDto((List.of(p1, p2)));

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                ArgumentMatchers.<ParameterizedTypeReference<ElectricityPriceResponseDto>>any()
        )).thenReturn(ResponseEntity.ok(body));

        List<ElectricityPriceDto> result = service.fetchDailyPrices();

        assertEquals(2, result.size());
        assertSame(p2, result.get(0));
        assertSame(p1, result.get(1));
    }

    @Test
    void returnsEmptyListWhenRestTemplateThrows() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                ArgumentMatchers.<ParameterizedTypeReference<ElectricityPriceResponseDto>>any()
        )).thenThrow(new RestClientException("boom"));

        List<ElectricityPriceDto> result = service.fetchDailyPrices();

        assertTrue(result.isEmpty());
    }

    @Test
    void returnsEmptyListWhenBodyIsNull() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                ArgumentMatchers.<ParameterizedTypeReference<ElectricityPriceResponseDto>>any()
        )).thenReturn(ResponseEntity.ok(null));

        List<ElectricityPriceDto> result = service.fetchDailyPrices();

        assertTrue(result.isEmpty());
    }
}
