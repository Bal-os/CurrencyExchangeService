package os.balashov.currencyexchangeservice.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.infrastructure.rest.enums.RatesDtoStatus;
import os.balashov.currencyexchangeservice.infrastructure.rest.enums.StatusEnum;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyResponseDto {
    private String status;
    @JsonIgnore
    private HttpStatus httpStatus;
    private int code;
    private List<CurrencyRate> data;
    private String message;

    public static ResponseEntity<CurrencyResponseDto> buildResponse(CurrencyRatesDto dto) {
        CurrencyResponseDtoBuilder builder = CurrencyResponseDto.builder();

        switch (RatesDtoStatus.from(dto)) {
            case FALLIBLE -> builder.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(dto.getExceptionMessage());

            case EMPTY -> builder.httpStatus(HttpStatus.NO_CONTENT);

            case CACHED -> builder.httpStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                    .data(dto.currencyRates())
                    .message(String.format("Return currency rates from %s", dto.dataSource().getName()));

            default -> builder.httpStatus(HttpStatus.OK)
                    .data(dto.currencyRates());
        }

        CurrencyResponseDto response = builder.build();
        response.setStatusAndCode(response.getHttpStatus());
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    public static ResponseEntity<CurrencyResponseDto> buildResponse(String message, HttpStatus httpStatus) {
        CurrencyResponseDto response = CurrencyResponseDto.builder()
                .message(message)
                .httpStatus(httpStatus)
                .build();
        response.setStatusAndCode(httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    private void setStatusAndCode(HttpStatus httpStatus) {
        this.status = httpStatus.is2xxSuccessful() ? StatusEnum.SUCCESS.getStatus() : StatusEnum.ERROR.getStatus();
        this.code = httpStatus.value();
    }
}

