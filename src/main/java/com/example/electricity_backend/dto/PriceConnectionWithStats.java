package com.example.electricity_backend.dto;
import graphql.relay.DefaultConnection;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class PriceConnectionWithStats extends DefaultConnection<Price> {
    private final Stats stats;

    public PriceConnectionWithStats(List<Edge<Price>> edges, PageInfo pageInfo, Stats stats) {
        super(edges, pageInfo);
        this.stats = stats;
    }
}
