package os.balashov.currencyexchangeservice.infrastructure.data.utils.mapper;

import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateEntity;

public interface RateEntityMapper {
    CurrencyRate toCurrencyRate(CurrencyRateEntity entity);

    CurrencyRateEntity toCurrencyRateEntity(CurrencyRate rate);
}
