package com.example.electricity_backend.service.pagination;

import org.springframework.stereotype.Component;

import com.example.electricity_backend.dto.PricePaginationArgs;

@Component
public class PricePaginationValidator {

    public void validate(PricePaginationArgs args) {
        boolean hasFirst = args.first() != null;
        boolean hasLast = args.last() != null;
        boolean hasAfter = args.after() != null;
        boolean hasBefore = args.before() != null;

        if (hasFirst && hasLast)
            throw new IllegalArgumentException("Cannot use 'first' and 'last' together");

        if (hasFirst && hasBefore)
            throw new IllegalArgumentException("Cannot use 'first' with 'before'");

        if (hasLast && hasAfter)
            throw new IllegalArgumentException("Cannot use 'last' with 'after'");
    }
}
