package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exceptions.RepositoryException;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class CurrencyRateService implements DeleteCurrencyRateUseCase, GetCurrencyRatesUseCase {
    private final CurrencyRateRepository currencyRateRepository;

    @Override
    public CurrencyRatesDto deleteRatesByDate(LocalDate date) {
        CurrencyRatesDto.CurrencyRatesDtoBuilder builder = CurrencyRatesDto.builder();
        try {
            currencyRateRepository.deleteCurrencyRate(date);
        } catch(RuntimeException e) {
            String message = String.format("Failed to delete %s currency rates: %s",  date, e.getMessage());
            log.error(message);
            builder.exception(new RepositoryException(message, e));
        }
        return builder.rateDate(date).build();
    }

    @Override
    public CurrencyRatesDto getRatesByDate(LocalDate date) {
        CurrencyRatesDto.CurrencyRatesDtoBuilder builder = CurrencyRatesDto.builder();
        try {
            List<CurrencyRate> currencyRates = currencyRateRepository.getCurrencyRates(date);
            LocalDateTime receiveTime = LocalDateTime.now();
            builder.currencyRates(currencyRates)
                    .receiveTime(receiveTime);
        } catch(RuntimeException e) {
            String message = String.format("Failed to get %s currency rates: %s", date, e.getMessage());
            log.error(message);
            builder.exception(new RepositoryException(message, e));
        }
        return builder.rateDate(date).build();
    }
}
