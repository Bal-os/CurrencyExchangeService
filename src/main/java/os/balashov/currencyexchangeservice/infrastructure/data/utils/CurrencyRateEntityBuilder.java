package os.balashov.currencyexchangeservice.infrastructure.data.utils;

import os.balashov.currencyexchangeservice.infrastructure.data.entity.AbstractRateEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

public class CurrencyRateEntityBuilder<T extends AbstractRateEntity> {
    private final Function<CurrencyRateEntityBuilder<T>, T> instanceSupplier;
    private String numberCode;
    private String code;
    private String name;
    private BigDecimal rate;
    private LocalDateTime timestamp;
    private LocalDate date;

    private CurrencyRateEntityBuilder(Function<CurrencyRateEntityBuilder<T>, T> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public static <T extends AbstractRateEntity> CurrencyRateEntityBuilder<T> builder(Function<CurrencyRateEntityBuilder<T>, T> instanceSupplier) {
        return new CurrencyRateEntityBuilder<>(instanceSupplier);
    }

    public CurrencyRateEntityBuilder<T> numberCode(String numberCode) {
        this.numberCode = numberCode;
        return this;
    }

    public CurrencyRateEntityBuilder<T> code(String code) {
        this.code = code;
        return this;
    }

    public CurrencyRateEntityBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public CurrencyRateEntityBuilder<T> rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public CurrencyRateEntityBuilder<T> timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public CurrencyRateEntityBuilder<T> date(LocalDate date) {
        this.date = date;
        return this;
    }

    public T build() {
        T currencyRate = instanceSupplier.apply(this);
        currencyRate.setNumberCode(numberCode);
        currencyRate.setCode(code);
        currencyRate.setName(name);
        currencyRate.setRate(rate);
        currencyRate.setTimestamp(timestamp);
        currencyRate.setDate(date);
        return currencyRate;
    }
}

