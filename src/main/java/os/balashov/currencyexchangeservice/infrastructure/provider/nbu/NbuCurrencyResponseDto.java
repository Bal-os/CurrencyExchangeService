package os.balashov.currencyexchangeservice.infrastructure.provider.nbu;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NbuCurrencyResponseDto {
    private int r030;
    private String txt;
    private double rate;
    private String cc;
    private String exchangedate;
}

