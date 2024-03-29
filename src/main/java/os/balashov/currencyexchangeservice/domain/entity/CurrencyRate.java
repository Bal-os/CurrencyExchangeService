package os.balashov.currencyexchangeservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CurrencyRate {
    private Currency currency;
    private Double rate;
    private LocalDate currencyDate;
    private LocalDateTime receivingTime;
}
