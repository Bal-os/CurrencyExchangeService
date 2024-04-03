package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exception.ProviderException;
import os.balashov.currencyexchangeservice.application.exception.RepositoryException;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;

import java.time.LocalDate;

@Slf4j
@AllArgsConstructor
public class CurrencyRatesService implements GetCurrencyRatesUseCase, GetActualCurrencyRatesUseCase {
    private final CurrencyRateProvider currencyRateProvider;
    private final CurrencyRateRepository currencyRateRepository;

    public CurrencyRatesService(CurrencyRateRepository currencyRateRepository,
                                CurrencyRateProvider currencyRateProvider) {
        this.currencyRateRepository = currencyRateRepository;
        this.currencyRateProvider = currencyRateProvider;
    }

    @Override
    public CurrencyRatesDto getActualRates() {
        log.info("Getting actual rates");
        LocalDate date = LocalDate.now();
        var dto = getRatesFromRepository(date);

        if (dto.isEmptyOrFailed()) {
            dto = getRatesFromProvider(date);
        }
        if (dto.isUpdatable()) {
            updateCurrencyRate(dto);
        }
        return dto;
    }

    @Override
    public CurrencyRatesDto getRates(LocalDate date) {
        log.info("Getting rates by date: {}", date);
        return getRatesFromRepository(date);
    }

    private CurrencyRatesDto getRatesFromRepository(LocalDate date) {
        log.info("Getting rates from repository for date: {}", date);
        try {
            return CurrencyRatesDto.create(date, true, currencyRateRepository.getCurrencyRates(date));
        } catch (RuntimeException e) {
            String message = String.format("Failed to get %s currency rates: %s", date, e.getMessage());
            log.error(message);
            return CurrencyRatesDto.create(date, true, new RepositoryException(message, e));
        }
    }

    private CurrencyRatesDto getRatesFromProvider(LocalDate date) {
        log.info("Getting rates from provider for date: {}", date);
        try {
            return CurrencyRatesDto.create(date, false, currencyRateProvider.getCurrentRates());
        } catch (RuntimeException e) {
            String message = String.format("Failed to get %s currency rates: %s", date, e.getMessage());
            log.error(message);
            return CurrencyRatesDto.create(date, false, new ProviderException(message, e));
        }
    }

    private void updateCurrencyRate(CurrencyRatesDto dto) {
        log.info("Updating currency rate for date: {}", dto.rateDate());
        try {
            currencyRateRepository.updateCurrencyRate(dto.currencyRates());
        } catch (RuntimeException e) {
            String message = String.format("Failed to update %s currency rates: %s", dto.rateDate(), e.getMessage());
            log.error(message);
            //the request is internal. User does not need to know if we failed in caching the data.
            //dto.setException(new RepositoryException(message, e));
        }
    }
}
