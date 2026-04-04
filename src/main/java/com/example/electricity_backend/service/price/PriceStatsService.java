package com.example.electricity_backend.service.price;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.electricity_backend.dto.Price;
import com.example.electricity_backend.dto.Stats;

@Service
public class PriceStatsService {
    
    public Stats computeStats(List<Price> prices) {
    if (prices.isEmpty()) {
        return new Stats(0.0, 0.0, 0.0);
    }

    BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
    BigDecimal max = BigDecimal.valueOf(-Double.MAX_VALUE);
    BigDecimal sum = BigDecimal.ZERO;

    for (Price p : prices) {
        BigDecimal price = BigDecimal.valueOf(p.value());

        if (price.compareTo(min) < 0) min = price;
        if (price.compareTo(max) > 0) max = price;

        sum = sum.add(price);
    }

    BigDecimal avg = sum.divide(
        BigDecimal.valueOf(prices.size()),
        3,
        RoundingMode.HALF_UP
    );

    min = min.setScale(3, RoundingMode.HALF_UP);
    max = max.setScale(3, RoundingMode.HALF_UP);

    return new Stats(
        min.doubleValue(),
        max.doubleValue(),
        avg.doubleValue()
    );
}
}
