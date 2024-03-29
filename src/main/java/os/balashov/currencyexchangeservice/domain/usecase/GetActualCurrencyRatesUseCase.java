package os.balashov.currencyexchangeservice.domain.usecase;

import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.util.List;

public interface GetActualCurrencyRatesUseCase {
    List<CurrencyRate> getActualRates();
}
