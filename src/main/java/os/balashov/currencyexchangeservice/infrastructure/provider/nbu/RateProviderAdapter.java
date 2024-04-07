package os.balashov.currencyexchangeservice.infrastructure.provider.nbu;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.domain.builder.CurrencyRateBuilder;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class RateProviderAdapter implements CurrencyRateProvider {
    private final NbuClient nbuClient;

    @Override
    public List<CurrencyRate> getCurrentRates() {
        return nbuClient.getCurrentRates().stream()
                .map(this::mapToCurrencyRate)
                .toList();
    }

    private CurrencyRate mapToCurrencyRate(NbuCurrencyResponseDto dto) {
        return CurrencyRateBuilder.builder()
                .currencyName(dto.getTxt())
                .currencyCode(dto.getCc())
                .currencyRate(dto.getRate())
                .currencyNumberCode(String.valueOf(dto.getR030()))
                .currencyDate(LocalDate.parse(dto.getExchangedate()))
                .build();
    }
}
