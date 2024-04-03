package os.balashov.currencyexchangeservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/currency")
@AllArgsConstructor
public class CurrencyController {
    private final DeleteCurrencyRateUseCase deleteCurrencyRateUseCase;
    private final GetActualCurrencyRatesUseCase getActualCurrencyRatesUseCase;
    private final GetCurrencyRatesUseCase getCurrencyRatesUseCase;

    @GetMapping
    public ResponseEntity<CurrencyResponseDto> getRatesByDate(@RequestParam(required = false) String date) {
        log.info("Request to get currency rates for date: {}", date);
        if (Strings.isBlank(date)) {
            log.info("Request to get actual currency rates");
            var currencyRates = getActualCurrencyRatesUseCase.getActualRates();
            return handleResponse(new CurrencyResponseDto(currencyRates));
        }
        DateDto dateDto = DateDto.parseDate(date);
        if (dateDto.isFallible()) {
            String message = dateDto.getExceptionMessage();
            log.warn("Request to get currency rates for {} day returns bad request: {}",
                    date,
                    dateDto.getExceptionMessage());
            return handleResponse(new CurrencyResponseDto(message, HttpStatus.BAD_REQUEST));
        }
        log.info("Request to get currency rates for {} day returns success", date);
        var currencyRates = getCurrencyRatesUseCase.getRates(dateDto.get());
        return handleResponse(new CurrencyResponseDto(currencyRates));
    }

    @DeleteMapping
    public ResponseEntity<CurrencyResponseDto> deleteRatesByDate(@RequestParam String date) {
        log.info("Request to delete currency rates for date: {}", date);
        DateDto dateDto = DateDto.parseDate(date);
        if (dateDto.isFallible()) {
            String message = dateDto.getExceptionMessage();
            log.warn("Request to delete currency rates for {} day returns bad request: {}",
                    date,
                    dateDto.getExceptionMessage());
            return handleResponse(new CurrencyResponseDto(message, HttpStatus.BAD_REQUEST));
        }
        Optional<CurrencyRateException> exception = deleteCurrencyRateUseCase.deleteRates(dateDto.get());
        if (exception.isPresent()) {
            String message = exception.get().getMessage();
            log.warn("Request returns internal server error: {}", message);
            return handleResponse(new CurrencyResponseDto(message, HttpStatus.INTERNAL_SERVER_ERROR));
        }
        log.info("Request to delete currency rates for {} day returns success", date);
        return handleResponse(new CurrencyResponseDto("Currency rates for " + date + " deleted", HttpStatus.OK));
    }

    private ResponseEntity<CurrencyResponseDto> handleResponse(CurrencyResponseDto dto) {
        return dto.getResponseEntity();
    }
}
