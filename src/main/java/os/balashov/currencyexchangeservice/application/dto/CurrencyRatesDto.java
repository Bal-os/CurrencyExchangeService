package os.balashov.currencyexchangeservice.application.dto;

import lombok.Builder;
import lombok.Getter;
import os.balashov.currencyexchangeservice.application.exceptions.CurrencyRateException;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class CurrencyRatesDto {
    private final List<CurrencyRate> currencyRates;
    private final LocalDate rateDate;
    private final LocalDateTime receiveTime;
    private CurrencyRateException exception;
    public boolean isFallible() {
        return Objects.nonNull(exception);
    }
    public void setException(CurrencyRateException repositoryException) {
        this.exception = repositoryException;
    }
}
