package com.example.electricity_backend.service;

import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    /**
     * Save a single price entry to the database.
     */
    public PriceEntity savePrice(PriceEntity priceEntity) {
        return priceRepository.save(priceEntity);
    }

    /**
     * Save multiple price entries in batch.
     */
    public List<PriceEntity> saveAll(List<PriceEntity> prices) {
        return priceRepository.saveAll(prices);
    }

    /**
     * Fetch all prices from the database.
     */
    public List<PriceEntity> getAllPrices() {
        return priceRepository.findAll();
    }

    /**
     * Fetch prices within a given date/time range.
     */
    public List<PriceEntity> getPricesBetween(LocalDateTime start, LocalDateTime end) {
        return priceRepository.findByStartTimeBetween(start, end);
    }

    /**
     * Fetch prices for a specific resolution (e.g., 1 hour or 15 min intervals).
     */
    public List<PriceEntity> getPricesByResolution(int resolution) {
        return priceRepository.findByResolutionMinutes(resolution);
    }

    /**
     * Delete a price entry by ID.
     */
    public void deletePrice(Long id) {
        priceRepository.deleteById(id);
    }

    /**
     * Delete all price entries.
     */
    public void deleteAllPrices() {
        priceRepository.deleteAll();
    }
}
