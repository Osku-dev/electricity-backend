package com.example.electricity_backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.electricity_backend.service.price.PriceService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {

    private final PriceService priceService;

    @Scheduled(cron = "0 */2 14-15 * * *", zone = "Europe/Helsinki")
    public void fetchPricesIfNeeded() {
        priceService.fetchTomorrowPricesIfMissing();
    }
}
