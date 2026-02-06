package com.example.electricity_backend.service.pagination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.electricity_backend.connection.CursorUtil;
import com.example.electricity_backend.dto.PricePaginationArgs;
import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.service.price.PriceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricePaginationServiceTest {

    @Mock
    private PriceService priceService;

    @Mock
    private CursorUtil cursorUtil;

    @Mock
    private PricePaginationValidator validator;

    @InjectMocks
    private PricePaginationService service;

    private static final int DEFAULT_PRICE_POINTS_48H_15MIN = 192;
    private static final String FIXED_TIMESTAMP = "2026-01-01T00:00";
    

    private List<PriceEntity> prices(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new PriceEntity())
                .toList();
    }

    @Test
    void fetchAfterAndFirst() {
        var args = new PricePaginationArgs(10, "cursor", null, null);

        when(cursorUtil.decodeCursor("cursor"))
                .thenReturn(FIXED_TIMESTAMP);

        when(priceService.getPricesAfter(any(LocalDateTime.class), eq(11)))
                .thenReturn(prices(11));

        var result = service.fetch(args);

        verify(validator).validate(args);
        verify(cursorUtil).decodeCursor("cursor");
        verify(priceService).getPricesAfter(any(LocalDateTime.class), eq(11));
        verifyNoMoreInteractions(priceService, cursorUtil, validator);

        assertTrue(result.hasNextPage());
        assertTrue(result.hasPreviousPage());
        assertEquals(10, result.items().size());
    }

    @Test
    void fetchBeforeAndLast() {
        var args = new PricePaginationArgs(null, null, 5, "cursor");

        when(cursorUtil.decodeCursor("cursor"))
                .thenReturn(FIXED_TIMESTAMP);

        when(priceService.getPricesBefore(any(LocalDateTime.class), eq(6)))
                .thenReturn(prices(6));

        var result = service.fetch(args);

        verify(validator).validate(args);
        verify(cursorUtil).decodeCursor("cursor");
        verify(priceService).getPricesBefore(any(LocalDateTime.class), eq(6));
        verifyNoMoreInteractions(priceService, cursorUtil, validator);

        assertTrue(result.hasPreviousPage());
        assertTrue(result.hasNextPage());
        assertEquals(5, result.items().size());
    }

    @Test
    void fetchFirstOnly() {
        var args = new PricePaginationArgs(3, null, null, null);

        when(priceService.getOldest(4))
                .thenReturn(prices(4));

        var result = service.fetch(args);

        verify(validator).validate(args);
        verify(priceService).getOldest(4);
        verifyNoMoreInteractions(priceService, cursorUtil, validator);

        assertTrue(result.hasNextPage());
        assertFalse(result.hasPreviousPage());
        assertEquals(3, result.items().size());
    }

    @Test
    void fetchLastOnly() {
        var args = new PricePaginationArgs(null, null, 3, null);

        when(priceService.getNewest(4))
                .thenReturn(prices(4));

        var result = service.fetch(args);

        verify(validator).validate(args);
        verify(priceService).getNewest(4);
        verifyNoMoreInteractions(priceService, cursorUtil, validator);

        assertFalse(result.hasNextPage());
        assertTrue(result.hasPreviousPage());
        assertEquals(3, result.items().size());
    }

    @Test
    void fetchDefault() {
        var args = new PricePaginationArgs(null, null, null, null);

        when(priceService.getNewest(DEFAULT_PRICE_POINTS_48H_15MIN + 1))
                .thenReturn(prices(DEFAULT_PRICE_POINTS_48H_15MIN + 1));

        var result = service.fetch(args);

        verify(validator).validate(args);
        verify(priceService).getNewest(DEFAULT_PRICE_POINTS_48H_15MIN + 1);
        verifyNoMoreInteractions(priceService, cursorUtil, validator);

        assertFalse(result.hasNextPage());
        assertTrue(result.hasPreviousPage());
        assertEquals(192, result.items().size());
    }

}
