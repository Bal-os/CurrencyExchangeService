package os.balashov.currencyexchangeservice.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateDto {
    private final Optional<LocalDate> dateObj;
    private final Optional<CurrencyRateException> exception;

    private DateDto(Optional<LocalDate> dateObj, Optional<CurrencyRateException> exception) {
        this.dateObj = dateObj;
        this.exception = exception;
    }

    public LocalDate get() {
        return dateObj.get();
    }

    public boolean isFallible() {
        return exception.isPresent();
    }

    public String getExceptionMessage() {
        return exception.get().getMessage();
    }

    public static DateDto parseDate(String date) {
        LocalDate dateObj;
        try {
            dateObj = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return new DateDto(Optional.empty(),
                    Optional.of(new CurrencyRateException("Failed to parse date: " + date, e)));
        }

        if (LocalDate.now().isBefore(dateObj)) {
            return new DateDto(Optional.empty(),
                    Optional.of(new CurrencyRateException("Date is in the future: " + date)));
        }
        return new DateDto(Optional.of(dateObj), Optional.empty());
    }
}