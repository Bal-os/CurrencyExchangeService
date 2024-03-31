package os.balashov.currencyexchangeservice.application.usecase;

import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;

import java.time.LocalDate;

public interface GetCurrencyRatesUseCase {
    CurrencyRatesDto getRatesByDate(LocalDate date);
}
