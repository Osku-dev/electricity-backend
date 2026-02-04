package com.example.electricity_backend.dto;

public record PricePaginationArgs (
    Integer first,
    String after,
    Integer last,
    String before
) {}
