package com.example.electricity_backend.repository;

import com.example.electricity_backend.model.PriceEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    // Find all prices in a time range
    List<PriceEntity> findByStartTimeAfter(LocalDateTime start);

    // Find all prices for a given resolution (e.g., 1h, 15min)
    List<PriceEntity> findByResolutionMinutes(int resolutionMinutes);

    // Forward pagination (after + first)
    List<PriceEntity> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime after, Pageable pageable);

    // Backward pagination (before + last)
    List<PriceEntity> findByStartTimeBeforeOrderByStartTimeDesc(LocalDateTime before, Pageable pageable);

    // Default: get newest first N (for last)
    List<PriceEntity> findAllByOrderByStartTimeDesc(Pageable pageable);

    // Default: get oldest first N (for first)
    List<PriceEntity> findAllByOrderByStartTimeAsc(Pageable pageable);


}
