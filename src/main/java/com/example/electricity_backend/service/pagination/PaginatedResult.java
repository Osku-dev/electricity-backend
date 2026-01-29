package com.example.electricity_backend.service.pagination;

import java.util.List;

public record PaginatedResult<T>(
    List<T> items,
    boolean hasNextPage,
    boolean hasPreviousPage
) {}

