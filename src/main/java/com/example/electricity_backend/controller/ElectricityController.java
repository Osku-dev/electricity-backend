package com.example.electricity_backend.controller;

import com.example.electricity_backend.model.ElectricityPrice;
import com.example.electricity_backend.model.HourlyElectricityPrice;
import com.example.electricity_backend.service.ElectricityService;

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

    private final ElectricityService electricityService;
    

    @Autowired
    public ElectricityController(ElectricityService electricityService) {
        this.electricityService = electricityService;
    }

    @GetMapping
    public List<ElectricityPrice> getLatestPrices() {
        return electricityService.fetchElectricityData();
    }

    @GetMapping("/by-hour")
    public ResponseEntity<HourlyElectricityPrice> getHourlyPrice(
        @RequestParam String date,
        @RequestParam int hour) {
    
    HourlyElectricityPrice price = electricityService.fetchHourlyPrice(date, hour);
    
    if (price != null) {
        return ResponseEntity.ok(price);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(null);
    }
}

@GetMapping("/stats")
public Map<String, Double> getPriceStats() {
    List<ElectricityPrice> prices = electricityService.fetchElectricityData();
    return electricityService.calculateDailyStats(prices);
}

@GetMapping("/cheapest-window")
public List<ElectricityPrice> getCheapestChargingWindow(@RequestParam int hours) {
    List<ElectricityPrice> prices = electricityService.fetchElectricityData();
    return electricityService.findCheapestChargingWindow(prices, hours);
}

}
