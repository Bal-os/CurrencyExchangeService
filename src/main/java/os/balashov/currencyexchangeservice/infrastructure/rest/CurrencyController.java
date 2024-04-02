package os.balashov.currencyexchangeservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/api/currency")
@AllArgsConstructor
public class CurrencyController {
    private final DeleteCurrencyRateUseCase deleteCurrencyRateUseCase;
    private final GetActualCurrencyRatesUseCase getActualCurrencyRatesUseCase;
    private final GetCurrencyRatesUseCase getCurrencyRatesUseCase;

    @GetMapping
    public ResponseEntity<CurrencyResponseDto> getActualRates() {
        log.info("Request to get currency rates for today");
        return handleRequest(getActualCurrencyRatesUseCase.getActualRates());
    }

    @GetMapping("/{date}")
    public ResponseEntity<CurrencyResponseDto> getRatesByDate(@PathVariable(required = false) String date) {
        boolean isDateEmpty = date == null || date.isEmpty() || date.isBlank();
        log.info("Request to get currency rates for {}", isDateEmpty ? "today" : date);
        if (isDateEmpty) {
            return handleRequest(getActualCurrencyRatesUseCase.getActualRates());
        }

        return checkDateAndHandleRequest(date, "get", getCurrencyRatesUseCase::getRatesByDate);
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<CurrencyResponseDto> deleteRatesByDate(@PathVariable String date) {
        log.warn("Request to delete currency rates for date: {}", date);
        return checkDateAndHandleRequest(date, "delete",  deleteCurrencyRateUseCase::deleteRatesByDate);
    }

    private ResponseEntity<CurrencyResponseDto> checkDateAndHandleRequest(String date,
                                                                          String method,
                                                                          Function<LocalDate, CurrencyRatesDto> executor) {
        CurrencyRatesDto dateDto = DateUtils.parseDate(date);
        if (dateDto.isFallible()) {
            log.warn("Request to {} currency rates for {} day returns bad request: {}",
                    method,
                    date,
                    dateDto.getException().getMessage());
            return new ResponseEntity<>(new CurrencyResponseDto(dateDto), HttpStatus.BAD_REQUEST);
        }
        return handleRequest(executor.apply(dateDto.getRateDate()));
    }

    private ResponseEntity<CurrencyResponseDto> handleRequest(CurrencyRatesDto actualRates) {
        if (actualRates.isFallible()) {
            log.warn("Request returns bad request: {}", actualRates.getException().getMessage());
            return new ResponseEntity<>(new CurrencyResponseDto(actualRates), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (Objects.isNull(actualRates.getCurrencyRates()) || actualRates.getCurrencyRates().isEmpty()) {
            log.warn("Request returns without content");
            return new ResponseEntity<>(new CurrencyResponseDto(actualRates), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new CurrencyResponseDto(actualRates), HttpStatus.OK);
    }
}
