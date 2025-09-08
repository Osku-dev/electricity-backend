package com.example.electricity_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.electricity_backend.dto.ElectricityPriceDto;
import com.example.electricity_backend.dto.ElectricityPriceResponseDto;
import com.example.electricity_backend.dto.HourlyElectricityPriceDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ExternalPriceService {

    private final RestTemplate restTemplate;
    @Value("${DAILY_PRICES_URL}")
    private String DAILY_PRICES_URL;
    @Value("${HOURLY_PRICE_URL}")
    private String HOURLY_PRICE_URL;

    @Autowired
    public ExternalPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
   @SuppressWarnings("null")
   public List<ElectricityPriceDto> fetchDailyPrices() {
    try {
        ResponseEntity<ElectricityPriceResponseDto> response = restTemplate.exchange(
            DAILY_PRICES_URL,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ElectricityPriceResponseDto>() {}
        );

        if (response.getBody() != null && response.getBody().getPrices() != null) {
            return response.getBody().getPrices();
        } else {
            System.err.println("Error: The response body or prices are null.");
            return Collections.emptyList();
        }
    } catch (Exception e) {
        System.err.println("Error occurred while fetching electricity data: " + e.getMessage());
        e.printStackTrace();
        return Collections.emptyList();
    }
}

@SuppressWarnings("null")
public HourlyElectricityPriceDto fetchHourlyPrice(String date, int hour) {
    try {
        ResponseEntity<HourlyElectricityPriceDto> response = restTemplate.exchange(
            HOURLY_PRICE_URL,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<HourlyElectricityPriceDto>() {},
            date,
            hour
        );

        if (response.getBody() != null && response.getBody().getPrice() != null) {
            return response.getBody();
        } else {
            System.err.println("Error: Response body is null or price is missing (possibly due to DST).");
            return null;
        }
    } catch (Exception e) {
        System.err.println("Error occurred while fetching hourly electricity price: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

public Map<String, Double> calculateDailyStats(List<ElectricityPriceDto> prices) {
    if (prices == null || prices.isEmpty()) {
        return Map.of("min", 0.0, "max", 0.0, "average", 0.0);
    }

    double min = prices.stream().mapToDouble(ElectricityPriceDto::getPrice).min().orElse(0.0);
    double max = prices.stream().mapToDouble(ElectricityPriceDto::getPrice).max().orElse(0.0);
    double avg = prices.stream().mapToDouble(ElectricityPriceDto::getPrice).average().orElse(0.0);

    return Map.of(
        "min", min,
        "max", max,
        "average", avg
    );
}

public List<ElectricityPriceDto> findCheapestChargingWindow(List<ElectricityPriceDto> prices, int hours) {
    if (prices == null || prices.size() < hours) {
        return Collections.emptyList();
    }

    List<ElectricityPriceDto> cheapestWindow = new ArrayList<>();
    double minSum = Double.MAX_VALUE;

    for (int i = 0; i <= prices.size() - hours; i++) {
        List<ElectricityPriceDto> window = prices.subList(i, i + hours);
        double sum = window.stream().mapToDouble(ElectricityPriceDto::getPrice).sum();

        if (sum < minSum) {
            minSum = sum;
            cheapestWindow = new ArrayList<>(window);
        }
    }

    return cheapestWindow;
}


}
