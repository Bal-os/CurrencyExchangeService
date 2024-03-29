package os.balashov.currencyexchangeservice.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CurrencyRate {
    private Currency currency;
    private Double rate;
    private LocalDate currencyDate;
    private LocalDateTime receivingTime;
}
