package os.balashov.currencyexchangeservice.application.usecase;

import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;

import java.time.LocalDate;
import java.util.Optional;

public interface DeleteCurrencyRateUseCase {
    Optional<CurrencyRateException> deleteRates(LocalDate date);
}
