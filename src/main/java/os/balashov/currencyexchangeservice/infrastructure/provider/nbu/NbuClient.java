package os.balashov.currencyexchangeservice.infrastructure.provider.nbu;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@Profile({"dev", "prod"})
@FeignClient(name = "nbu-client", url = "${nbu.api.url}", configuration = FeignConfig.class)
public interface NbuClient {

    @GetMapping()
    List<NbuCurrencyResponseDto> getRates();
}
