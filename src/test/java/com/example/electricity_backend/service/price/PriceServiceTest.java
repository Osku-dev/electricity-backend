package com.example.electricity_backend.service.price;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.repository.PriceRepository;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    private PriceService priceService;

    @BeforeEach
    void setUp() {
        priceService = new PriceService(priceRepository);
    }

    @Test
    void getPricesAfter_callsRepositoryWithLimitPlusOne() {
        LocalDateTime after = LocalDateTime.now();
        int limit = 10;

        List<PriceEntity> mockResult = List.of(
                new PriceEntity(),
                new PriceEntity()
        );

        when(priceRepository.findByStartTimeAfterOrderByStartTimeAsc(
                eq(after),
                eq(PageRequest.of(0, limit + 1))
        )).thenReturn(mockResult);

        List<PriceEntity> result =
                priceService.getPricesAfter(after, limit);

        assertEquals(mockResult, result);

        verify(priceRepository).findByStartTimeAfterOrderByStartTimeAsc(
                eq(after),
                eq(PageRequest.of(0, limit + 1))
        );
    }

    @Test
    void getPricesBefore_reversesResultOrder() {
        LocalDateTime before = LocalDateTime.now();
        int limit = 2;

        PriceEntity first = new PriceEntity();
        PriceEntity second = new PriceEntity();

        // Repository returns DESC
        when(priceRepository.findByStartTimeBeforeOrderByStartTimeDesc(
                eq(before),
                eq(PageRequest.of(0, limit + 1))
        )).thenReturn(List.of(first, second));

        List<PriceEntity> result =
                priceService.getPricesBefore(before, limit);

        // Service reverses to ASC
        assertEquals(second, result.get(0));
        assertEquals(first, result.get(1));
    }

    @Test
    void getNewest_reversesDescendingOrder() {
        int limit = 3;

        PriceEntity newest = new PriceEntity();
        PriceEntity older = new PriceEntity();

        when(priceRepository.findAllByOrderByStartTimeDesc(
                eq(PageRequest.of(0, limit + 1))
        )).thenReturn(List.of(newest, older));

        List<PriceEntity> result =
                priceService.getNewest(limit);

        assertEquals(older, result.get(0));
        assertEquals(newest, result.get(1));
    }

    @Test
    void getOldest_returnsRepositoryResultAsIs() {
        int limit = 5;

        List<PriceEntity> entities = List.of(
                new PriceEntity(),
                new PriceEntity()
        );

        when(priceRepository.findAllByOrderByStartTimeAsc(
                eq(PageRequest.of(0, limit + 1))
        )).thenReturn(entities);

        List<PriceEntity> result =
                priceService.getOldest(limit);

        assertEquals(entities, result);
    }
}