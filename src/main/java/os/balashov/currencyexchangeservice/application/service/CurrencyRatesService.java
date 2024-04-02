package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;
import os.balashov.currencyexchangeservice.application.exception.ProviderException;
import os.balashov.currencyexchangeservice.application.exception.RepositoryException;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
        CurrencyRatesDto dto;
        LocalDate date = LocalDate.now();
        dto = getRatesByDate(date);

        if (dto.isFallible() || dto.getCurrencyRates().isEmpty()) {
            dto = getRatesFromProviderAndUpdateRepository(date, dto.getException());
        }
        return dto;
    }

    @Override
    public CurrencyRatesDto getRatesByDate(LocalDate date) {
        log.info("Getting rates by date: {}", date);
        CurrencyRatesDto.CurrencyRatesDtoBuilder builder = CurrencyRatesDto.builder();
        try {
            builder.currencyRates(currencyRateRepository.getCurrencyRates(date));
        } catch(RuntimeException e) {
            String message = String.format("Failed to get %s currency rates: %s", date, e.getMessage());
            log.error(message);
            builder.exception(new RepositoryException(message, e));
        }
        return builder.rateDate(date).build();
    }

    private CurrencyRatesDto getRatesFromProviderAndUpdateRepository(LocalDate date, CurrencyRateException ex) {
        log.info("Getting rates from provider and updating repository for date: {}", date);
        CurrencyRatesDto dto = getRatesFromProvider(date);
        if (Objects.nonNull(ex)) {
            dto.setException(ex);
        }
        if (!dto.isFallible() && !dto.getCurrencyRates().isEmpty()) {
            updateCurrencyRate(dto);
        }
        return dto;
    }

    private CurrencyRatesDto getRatesFromProvider(LocalDate date) {
        log.info("Getting rates from provider for date: {}", date);
        CurrencyRatesDto.CurrencyRatesDtoBuilder builder = CurrencyRatesDto.builder();
        try {
            List<CurrencyRate> currencyRates = currencyRateProvider.getCurrentRates();
            builder.currencyRates(currencyRates);
        } catch (RuntimeException e) {
            String message = String.format("Failed to get %s currency rates: %s", date, e.getMessage());
            log.error(message);
            builder.exception(new ProviderException(message, e));
        }
        return builder.rateDate(date).build();
    }


    private void updateCurrencyRate(CurrencyRatesDto dto) {
        log.info("Updating currency rate for date: {}", dto.getRateDate());
        try {
            currencyRateRepository.updateCurrencyRate(dto.getCurrencyRates());
        } catch (RuntimeException e) {
            String message = String.format("Failed to update %s currency rates: %s", dto.getRateDate(), e.getMessage());
            log.error(message);
            dto.setException(new RepositoryException(message, e));
        }
    }
}
