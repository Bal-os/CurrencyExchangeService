package os.balashov.currencyexchangeservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import os.balashov.currencyexchangeservice.application.service.CurrencyRatesService;
import os.balashov.currencyexchangeservice.application.service.DeleteCurrencyRateService;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;

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
    public GetCurrencyRatesUseCase getCurrencyRatesUseCase(CurrencyRatesService service) {
        return service;
    }

    @Bean
    public GetActualCurrencyRatesUseCase getActualCurrencyRatesUseCase(CurrencyRatesService service) {
        return service;
    }
}
