package os.balashov.currencyexchangeservice.domain.datasource;

import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.util.List;

public interface CurrencyRateProvider {
    List<CurrencyRate> getCurrentRates();
}
