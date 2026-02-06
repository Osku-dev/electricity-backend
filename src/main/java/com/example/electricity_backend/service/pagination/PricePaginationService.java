package com.example.electricity_backend.service.pagination;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.electricity_backend.connection.CursorUtil;
import com.example.electricity_backend.dto.PaginatedResult;
import com.example.electricity_backend.dto.PricePaginationArgs;
import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.service.price.PriceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PricePaginationService {

    private final PriceService priceService;
    private final CursorUtil cursorUtil;
    private final PricePaginationValidator validator;
    private static final int DEFAULT_PRICE_POINTS_48H_15MIN = 192;

    public PaginatedResult<PriceEntity> fetch(PricePaginationArgs args) {
        
        validator.validate(args);

        if (args.after() != null && args.first() != null) {
            return fetchAfter(args.after(), args.first());
        }

        if (args.before() != null && args.last() != null) {
            return fetchBefore(args.before(), args.last());
        }

        if (args.first() != null) {
            return fetchFirst(args.first());
        }

        if (args.last() != null) {
            return fetchLast(args.last());
        }

        return fetchDefault();
    }

    private PaginatedResult<PriceEntity> fetchAfter(String after, Integer first) {
        LocalDateTime afterTime = LocalDateTime.parse(cursorUtil.decodeCursor(after));

        List<PriceEntity> entities = priceService.getPricesAfter(afterTime, first + 1);

        boolean hasNextPage = entities.size() > first;
        boolean hasPreviousPage = true;

        if (hasNextPage) {
            entities = entities.subList(0, first);
        }
        

        return new PaginatedResult<>(entities, hasNextPage, hasPreviousPage);
    }

    private PaginatedResult<PriceEntity> fetchBefore(String before, Integer last) {
        LocalDateTime beforeTime = LocalDateTime.parse(cursorUtil.decodeCursor(before));

        List<PriceEntity> entities = priceService.getPricesBefore(beforeTime, last + 1);

        boolean hasPreviousPage = entities.size() > last;
        boolean hasNextPage = true;
        if (hasPreviousPage) {
            entities = entities.subList(0, last);
        }
        

        return new PaginatedResult<>(entities, hasNextPage, hasPreviousPage);
    }

    private PaginatedResult<PriceEntity> fetchFirst(Integer first) {
        List<PriceEntity> entities = priceService.getOldest(first + 1);

        boolean hasNextPage = entities.size() > first;
        boolean hasPreviousPage = false;
        if (hasNextPage) {
            entities = entities.subList(0, first);
        }
        

        return new PaginatedResult<>(entities, hasNextPage, hasPreviousPage);
    }


    private PaginatedResult<PriceEntity> fetchLast(Integer last) {
        List<PriceEntity> entities = priceService.getNewest(last + 1);

        boolean hasPreviousPage = entities.size() > last;
        boolean hasNextPage = false;
        if (hasPreviousPage) {
            entities = entities.subList(0, last);
        }
        

        return new PaginatedResult<>(entities, hasNextPage, hasPreviousPage);
    }

    private PaginatedResult<PriceEntity> fetchDefault() {
        List<PriceEntity> entities = priceService.getNewest(DEFAULT_PRICE_POINTS_48H_15MIN + 1);

        boolean hasPreviousPage = entities.size() > DEFAULT_PRICE_POINTS_48H_15MIN;
        boolean hasNextPage = false;
        if (hasPreviousPage) {
            entities = entities.subList(0, DEFAULT_PRICE_POINTS_48H_15MIN);
        }
        

        return new PaginatedResult<>(entities, hasNextPage, hasPreviousPage);
    }
}