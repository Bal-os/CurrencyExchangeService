package os.balashov.currencyexchangeservice.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyResponseDto {
    private String status;
    private List<CurrencyRate> currencyRates;
    private String message;

    public CurrencyResponseDto(CurrencyRatesDto currencyRatesDto) {
        if (currencyRatesDto.isFallible()) {
            this.status = "error";
            this.message = currencyRatesDto.getException().getMessage();
        } else {
            this.status = "success";
        }
        this.currencyRates = currencyRatesDto.getCurrencyRates();
    }
}
