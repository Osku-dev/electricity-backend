package com.example.electricity_backend.repository;

import com.example.electricity_backend.model.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    // Find all prices in a time range
    List<PriceEntity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Find all prices for a given resolution (e.g., 1h, 15min)
    List<PriceEntity> findByResolutionMinutes(int resolutionMinutes);
}
