package os.balashov.currencyexchangeservice.application.usecase;

import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;

import java.time.LocalDate;
import java.util.Optional;

public interface DeleteCurrencyRateUseCase {
    Optional<RateDataSourceException> deleteRates(LocalDate date);
}
