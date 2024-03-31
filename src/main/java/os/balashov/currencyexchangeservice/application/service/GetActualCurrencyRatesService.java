package os.balashov.currencyexchangeservice.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exceptions.CurrencyRateException;
import os.balashov.currencyexchangeservice.application.exceptions.ProviderException;
import os.balashov.currencyexchangeservice.application.exceptions.RepositoryException;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class GetActualCurrencyRatesService implements GetActualCurrencyRatesUseCase {
    private final CurrencyRateProvider currencyRateProvider;
    private final CurrencyRateRepository currencyRateRepository;

    public GetActualCurrencyRatesService(CurrencyRateRepository currencyRateRepository,
                                         CurrencyRateProvider currencyRateProvider) {
        this.currencyRateRepository = currencyRateRepository;
        this.currencyRateProvider = currencyRateProvider;
    }

    @Override
    public CurrencyRatesDto getActualRates() {
        CurrencyRatesDto dto;
        LocalDate date = LocalDate.now();
        dto = getRatesFromRepository(date);

        if (dto.isFallible() || dto.getCurrencyRates().isEmpty()) {
            dto = getRatesFromProviderAndUpdateRepository(date, dto.getException());
        }
        return dto;
    }

    private CurrencyRatesDto getRatesFromProviderAndUpdateRepository(LocalDate date, CurrencyRateException ex) {
        CurrencyRatesDto dto;
        try {
            List<CurrencyRate> currencyRates = currencyRateProvider.getCurrentRates();
            LocalDateTime receiveTime = LocalDateTime.now();
            dto = CurrencyRatesDto.builder()
                    .rateDate(date)
                    .currencyRates(currencyRates)
                    .receiveTime(receiveTime)
                    .build();
        } catch (RuntimeException e) {
            String message = String.format("Failed to get currency rates: %s", e.getMessage());
            log.error(message);
            return CurrencyRatesDto.builder()
                    .exception(new ProviderException(message, e))
                    .rateDate(date)
                    .build();
        }
        if (Objects.nonNull(ex)) {
            dto.setException(ex);
        }
        if (!dto.getCurrencyRates().isEmpty()) {
            updateCurrencyRate(dto);
        }
        return dto;
    }

    private CurrencyRatesDto getRatesFromRepository(LocalDate date) {
        CurrencyRatesDto.CurrencyRatesDtoBuilder dtoBuilder = CurrencyRatesDto.builder();
        try {
            dtoBuilder.currencyRates(currencyRateRepository.getCurrencyRates(date));
        } catch (RuntimeException e) {
            String message = String.format("Failed to get currency rates: %s", e.getMessage());
            log.error(message);
            dtoBuilder.exception(new RepositoryException(message, e));
        }
        return dtoBuilder.build();
    }

    private void updateCurrencyRate(CurrencyRatesDto dto) {
        try {
            currencyRateRepository.updateCurrencyRate(dto.getCurrencyRates());
        } catch (RuntimeException e) {
            String message =
                    String.format("Failed to update %s currency rates: %s", dto.getRateDate(), e.getMessage());
            log.error(message);
            dto.setException(new RepositoryException(message, e));
        }
    }
}
