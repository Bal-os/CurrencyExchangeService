package os.balashov.currencyexchangeservice.domain.datasource;

import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import java.time.LocalDate;

public interface ExchangeRateRepository {
    CurrencyRate getExchangeRate(LocalDate currencyDate);

    void updateExchangeRate(CurrencyRate exchangeRate);

    void deleteExchangeRate(CurrencyRate exchangeRate);
}
