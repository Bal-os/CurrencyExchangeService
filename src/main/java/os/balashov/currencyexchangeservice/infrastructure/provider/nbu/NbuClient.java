package os.balashov.currencyexchangeservice.infrastructure.provider.nbu;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import org.springframework.cloud.openfeign.FeignClient;
import java.util.List;

@Component
@Profile("dev")
@FeignClient(name = "nbu-client", url = "${nbu.api.url}")
public interface NbuClient {

    @GetMapping
    List<NbuCurrencyResponseDto> getCurrentRates();
}
