package com.example.electricity_backend.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.electricity_backend.service.price.ExternalPriceService;
import com.example.electricity_backend.service.price.PriceService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {

    private static final Logger log =
        LoggerFactory.getLogger(ExternalPriceService.class);

    private final PriceService priceService;

    @Scheduled(cron = "0 */2 14-15 * * *", zone = "Europe/Helsinki")
    public void fetchPricesIfNeeded() {
        log.info("Starting electricity price update job");

        priceService.fetchTomorrowPricesIfMissing();

        log.info("Electricity price update job finished");
    }
}
