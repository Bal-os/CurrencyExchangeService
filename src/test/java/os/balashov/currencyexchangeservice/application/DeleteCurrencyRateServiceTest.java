package os.balashov.currencyexchangeservice.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;
import os.balashov.currencyexchangeservice.application.service.DeleteCurrencyRateService;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.utils.TestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteCurrencyRateServiceTest implements TestUtils {
    @Mock
    private CurrencyRateRepository mockRepository;
    @InjectMocks
    private DeleteCurrencyRateService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteRatesByDate_Success() {
        LocalDate date = LocalDate.of(2024, 3, 29);
        Mockito.doNothing().when(mockRepository).deleteCurrencyRate(date);

        service.deleteRates(date);

        Mockito.verify(mockRepository).deleteCurrencyRate(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void testDeleteRatesByDate_Fail() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        String message = String.format("Failed to delete %s currency rates:", date);
        Mockito.doThrow(new RuntimeException()).when(mockRepository).deleteCurrencyRate(date);

        Optional<RateDataSourceException> exception = service.deleteRates(date);

        assertTrue(exception.isPresent());
        assertTrue(exception.get().getMessage().contains(message));
        Mockito.verify(mockRepository).deleteCurrencyRate(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }
}
