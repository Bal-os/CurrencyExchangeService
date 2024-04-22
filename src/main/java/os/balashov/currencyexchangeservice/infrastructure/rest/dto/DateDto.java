package os.balashov.currencyexchangeservice.infrastructure.rest.dto;

import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateDto {
    private final Optional<LocalDate> dateObj;
    private final Optional<RateDataSourceException> exception;

    private DateDto(Optional<LocalDate> dateObj, Optional<RateDataSourceException> exception) {
        this.dateObj = dateObj;
        this.exception = exception;
    }

    public static DateDto parseDate(String date) {
        LocalDate dateObj;
        try {
            dateObj = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return new DateDto(Optional.empty(),
                    Optional.of(new RateDataSourceException("Failed to parse date: " + date, e)));
        }

        if (dateObj.isAfter(LocalDate.now())) {
            return new DateDto(Optional.empty(),
                    Optional.of(new RateDataSourceException("Date is in the future: " + date)));
        }
        return new DateDto(Optional.of(dateObj), Optional.empty());
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
}