package os.balashov.currencyexchangeservice.domain.builder;

import os.balashov.currencyexchangeservice.domain.entity.Currency;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CurrencyRateBuilder {
    private String currencyName;
    private String currencyCode;
    private String currencyNumberCode;
    private Double rate;
    private LocalDate date;
    private LocalDateTime time;

    public static CurrencyRateBuilder builder() {
        return new CurrencyRateBuilder();
    }
    public CurrencyRateBuilder currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public CurrencyRateBuilder currencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }

    public CurrencyRateBuilder currencyNumberCode(String currencyNumberCode) {
        this.currencyNumberCode = currencyNumberCode;
        return this;
    }
    public CurrencyRateBuilder currency(Currency currency) {
        this.currencyCode = currency.currencyCode();
        this.currencyName = currency.name();
        this.currencyNumberCode = currency.numberCode();
        return this;
    }

    public CurrencyRateBuilder currencyRate(Double rate) {
        this.rate = rate;
        return this;
    }

    public CurrencyRateBuilder currencyDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public CurrencyRateBuilder receivingTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public CurrencyRate build() {
        Currency currency = new Currency(this.currencyNumberCode, this.currencyCode, this.currencyName);
        return new CurrencyRate(currency, this.rate, this.date, this.time);
    }
}