package os.balashov.currencyexchangeservice.infrastructure.data.utils;

import os.balashov.currencyexchangeservice.infrastructure.data.entity.AbstractRateEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AbstractCurrencyRateEntityBuilder {
    private String numberCode;
    private String code;
    private String name;
    private BigDecimal rate;
    private LocalDateTime timestamp;
    private LocalDate date;

    public static AbstractCurrencyRateEntityBuilder builder() {
        return new AbstractCurrencyRateEntityBuilder();
    }

    public AbstractCurrencyRateEntityBuilder numberCode(String numberCode) {
        this.numberCode = numberCode;
        return this;
    }

    public AbstractCurrencyRateEntityBuilder code(String code) {
        this.code = code;
        return this;
    }

    public AbstractCurrencyRateEntityBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AbstractCurrencyRateEntityBuilder rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public AbstractCurrencyRateEntityBuilder timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public AbstractCurrencyRateEntityBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public <T extends AbstractRateEntity> AbstractRateEntity build() {
        T currencyRate = (T) new AbstractRateEntity() {};
        currencyRate.setNumberCode(numberCode);
        currencyRate.setCode(code);
        currencyRate.setName(name);
        currencyRate.setRate(rate);
        currencyRate.setTimestamp(timestamp);
        currencyRate.setDate(date);
        return currencyRate;
    }
}

