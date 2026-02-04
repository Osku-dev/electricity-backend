package com.example.electricity_backend.service.pagination;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.example.electricity_backend.dto.PricePaginationArgs;

class PricePaginationValidatorTest {

    private final PricePaginationValidator validator =
            new PricePaginationValidator();

    @Test
    void throwsIfFirstAndLastAreProvided() {
        var args = new PricePaginationArgs(10, null, 10, null);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(args));
    }

    @Test
    void throwsIfFirstAndBeforeAreProvided() {
        var args = new PricePaginationArgs(10, null, null, "cursor");

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(args));
    }

    @Test
    void throwsIfLastAndAfterAreProvided() {
        var args = new PricePaginationArgs(null, "cursor", 10, null);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(args));
    }

    @Test
    void doesNotThrowForValidCombination_firstAndAfter() {
        var args = new PricePaginationArgs(10, "cursor", null, null);

        assertDoesNotThrow(() -> validator.validate(args));
    }

    @Test
    void doesNotThrowForValidCombination_lastAndBefore() {
        var args = new PricePaginationArgs(null, null, 10, "cursor");

        assertDoesNotThrow(() -> validator.validate(args));
    }

    @Test
    void doesNotThrowWhenOnlyFirstIsProvided() {
        var args = new PricePaginationArgs(10, null, null, null);

        assertDoesNotThrow(() -> validator.validate(args));
    }
}
