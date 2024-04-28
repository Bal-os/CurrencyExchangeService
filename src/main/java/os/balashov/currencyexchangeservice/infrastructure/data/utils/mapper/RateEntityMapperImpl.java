package os.balashov.currencyexchangeservice.infrastructure.data.utils.mapper;

import org.springframework.stereotype.Component;
import os.balashov.currencyexchangeservice.domain.builder.CurrencyRateBuilder;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.AbstractRateEntity;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateEntity;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateHistory;
import os.balashov.currencyexchangeservice.infrastructure.data.utils.CurrencyRateEntityBuilder;

import java.math.BigDecimal;

@Component
public class RateEntityMapperImpl implements RateHistoryMapper, RateEntityMapper {
    @Override
    public CurrencyRate toCurrencyRate(CurrencyRateEntity entity) {
        return toCurrencyRate((AbstractRateEntity) entity);
    }

    @Override
    public CurrencyRateEntity toCurrencyRateEntity(CurrencyRate rate) {
        return new CurrencyRateEntity(toAbstractRateEntity(rate));
    }

    @Override
    public CurrencyRate toCurrencyRate(CurrencyRateHistory entity) {
        return toCurrencyRate((AbstractRateEntity) entity);
    }

    @Override
    public CurrencyRateHistory toCurrencyRateHistoryEntity(CurrencyRate rate) {
        return new CurrencyRateHistory(toAbstractRateEntity(rate));
    }

    private CurrencyRate toCurrencyRate(AbstractRateEntity entity) {
        return CurrencyRateBuilder.builder()
                .currencyCode(entity.getCode())
                .currencyName(entity.getName())
                .currencyNumberCode(entity.getNumberCode())
                .currencyRate(entity.getRate().doubleValue())
                .currencyDate(entity.getDate())
                .receivingTime(entity.getTimestamp())
                .build();
    }

    private AbstractRateEntity toAbstractRateEntity(CurrencyRate rate) {
        return CurrencyRateEntityBuilder.builder((t) -> new CurrencyRateEntity())
                .code(rate.currency().currencyCode())
                .name(rate.currency().name())
                .numberCode(rate.currency().numberCode())
                .rate(BigDecimal.valueOf(rate.rate()))
                .date(rate.currencyDate())
                .timestamp(rate.receivingTime())
                .build();
    }
}
