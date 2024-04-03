package os.balashov.currencyexchangeservice.application.dto;

import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record CurrencyRatesDto(LocalDate rateDate, DataSource dataSource, List<CurrencyRate> currencyRates,
                               Optional<RateDataSourceException> exception) {
    public boolean isUpdatable() {
        return !isCached() && !this.isEmptyOrFailed();
    }

    public boolean isEmptyOrFailed() {
        return this.isFallible() || this.isEmpty();
    }

    public boolean isFallible() {
        return this.exception.isPresent();
    }

    public boolean isEmpty() {
        return this.currencyRates.isEmpty();
    }

    public boolean isCached() {
        return DataSource.CACHE.equals(this.dataSource);
    }

    public String getExceptionMessage() {
        return this.exception.get().getMessage();
    }

    public static CurrencyRatesDto create(LocalDate rateDate,
                                          DataSource dataSource,
                                          RateDataSourceException exception) {
        return new CurrencyRatesDto(rateDate, dataSource, Collections.emptyList(), Optional.ofNullable(exception));
    }

    public static CurrencyRatesDto create(LocalDate rateDate,
                                          DataSource dataSource,
                                          List<CurrencyRate> currencyRates) {
        return new CurrencyRatesDto(rateDate, dataSource, currencyRates, Optional.empty());
    }
}


