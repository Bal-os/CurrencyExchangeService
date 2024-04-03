package os.balashov.currencyexchangeservice.utils;

import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CurrencyRatesDtoBuilder  {
    private LocalDate rateDate;
    private List<CurrencyRate> currencyRates;
    private boolean isCached;
    private CurrencyRateException exception;
    public CurrencyRatesDtoBuilder rateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
        return this;
    }

    public CurrencyRatesDtoBuilder currencyRates(List<CurrencyRate> currencyRates) {
        this.currencyRates = currencyRates;
        return this;
    }

    public CurrencyRatesDtoBuilder isCached(boolean isCached) {
        this.isCached = isCached;
        return this;
    }

    public CurrencyRatesDtoBuilder exception(CurrencyRateException exception) {
        this.exception = exception;
        return this;
    }

    public CurrencyRatesDto build() {
        return new CurrencyRatesDto(rateDate, isCached, currencyRates, Optional.ofNullable(exception));
    }

    public static CurrencyRatesDtoBuilder builder() {
        return new CurrencyRatesDtoBuilder();
    }
}
