package os.balashov.currencyexchangeservice.infrastructure.data.utils.mapper;

import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateHistory;

public interface RateHistoryMapper {
    CurrencyRate toCurrencyRate(CurrencyRateHistory entity);

    CurrencyRateHistory toCurrencyRateHistoryEntity(CurrencyRate rate);
}
