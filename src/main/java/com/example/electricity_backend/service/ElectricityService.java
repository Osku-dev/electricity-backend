package com.example.electricity_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.electricity_backend.model.ElectricityPrice;
import com.example.electricity_backend.model.ElectricityPriceResponse;
import com.example.electricity_backend.model.HourlyElectricityPrice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ElectricityService {

    private final RestTemplate restTemplate;
    private final String DAILY_PRICES_URL = "https://api.porssisahko.net/v1/latest-prices.json";
    private final String HOURLY_PRICE_URL = "https://api.porssisahko.net/v1/price.json?date={date}&hour={hour}";

    @Autowired
    public ElectricityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
   @SuppressWarnings("null")
   public List<ElectricityPrice> fetchElectricityData() {
    try {
        ResponseEntity<ElectricityPriceResponse> response = restTemplate.exchange(
            DAILY_PRICES_URL,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ElectricityPriceResponse>() {}
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
public HourlyElectricityPrice fetchHourlyPrice(String date, int hour) {
    try {
        ResponseEntity<HourlyElectricityPrice> response = restTemplate.exchange(
            HOURLY_PRICE_URL,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<HourlyElectricityPrice>() {},
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

public Map<String, Double> calculateDailyStats(List<ElectricityPrice> prices) {
    if (prices == null || prices.isEmpty()) {
        return Map.of("min", 0.0, "max", 0.0, "average", 0.0);
    }

    double min = prices.stream().mapToDouble(ElectricityPrice::getPrice).min().orElse(0.0);
    double max = prices.stream().mapToDouble(ElectricityPrice::getPrice).max().orElse(0.0);
    double avg = prices.stream().mapToDouble(ElectricityPrice::getPrice).average().orElse(0.0);

    return Map.of(
        "min", min,
        "max", max,
        "average", avg
    );
}

public List<ElectricityPrice> findCheapestChargingWindow(List<ElectricityPrice> prices, int hours) {
    if (prices == null || prices.size() < hours) {
        return Collections.emptyList();
    }

    List<ElectricityPrice> cheapestWindow = new ArrayList<>();
    double minSum = Double.MAX_VALUE;

    for (int i = 0; i <= prices.size() - hours; i++) {
        List<ElectricityPrice> window = prices.subList(i, i + hours);
        double sum = window.stream().mapToDouble(ElectricityPrice::getPrice).sum();

        if (sum < minSum) {
            minSum = sum;
            cheapestWindow = new ArrayList<>(window);
        }
    }

    return cheapestWindow;
}


}
