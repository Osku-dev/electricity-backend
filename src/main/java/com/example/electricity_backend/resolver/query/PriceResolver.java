package com.example.electricity_backend.resolver.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.electricity_backend.connection.CursorUtil;
import com.example.electricity_backend.dto.PaginatedResult;
import com.example.electricity_backend.dto.Price;
import com.example.electricity_backend.dto.PriceConnectionWithStats;
import com.example.electricity_backend.dto.PricePaginationArgs;
import com.example.electricity_backend.dto.Stats;
import com.example.electricity_backend.mapper.PriceMapper;
import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.service.pagination.PricePaginationService;
import com.example.electricity_backend.service.price.PriceStatsService;

import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PriceResolver {

    private final PricePaginationService paginationService;
    private final CursorUtil cursorUtil;
    private final PriceStatsService priceStatsService;

    @QueryMapping
    public PriceConnectionWithStats prices(
            @Argument Integer first,
            @Argument String after,
            @Argument Integer last,
            @Argument String before
    ) {

        var args = new PricePaginationArgs(first, after, last, before);

        PaginatedResult<PriceEntity> page =
                paginationService.fetch(args);

        List<Price> prices = page.items().stream()
                .map(PriceMapper::fromEntity)
                .toList();

        Stats stats = priceStatsService.computeStats(prices);

        List<Edge<Price>> edges = prices.stream()
                .map(price -> new DefaultEdge<>(
                        price,
                        cursorUtil.createCursorWith(price.timestamp())
                ))
                .collect(Collectors.toList());

        var pageInfo = new DefaultPageInfo(
                cursorUtil.getFirstCursorFrom(edges),
                cursorUtil.getLastCursorFrom(edges),
                page.hasPreviousPage(),
                page.hasNextPage()
        );

        return new PriceConnectionWithStats(edges, pageInfo, stats);
    }

}
