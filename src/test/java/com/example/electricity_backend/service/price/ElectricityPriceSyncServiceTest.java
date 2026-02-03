package com.example.electricity_backend.service.price;

import com.example.electricity_backend.dto.ElectricityPriceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElectricityPriceSyncServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ExternalPriceService externalPriceService;

    @InjectMocks
    private ElectricityPriceSyncService syncService;

    @Test
    void syncNewBatch_whenNoPrices_doesNothing() {
        when(externalPriceService.fetchDailyPrices())
                .thenReturn(List.of());

        syncService.syncNewBatch();

        verify(externalPriceService).fetchDailyPrices();
        verify(jdbcTemplate, never()).batchUpdate(anyString(), anyList());
    }

    @Test
    void syncNewBatch_whenPricesExist_callsBatchUpdateWithMappedValues() {
        
        ElectricityPriceDto dto1 = new ElectricityPriceDto(
                "2026-01-01T00:00",
                12.34
        );

        ElectricityPriceDto dto2 = new ElectricityPriceDto(
                "2026-01-01T00:15",
                15.67
        );

        when(externalPriceService.fetchDailyPrices())
                .thenReturn(List.of(dto1, dto2));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Object[]>> batchArgsCaptor =
                ArgumentCaptor.forClass(List.class);

        syncService.syncNewBatch();

        verify(jdbcTemplate).batchUpdate(
                startsWith("INSERT INTO prices"),
                batchArgsCaptor.capture()
        );

        List<Object[]> batchArgs = batchArgsCaptor.getValue();
        assertThat(batchArgs).hasSize(2);

        Object[] firstRow = batchArgs.get(0);
        Object[] secondRow = batchArgs.get(1);

        assertThat(firstRow[0]).isNotNull();
assertThat(firstRow[1]).isEqualTo(BigDecimal.valueOf(dto1.getPrice()));
assertThat(firstRow[2]).isEqualTo(15);

assertThat(secondRow[0]).isNotNull();
assertThat(secondRow[1]).isEqualTo(BigDecimal.valueOf(dto2.getPrice()));
assertThat(secondRow[2]).isEqualTo(15);
    }
}
