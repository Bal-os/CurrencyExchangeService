package os.balashov.currencyexchangeservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.infrastructure.provider.nbu.NbuClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NbuClientTestIT {
    private final String url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
    private final String dateFormat = "dd.MM.yyyy";
    private NbuClient nbuClient;

    @BeforeEach
    public void setup() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        nbuClient = new NbuClient(url, dateFormat, restTemplate);
    }

    @Test
    public void testGetExchangeRates_Success() {
        List<CurrencyRate> result = nbuClient.getCurrentRates();

        assertFalse(result.isEmpty());
        CurrencyRate currencyRate = result.stream().findAny().orElse(result.get(0));
        assertNotNull(currencyRate.currency());
        assertNotNull(currencyRate.currencyDate());
        assertNotNull(currencyRate.receivingTime());
        assertNotNull(currencyRate.rate());
        assertTrue(LocalDate.now().equals(currencyRate.currencyDate())
                || currencyRate.receivingTime().isBefore(
                currencyRate.currencyDate().atTime(12, 0, 0)));
    }
}