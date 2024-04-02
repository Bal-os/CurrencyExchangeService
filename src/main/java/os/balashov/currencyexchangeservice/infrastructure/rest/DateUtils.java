package os.balashov.currencyexchangeservice.infrastructure.rest;

import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateUtils {
    public static CurrencyRatesDto parseDate(String date) {
        CurrencyRatesDto.CurrencyRatesDtoBuilder builder = CurrencyRatesDto.builder();
        LocalDate dateObj;
        try {
            dateObj = LocalDate.parse(date);

            if (LocalDate.now().isBefore(dateObj)) {
                builder.exception(new CurrencyRateException("Date is in the future: " + date));
            } else {
                builder.rateDate(dateObj);
            }
        } catch (DateTimeParseException e) {
            builder.exception(new CurrencyRateException("Failed to parse date: " + date, e));
        }
        return builder.build();
    }
}