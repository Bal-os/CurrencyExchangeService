package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.exeptions.RepositoryException;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.domain.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.domain.usecase.GetCurrencyRatesUseCase;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class CurrencyRateService implements DeleteCurrencyRateUseCase, GetCurrencyRatesUseCase {
    private final CurrencyRateRepository currencyRateRepository;

    @Override
    public void deleteRatesByDate(LocalDate date) throws RepositoryException {
        try {
            currencyRateRepository.deleteCurrencyRate(date);
        } catch(RuntimeException e) {
            String message = String.format("Failed to delete %s currency rates: %s",  date, e.getMessage());
            log.error(message);
            throw new RepositoryException(message, e);
        }
    }
    @Override
    public List<CurrencyRate> getRatesByDate(LocalDate date) throws RepositoryException {
        List<CurrencyRate> currencyRates;
        try {
            currencyRates = currencyRateRepository.getCurrencyRates(date);
        } catch(RuntimeException e) {
            String message = String.format("Failed to get %s currency rates: %s", date, e.getMessage());
            log.error(message);
            throw new RepositoryException(message, e);
        }
        return currencyRates;
    }
}
