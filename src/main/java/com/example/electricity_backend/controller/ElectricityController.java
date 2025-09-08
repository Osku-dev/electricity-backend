package com.example.electricity_backend.controller;

import com.example.electricity_backend.dto.ElectricityPriceDto;
import com.example.electricity_backend.dto.HourlyElectricityPriceDto;
import com.example.electricity_backend.service.ExternalPriceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${FRONTEND_URL}")
@RestController
@RequestMapping("/api/prices")
public class ElectricityController {

    private final ExternalPriceService electricityService;
    

    @Autowired
    public ElectricityController(ExternalPriceService electricityService) {
        this.electricityService = electricityService;
    }

    @GetMapping
    public List<ElectricityPriceDto> getLatestPrices() {
        return electricityService.fetchDailyPrices();
    }

    @GetMapping("/by-hour")
    public ResponseEntity<HourlyElectricityPriceDto> getHourlyPrice(
        @RequestParam String date,
        @RequestParam int hour) {
    
    HourlyElectricityPriceDto price = electricityService.fetchHourlyPrice(date, hour);
    
    if (price != null) {
        return ResponseEntity.ok(price);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(null);
    }
}

@GetMapping("/stats")
public Map<String, Double> getPriceStats() {
    List<ElectricityPriceDto> prices = electricityService.fetchDailyPrices();
    return electricityService.calculateDailyStats(prices);
}

@GetMapping("/cheapest-window")
public List<ElectricityPriceDto> getCheapestChargingWindow(@RequestParam int hours) {
    List<ElectricityPriceDto> prices = electricityService.fetchDailyPrices();
    return electricityService.findCheapestChargingWindow(prices, hours);
}

}
