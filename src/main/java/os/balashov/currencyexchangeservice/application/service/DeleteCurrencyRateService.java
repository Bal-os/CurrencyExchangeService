package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;
import os.balashov.currencyexchangeservice.application.exception.RepositoryException;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class DeleteCurrencyRateService implements DeleteCurrencyRateUseCase {
    private final CurrencyRateRepository currencyRateRepository;

    @Override
    public Optional<RateDataSourceException> deleteRates(LocalDate date) {
        log.info("Deleting rates by date: {}", date);
        try {
            currencyRateRepository.deleteCurrencyRate(date);
        } catch (RuntimeException e) {
            String message = String.format("Failed to delete %s currency rates: %s", date, e.getMessage());
            log.error(message);
            return Optional.of(new RepositoryException(message, e));
        }
        return Optional.empty();
    }
}
