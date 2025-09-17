package com.example.electricity_backend.resolver.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
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

        if (after != null && first != null) {
            LocalDateTime afterTime = LocalDateTime.parse(cursorUtil.decodeCursor(after));
            entities = priceService.getPricesAfter(afterTime, first);

        } else if (before != null && last != null) {
            LocalDateTime beforeTime = LocalDateTime.parse(cursorUtil.decodeCursor(before));
            entities = priceService.getPricesBefore(beforeTime, last);

        } else if (first != null) {
            entities = priceService.getOldest(first);

        } else if (last != null) {
            entities = priceService.getNewest(last);

        } else {
            entities = priceService.getNewest(50);
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
                before != null,
                after != null
        );

        return new DefaultConnection<>(edges, pageInfo);
    }
}
