package com.example.electricity_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.electricity_backend.dto.ElectricityPriceDto;
import com.example.electricity_backend.dto.ElectricityPriceResponseDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExternalPriceService {

    private static final Logger log =
        LoggerFactory.getLogger(ExternalPriceService.class);

    private final RestTemplate restTemplate;
    
    private String DAILY_PRICES_URL;

    @Autowired
    public ExternalPriceService(RestTemplate restTemplate, @Value("${DAILY_PRICES_URL}") String dailyPricesUrl) {
        this.restTemplate = restTemplate;
        this.DAILY_PRICES_URL = dailyPricesUrl;
    }
    
   public List<ElectricityPriceDto> fetchDailyPrices() {
    ResponseEntity<ElectricityPriceResponseDto> response;

    try {
        response = restTemplate.exchange(
            DAILY_PRICES_URL,
            HttpMethod.GET,
            HttpEntity.EMPTY,
            new ParameterizedTypeReference<ElectricityPriceResponseDto>() {}
        );
    } catch (RestClientException e) {
        log.error("Failed to fetch daily electricity prices", e);
        return Collections.emptyList();
    }

    ElectricityPriceResponseDto body = response.getBody();
    if (body == null || body.getPrices() == null) {
        log.warn("Electricity price API returned empty body or prices");
        return Collections.emptyList();
    }

    List<ElectricityPriceDto> prices = new ArrayList<>(body.getPrices());
    Collections.reverse(prices);
    return prices;
}

}
