package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exception.RepositoryException;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;

import java.time.LocalDate;

@Slf4j
@AllArgsConstructor
public class DeleteCurrencyRateService implements DeleteCurrencyRateUseCase {
    private final CurrencyRateRepository currencyRateRepository;

    @Override
    public CurrencyRatesDto deleteRatesByDate(LocalDate date) {
        log.info("Deleting rates by date: {}", date);
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

}
