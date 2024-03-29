package os.balashov.currencyexchangeservice.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record CurrencyRate(Currency currency, Double rate, LocalDate currencyDate, LocalDateTime receivingTime) {
}
