package os.balashov.currencyexchangeservice.application.dto;

import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record CurrencyRatesDto(LocalDate rateDate, boolean isCached, List<CurrencyRate> currencyRates,
                               Optional<CurrencyRateException> exception) {
    public boolean isUpdatable() {
        return !this.isCached && !this.isEmptyOrFailed();
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

    public String getExceptionMessage() {
        return this.exception.get().getMessage();
    }

    public static CurrencyRatesDto create(LocalDate rateDate,
                                          boolean isCached,
                                          CurrencyRateException exception) {
        return new CurrencyRatesDto(rateDate, isCached, Collections.emptyList(), Optional.ofNullable(exception));
    }

    public static CurrencyRatesDto create(LocalDate rateDate,
                                          boolean isCached,
                                          List<CurrencyRate> currencyRates) {
        return new CurrencyRatesDto(rateDate, isCached, currencyRates, Optional.empty());
    }
}


