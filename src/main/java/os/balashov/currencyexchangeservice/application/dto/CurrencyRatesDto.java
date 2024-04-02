package os.balashov.currencyexchangeservice.application.dto;

import lombok.Builder;
import lombok.Getter;
import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class CurrencyRatesDto {
    private final LocalDate rateDate;
    private final List<CurrencyRate> currencyRates;
    private CurrencyRateException exception;
    public boolean isFallible() {
        return Objects.nonNull(exception);
    }
    public void setException(CurrencyRateException repositoryException) {
        this.exception = repositoryException;
    }
}
