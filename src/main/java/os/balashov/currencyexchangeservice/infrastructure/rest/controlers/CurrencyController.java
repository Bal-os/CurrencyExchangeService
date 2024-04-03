package os.balashov.currencyexchangeservice.infrastructure.rest.controlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.infrastructure.rest.dto.CurrencyResponseDto;
import os.balashov.currencyexchangeservice.infrastructure.rest.dto.DateDto;

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
    public ResponseEntity<CurrencyResponseDto> getRates(@RequestParam(required = false) String date) {
        log.info("Request to get currency rates: {}", date);

        if (Strings.isBlank(date)) {
            log.info("Request to get actual currency rates");
            return CurrencyResponseDto.buildResponse(getActualCurrencyRatesUseCase.getActualRates());
        }

        DateDto dateDto = DateDto.parseDate(date);
        if (dateDto.isFallible()) {
            String message = dateDto.getExceptionMessage();
            log.warn("Invalid date format: {}", message);
            return CurrencyResponseDto.buildResponse(message, HttpStatus.BAD_REQUEST);
        }

        log.info("Request to get currency rates for {}", dateDto.get());
        return CurrencyResponseDto.buildResponse(getCurrencyRatesUseCase.getRates(dateDto.get()));
    }

    @DeleteMapping
    public ResponseEntity<CurrencyResponseDto> deleteRates(@RequestParam(required = false) String date) {
        log.info("Request to delete currency rates for date: {}", date);

        DateDto dateDto = DateDto.parseDate(date);
        if (dateDto.isFallible()) {
            String message = dateDto.getExceptionMessage();
            log.warn("Invalid date format: {}", message);
            return CurrencyResponseDto.buildResponse(message, HttpStatus.BAD_REQUEST);
        }

        Optional<RateDataSourceException> exception = deleteCurrencyRateUseCase.deleteRates(dateDto.get());
        if (exception.isPresent()) {
            String message = exception.get().getMessage();
            log.warn("Failed to delete rates: {}", message);
            return CurrencyResponseDto.buildResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Currency rates for {} deleted successfully", date);
        String successMessage = "Currency rates for " + date + " deleted";
        return CurrencyResponseDto.buildResponse(successMessage, HttpStatus.OK);
    }
}

