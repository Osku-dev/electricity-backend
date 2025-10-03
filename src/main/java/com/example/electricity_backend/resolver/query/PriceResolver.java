package com.example.electricity_backend.resolver.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.electricity_backend.connection.CursorUtil;
import com.example.electricity_backend.dto.Price;
import com.example.electricity_backend.mapper.PriceMapper;
import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.service.PriceService;

import graphql.relay.Connection;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PriceResolver{

    private final PriceService priceService;
    private final CursorUtil cursorUtil;


@QueryMapping
    public Connection<Price> prices(
            @Argument Integer first,
            @Argument String after,
            @Argument Integer last,
            @Argument String before
    ) {
        List<PriceEntity> entities;
        boolean hasNextPage = false;
        boolean hasPreviousPage = false;

        validatePaginationArgs(first, last, after, before);

        if (after != null && first != null) {
            LocalDateTime afterTime = LocalDateTime.parse(cursorUtil.decodeCursor(after));
            entities = priceService.getPricesAfter(afterTime, first);

            if (entities.size() > first) {
            hasNextPage = true;
            entities = entities.subList(0, first);
        }
        
        hasPreviousPage = after != null;


        } else if (before != null && last != null) {
            LocalDateTime beforeTime = LocalDateTime.parse(cursorUtil.decodeCursor(before));
            entities = priceService.getPricesBefore(beforeTime, last);

            if (entities.size() > last) {
            hasPreviousPage = true;
            entities = entities.subList(0, last);
        }
        
        hasNextPage = before != null;

        } else if (first != null) {
            entities = priceService.getOldest(first);

            if (entities.size() > first) {
            hasNextPage = true;
            entities = entities.subList(0, first);
        }

        } else if (last != null) {
            entities = priceService.getNewest(last);

             if (entities.size() > last) {
            hasPreviousPage = true;
            entities = entities.subList(0, last);
        }

        } else if (first == null && last == null && after == null && before == null) {
            entities = priceService.getNewest(192); // Newest 48 hours, will be 192 entries for 15min resolution

            if (entities.size() > 192) {
            hasPreviousPage = true;
            entities = entities.subList(0, 192);
        }

        } else {
            throw new IllegalArgumentException("Invalid pagination arguments");
        }

        // Map entities to DTOs
        List<Price> prices = entities.stream()
                .map(PriceMapper::fromEntity)
                .collect(Collectors.toList());

        // Build edges for cursor pagination
        List<Edge<Price>> edges = prices.stream()
                .map(price -> new DefaultEdge<>(
                        price,
                        cursorUtil.createCursorWith(price.getTimestamp())
                ))
                .collect(Collectors.toList());

        // Build PageInfo
        var pageInfo = new DefaultPageInfo(
                cursorUtil.getFirstCursorFrom(edges),
                cursorUtil.getLastCursorFrom(edges),
                hasPreviousPage,
                hasNextPage
        );

        return new DefaultConnection<>(edges, pageInfo);
    }

    private void validatePaginationArgs(Integer first, Integer last, String after, String before) {
    boolean hasFirst = first != null;
    boolean hasLast = last != null;
    boolean hasAfter = after != null;
    boolean hasBefore = before != null;

    // not allowed: both first and last
    if (hasFirst && hasLast) {
        throw new IllegalArgumentException("Cannot use 'first' and 'last' together");
    }

    // not allowed: first + before
    if (hasFirst && hasBefore) {
        throw new IllegalArgumentException("Cannot use 'first' with 'before'");
    }

    // not allowed: last + after
    if (hasLast && hasAfter) {
        throw new IllegalArgumentException("Cannot use 'last' with 'after'");
    }
}
}
