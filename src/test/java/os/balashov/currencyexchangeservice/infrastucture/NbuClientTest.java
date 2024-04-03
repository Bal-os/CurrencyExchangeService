package os.balashov.currencyexchangeservice.infrastucture;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import os.balashov.currencyexchangeservice.infrastructure.provider.nbu.NbuClient;

public class NbuClientTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private NbuClient nbuClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
}