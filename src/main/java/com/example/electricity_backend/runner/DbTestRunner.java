/*package com.example.electricity_backend.runner;

import com.example.electricity_backend.model.PriceEntity;
import com.example.electricity_backend.service.PriceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
public class DbTestRunner implements CommandLineRunner {

    private final PriceService priceService;

    public DbTestRunner(PriceService priceService) {
        this.priceService = priceService;
    }

    @Override
    public void run(String... args) throws Exception {
        PriceEntity test = new PriceEntity();
        test.setStartTime(LocalDateTime.now().withNano(0));
        test.setPrice(BigDecimal.valueOf(123.45));
        test.setResolutionMinutes(60);

        priceService.savePrice(test);

        List<PriceEntity> lastWeek = priceService.getPricesBetween(
    LocalDateTime.now().minusDays(5), 
    LocalDateTime.now()
);

        System.out.println("Saved price: " + lastWeek);
    }
}
*/