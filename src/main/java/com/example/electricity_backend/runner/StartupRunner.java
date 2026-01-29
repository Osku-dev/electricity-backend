package com.example.electricity_backend.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.electricity_backend.service.price.ElectricityPriceSyncService;

// Runner to execute the sync service at application startup

@Component
public class StartupRunner implements CommandLineRunner {

    private final ElectricityPriceSyncService syncService;

    public StartupRunner(ElectricityPriceSyncService syncService) {
        this.syncService = syncService;
    }

    @Override
    public void run(String... args) {
        syncService.syncNewBatch();
    }
}
