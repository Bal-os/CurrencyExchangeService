package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.dto.DataSource;
import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

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
        var dto = getRatesFromSource(date, DataSource.CACHE, () -> currencyRateRepository.getCurrencyRates(date));

        if (dto.isEmptyOrFailed()) {
            dto = getRatesFromSource(date, DataSource.EXTERNAL, currencyRateProvider::getCurrentRates);
        }
        if (dto.isUpdatable()) {
            updateCurrencyRate(dto);
        }
        return dto;
    }

    @Override
    public CurrencyRatesDto getRates(LocalDate date) {
        log.info("Getting rates by date: {}", date);
        return getRatesFromSource(date, DataSource.CACHE, () -> currencyRateRepository.getCurrencyRates(date));
    }

    private CurrencyRatesDto getRatesFromSource(LocalDate date, DataSource dataSource, Supplier<List<CurrencyRate>> source) {
        log.info("Getting rates from {} for date: {}", dataSource.getName(), date);
        try {
            return CurrencyRatesDto.create(date, dataSource, source.get());
        } catch (RuntimeException e) {
            String message = String.format("Failed to get %s currency rates: %s", date, e.getMessage());
            log.error(message);
            return CurrencyRatesDto.create(date, dataSource, new RateDataSourceException(message, e));
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
