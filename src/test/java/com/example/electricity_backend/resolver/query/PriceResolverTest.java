package com.example.electricity_backend.resolver.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.electricity_backend.connection.CursorUtil;
import com.example.electricity_backend.dto.PaginatedResult;
import com.example.electricity_backend.dto.Price;
import com.example.electricity_backend.dto.PriceConnectionWithStats;
import com.example.electricity_backend.dto.Stats;
import com.example.electricity_backend.mapper.PriceMapper;
import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.service.pagination.PricePaginationService;
import com.example.electricity_backend.service.price.PriceStatsService;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;

@ExtendWith(MockitoExtension.class)
class PriceResolverTest {

    @Mock
    private PricePaginationService paginationService;

    @Mock
    private CursorUtil cursorUtil;

    @Mock
    private PriceStatsService priceStatsService;

    @InjectMocks
     private PriceResolver resolver;

    @Test
    void pricesReturnsConnectionWithStats() {
        PriceEntity entity = new PriceEntity();
        entity.setStartTime(LocalDateTime.of(2024, 1, 1, 12, 0));
        entity.setPrice(BigDecimal.valueOf(99.99));
        entity.setResolutionMinutes(15);

        ConnectionCursor cursor = new DefaultConnectionCursor("cursor-1");

        var page = new PaginatedResult<>(
                List.of(entity),
                false,
                true
        );

        when(paginationService.fetch(any()))
                .thenReturn(page);

        Price mapped = PriceMapper.fromEntity(entity);

        when(cursorUtil.createCursorWith(any()))
                .thenReturn(cursor);

        when(cursorUtil.getFirstCursorFrom(any()))
                .thenReturn(cursor);

        when(cursorUtil.getLastCursorFrom(any()))
                .thenReturn(cursor);

        Stats stats = new Stats(null, null, null);
        when(priceStatsService.computeStats(any()))
                .thenReturn(stats);

        PriceConnectionWithStats result =
                resolver.prices(10, null, null, null);

        assertEquals(1, result.getEdges().size());
        assertEquals(mapped, result.getEdges().get(0).getNode());
        assertEquals(cursor, result.getEdges().get(0).getCursor());

        assertEquals(stats, result.getStats());
        assertFalse(result.getPageInfo().isHasNextPage());
        assertTrue(result.getPageInfo().isHasPreviousPage());
    }
}

