package os.balashov.currencyexchangeservice.utils;

import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.dto.DataSource;
import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CurrencyRatesDtoBuilder  {
    private LocalDate rateDate;
    private List<CurrencyRate> currencyRates;
    private DataSource dataSource;
    private RateDataSourceException exception;
    public CurrencyRatesDtoBuilder rateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
        return this;
    }

    public CurrencyRatesDtoBuilder currencyRates(List<CurrencyRate> currencyRates) {
        this.currencyRates = currencyRates;
        return this;
    }

    public CurrencyRatesDtoBuilder dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public CurrencyRatesDtoBuilder exception(RateDataSourceException exception) {
        this.exception = exception;
        return this;
    }

    public CurrencyRatesDto build() {
        return new CurrencyRatesDto(rateDate, dataSource, currencyRates, Optional.ofNullable(exception));
    }

    public static CurrencyRatesDtoBuilder builder() {
        return new CurrencyRatesDtoBuilder();
    }
}
