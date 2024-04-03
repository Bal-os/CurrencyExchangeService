package os.balashov.currencyexchangeservice.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import os.balashov.currencyexchangeservice.application.service.CurrencyRatesService;
import os.balashov.currencyexchangeservice.application.service.DeleteCurrencyRateService;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class AppConfig {
    @Bean
    public DeleteCurrencyRateUseCase deleteCurrencyRateService(CurrencyRateRepository repository) {
        return new DeleteCurrencyRateService(repository);
    }
    @Bean
    public CurrencyRatesService currencyRatesService(CurrencyRateRepository repository,
                                                     CurrencyRateProvider provider) {
        return new CurrencyRatesService(repository, provider);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getInterceptors().add(getClientHttpRequestInterceptor());
        return restTemplate;
    }

    private ClientHttpRequestInterceptor getClientHttpRequestInterceptor() {
        return (request, body, execution) -> {
            log.debug("Requesting {} {}", request.getMethod(), request.getURI());
            log.debug("Request Headers: {}", request.getHeaders());
            log.debug("Request Body: {}", new String(body, StandardCharsets.UTF_8));

            ClientHttpResponse response = execution.execute(request, body);

            log.debug("Response Status code: {}", response.getStatusCode());
            log.debug("Response Headers: {}", response.getHeaders());

            return response;
        };
    }
}
