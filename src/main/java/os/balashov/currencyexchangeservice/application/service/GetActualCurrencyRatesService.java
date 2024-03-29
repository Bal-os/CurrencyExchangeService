package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.exeptions.ProviderException;
import os.balashov.currencyexchangeservice.application.exeptions.RepositoryException;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.domain.usecase.GetActualCurrencyRatesUseCase;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class GetActualCurrencyRatesService implements GetActualCurrencyRatesUseCase {
    private final CurrencyRateProvider currencyRateProvider;
    private final CurrencyRateRepository currencyRateRepository;
    private RepositoryException repositoryException;

    public GetActualCurrencyRatesService(CurrencyRateRepository currencyRateRepository,
                                         CurrencyRateProvider currencyRateProvider) {
        this.currencyRateRepository = currencyRateRepository;
        this.currencyRateProvider = currencyRateProvider;
    }

    @Override
    public List<CurrencyRate> getActualRates() throws ProviderException {
        LocalDate date = LocalDate.now();
        List<CurrencyRate> currencyRates = getRatesFromRepository(date);

        if (currencyRates.isEmpty()) {
            currencyRates = getRatesFromProviderAndUpdateRepository(date);
        }
        return currencyRates;
    }

    private List<CurrencyRate> getRatesFromProviderAndUpdateRepository(LocalDate date) throws ProviderException {
        List<CurrencyRate> currencyRates = Collections.emptyList();
        try {
            currencyRates = currencyRateProvider.getCurrentRates();
        } catch (RuntimeException e) {
            String message = String.format("Failed to get currency rates: %s", e.getMessage());
            log.error(message);
            throw new ProviderException(message, e);
        }

        if (!currencyRates.isEmpty()) {
            updateCurrencyRate(date, currencyRates);
        }
        return currencyRates;
    }

    private List<CurrencyRate> getRatesFromRepository(LocalDate date) {
        List<CurrencyRate> currencyRates = Collections.emptyList();
        try {
            currencyRates = currencyRateRepository.getCurrencyRates(date);
        } catch (RuntimeException e) {
            String message = String.format("Failed to get currency rates: %s", e.getMessage());
            log.error(message);
            repositoryException = new RepositoryException(message, e);
        }
        return currencyRates;
    }

    private void updateCurrencyRate(LocalDate date, List<CurrencyRate> currencyRates) {
        try {
            currencyRateRepository.updateCurrencyRate(currencyRates);

            updateExceptionIfDataNotUpdated(date);
        } catch (RuntimeException e) {
            String message = String.format("Failed to update %s currency rates: %s", date, e.getMessage());
            log.error(message);
            repositoryException = new RepositoryException(message, e);
        }
    }

    private void updateExceptionIfDataNotUpdated(LocalDate date) {
        List<CurrencyRate> currencyRates;
        currencyRates = getRatesFromRepository(date);
        if (currencyRates.isEmpty()) {
            String message = String.format("The %s currency rates update is unsuccessful", date);
            log.error(message);
            repositoryException = new RepositoryException(message, repositoryException);
        }
    }
}
