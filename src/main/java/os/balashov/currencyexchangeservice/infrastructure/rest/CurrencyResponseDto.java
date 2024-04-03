package os.balashov.currencyexchangeservice.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.util.List;

import static os.balashov.currencyexchangeservice.infrastructure.rest.Status.ERROR;
import static os.balashov.currencyexchangeservice.infrastructure.rest.Status.SUCCESS;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyResponseDto {
    private String status;
    private HttpStatus code;
    private List<CurrencyRate> data;
    private String message;

    public CurrencyResponseDto(CurrencyRatesDto currencyRatesDto) {
        if (currencyRatesDto.isFallible()) {
            this.status = ERROR.getStatus();
            this.code = HttpStatus.INTERNAL_SERVER_ERROR;
            this.message = currencyRatesDto.getExceptionMessage();
        } else {
            this.status = SUCCESS.getStatus();
            this.data = currencyRatesDto.currencyRates();

            if (currencyRatesDto.isCached()) {
                this.code = HttpStatus.NON_AUTHORITATIVE_INFORMATION;
                this.message = "Return cached currency rates";
            } else if (currencyRatesDto.isEmpty()) {
                this.code = HttpStatus.NO_CONTENT;
            } else {
                this.code = HttpStatus.OK;
            }
        }
    }

    public CurrencyResponseDto(String message, HttpStatus code) {
        this.message = message;
        this.status = chooseStatus(code);
        this.code = code;
    }

    public ResponseEntity<CurrencyResponseDto> getResponseEntity() {
        return new ResponseEntity<>(this, this.code);
    }

    private String chooseStatus(HttpStatus code) {
        return code.is2xxSuccessful() ? SUCCESS.getStatus() : ERROR.getStatus();
    }
}
