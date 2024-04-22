package os.balashov.currencyexchangeservice.infrastructure.provider.nbu;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.util.List;

@Service
@AllArgsConstructor
public class RateProviderAdapter implements CurrencyRateProvider {
    private final NbuClient nbuClient;
    private final NbuDtoMapper nbuDtoMapper;

    @Override
    public List<CurrencyRate> getCurrentRates() {
        return nbuClient.getRates().stream()
                .map(nbuDtoMapper::getCurrencyRate)
                .toList();
    }
}
