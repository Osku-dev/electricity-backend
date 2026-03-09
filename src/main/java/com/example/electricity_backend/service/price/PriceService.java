package com.example.electricity_backend.service.price;

import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.repository.PriceRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor

public class PriceService {

    private final PriceRepository priceRepository;
    private final ElectricityPriceSyncService syncService;

    public void fetchTomorrowPricesIfMissing() {

        LocalDate tomorrow = LocalDate.now(ZoneId.of("Europe/Helsinki")).plusDays(1);

        boolean alreadyExists = priceRepository.existsByStartTimeBetween(
                tomorrow.atStartOfDay(),
                tomorrow.plusDays(1).atStartOfDay()
        );

        if (alreadyExists) {
            return;
        }

        syncService.syncNewBatch();
    }

    // +1 for pageInfo hasNextPage/hasPreviousPage checks
    public List<PriceEntity> getPricesAfter(LocalDateTime after, int limit) {
        return priceRepository.findByStartTimeAfterOrderByStartTimeAsc(
                after,
                PageRequest.of(0, limit + 1)
        );
    }

    public List<PriceEntity> getPricesBefore(LocalDateTime before, int limit) {
        List<PriceEntity> entities = new ArrayList<>( priceRepository.findByStartTimeBeforeOrderByStartTimeDesc(
                before,
                PageRequest.of(0, limit + 1)
        ));
        Collections.reverse(entities); // for Relay ASC order
        return entities;
    }

    public List<PriceEntity> getNewest(int limit) {
        List<PriceEntity> entities = new ArrayList<>( priceRepository.findAllByOrderByStartTimeDesc(
                PageRequest.of(0, limit + 1)
        ));
        Collections.reverse(entities); // ASC order
        return entities;
    }

    public List<PriceEntity> getOldest(int limit) {
        return priceRepository.findAllByOrderByStartTimeAsc(PageRequest.of(0, limit + 1));
    }
}
