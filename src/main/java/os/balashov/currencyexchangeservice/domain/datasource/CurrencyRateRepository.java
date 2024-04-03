package os.balashov.currencyexchangeservice.domain.datasource;

import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateRepository {
    List<CurrencyRate> getCurrencyRates(LocalDate currencyDate);

    void updateCurrencyRate(List<CurrencyRate> currencyRates);

    void deleteCurrencyRate(LocalDate currencyDate);
}
