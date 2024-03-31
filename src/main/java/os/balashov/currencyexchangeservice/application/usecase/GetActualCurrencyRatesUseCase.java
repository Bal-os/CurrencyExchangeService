package os.balashov.currencyexchangeservice.application.usecase;

import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;

public interface GetActualCurrencyRatesUseCase {
    CurrencyRatesDto getActualRates();
}
