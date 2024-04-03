package os.balashov.currencyexchangeservice.infrastructure.rest.enums;

import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;

public enum RatesDtoStatus {
    FALLIBLE,
    EMPTY,
    CACHED,
    OTHER;

    public static RatesDtoStatus from(CurrencyRatesDto dto) {
        return  dto.isFallible() ? FALLIBLE :
                dto.isEmpty() ? EMPTY :
                dto.isCached() ? CACHED : OTHER;
    }
}

