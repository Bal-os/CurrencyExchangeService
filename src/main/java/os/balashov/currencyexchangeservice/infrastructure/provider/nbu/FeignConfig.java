package os.balashov.currencyexchangeservice.infrastructure.provider.nbu;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Request.Options options() {
        Request.Options options = new Request.Options();
        options.isFollowRedirects();
        return options;
    }
}
