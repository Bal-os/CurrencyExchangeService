package os.balashov.currencyexchangeservice.domain.usecase;

import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import java.time.LocalDate;
import java.util.List;

public interface GetCurrencyRatesUseCase {
    List<CurrencyRate> getActualRates();
    List<CurrencyRate> getRatesByDate(LocalDate currencyDate);
}
