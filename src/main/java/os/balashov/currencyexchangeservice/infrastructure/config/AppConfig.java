package os.balashov.currencyexchangeservice.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import os.balashov.currencyexchangeservice.application.service.CurrencyRatesService;
import os.balashov.currencyexchangeservice.application.service.DeleteCurrencyRateService;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@EnableAsync
@EnableRetry
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
}
