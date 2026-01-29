package com.example.electricity_backend.service.pagination;

public record PricePaginationArgs (
    Integer first,
    String after,
    Integer last,
    String before
) {}
