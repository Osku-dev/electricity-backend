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
    if (prices.isEmpty()) return new Stats(BigDecimal.ZERO.floatValue(),
                                           BigDecimal.ZERO.floatValue(),
                                           BigDecimal.ZERO.floatValue());

    BigDecimal min = BigDecimal.valueOf(Float.MAX_VALUE);
    BigDecimal max = BigDecimal.valueOf(Float.MIN_VALUE);
    BigDecimal sum = BigDecimal.ZERO;

    for (Price p : prices) {
        BigDecimal price = BigDecimal.valueOf(p.getValue());
        if (price.compareTo(min) < 0) min = price;
        if (price.compareTo(max) > 0) max = price;
        sum = sum.add(price);
    }

    BigDecimal avg = sum.divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.HALF_UP);

    min = min.setScale(2, RoundingMode.HALF_UP);
    max = max.setScale(2, RoundingMode.HALF_UP);

    return new Stats(min.floatValue(), max.floatValue(), avg.floatValue());
}
}
