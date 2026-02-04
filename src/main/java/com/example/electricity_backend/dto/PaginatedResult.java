package com.example.electricity_backend.dto;

import java.util.List;

public record PaginatedResult<T>(
    List<T> items,
    boolean hasNextPage,
    boolean hasPreviousPage
) {}

