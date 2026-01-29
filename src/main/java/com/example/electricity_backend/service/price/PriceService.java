package com.example.electricity_backend.service.price;

import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.repository.PriceRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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
    public List<PriceEntity> getPricesBetween(LocalDateTime start) {
        return priceRepository.findByStartTimeAfter(start);
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
    // +1 for pageInfo hasNextPage/hasPreviousPage checks
    // Forward pagination
    public List<PriceEntity> getPricesAfter(LocalDateTime after, int limit) {
        return priceRepository.findByStartTimeAfterOrderByStartTimeAsc(
                after,
                PageRequest.of(0, limit + 1)
        );
    }

    // Backward pagination
    public List<PriceEntity> getPricesBefore(LocalDateTime before, int limit) {
        List<PriceEntity> entities = priceRepository.findByStartTimeBeforeOrderByStartTimeDesc(
                before,
                PageRequest.of(0, limit + 1)
        );
        Collections.reverse(entities); // for Relay ASC order
        return entities;
    }

    // Default newest N
    public List<PriceEntity> getNewest(int limit) {
        List<PriceEntity> entities = priceRepository.findAllByOrderByStartTimeDesc(
                PageRequest.of(0, limit + 1)
        );
        Collections.reverse(entities); // ASC order
        return entities;
    }

    // Default oldest N
    public List<PriceEntity> getOldest(int limit) {
        return priceRepository.findAllByOrderByStartTimeAsc(PageRequest.of(0, limit + 1));
    }
}
