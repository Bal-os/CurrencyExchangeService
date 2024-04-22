package os.balashov.currencyexchangeservice.infrastructure.provider.nbu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import os.balashov.currencyexchangeservice.domain.builder.CurrencyRateBuilder;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NbuDtoMapper {
    private final DateTimeFormatter pattern;

    public NbuDtoMapper(@Value("${nbu.api.date-pattern}") String pattern) {
        this.pattern = DateTimeFormatter.ofPattern(pattern);
    }

    public CurrencyRate getCurrencyRate(NbuCurrencyResponseDto dto) {
        return CurrencyRateBuilder.builder()
                .currencyCode(dto.getCc())
                .currencyRate(dto.getRate())
                .currencyName(dto.getTxt())
                .currencyNumberCode(Integer.toString(dto.getR030()))
                .currencyDate(LocalDate.parse(dto.getExchangedate(), pattern))
                .receivingTime(LocalDateTime.now())
                .build();
    }
}
