package os.balashov.currencyexchangeservice.infrastructure.provider.nbu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import os.balashov.currencyexchangeservice.domain.builder.CurrencyRateBuilder;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
public class NbuClient implements CurrencyRateProvider {
    private final DateTimeFormatter inputFormatter;
    private final String nbuApiUri;
    private final RestTemplate restTemplate;

    @Autowired
    public NbuClient(@Value("${nbu.api.url}") String nbuApiUri,
                     @Value("${nbu.api.pattern}") String nbuApiPattern,
                     RestTemplate restTemplate) {
        this.inputFormatter = DateTimeFormatter.ofPattern(nbuApiPattern);
        this.nbuApiUri = nbuApiUri;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<CurrencyRate> getCurrentRates() {
        try {
            return getExchangeRates().stream()
                    .map(this::mapCurrencyRate)
                    .toList();
        } catch (RuntimeException e) {
            log.error("Error fetching exchange rates from NBU API: {}", e.getMessage());
            throw e;
        }
    }

    private List<NbuCurrencyResponseDto> getExchangeRates() throws RuntimeException {
        ResponseEntity<List<NbuCurrencyResponseDto>> response = restTemplate.exchange(
                nbuApiUri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Response Body: {}", response.getBody());
        } else {
            log.error("Error fetching exchange rates from NBU API: {}", response.getStatusCode());
            throw new RuntimeException("Error fetching exchange rates from NBU API with status code: " + response.getStatusCode());
        }

        return response.getBody();
    }

    private CurrencyRate mapCurrencyRate(NbuCurrencyResponseDto nbuCurrencyResponseDto) {
        return CurrencyRateBuilder.builder()
                .currencyNumberCode(String.valueOf(nbuCurrencyResponseDto.getR030()))
                .currencyCode(nbuCurrencyResponseDto.getCc())
                .currencyName(nbuCurrencyResponseDto.getTxt())
                .currencyRate(nbuCurrencyResponseDto.getRate())
                .currencyDate(LocalDate.parse(nbuCurrencyResponseDto.getExchangedate(), inputFormatter))
                .receivingTime(LocalDateTime.now())
                .build();
    }
}
