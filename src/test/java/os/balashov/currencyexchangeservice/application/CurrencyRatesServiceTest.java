package os.balashov.currencyexchangeservice.application;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import os.balashov.currencyexchangeservice.application.exception.RateDataSourceException;
import os.balashov.currencyexchangeservice.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exception.RepositoryException;
import os.balashov.currencyexchangeservice.application.service.CurrencyRatesService;
import os.balashov.currencyexchangeservice.domain.builder.CurrencyRateBuilder;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.domain.entity.Currency;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class CurrencyRatesServiceTest implements TestUtils {
    @Mock
    private CurrencyRateProvider mockProvider;
    @Mock
    private CurrencyRateRepository mockRepository;
    @InjectMocks
    private CurrencyRatesService service;
    private Map<String, Currency> codeCurrencyMap;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        codeCurrencyMap = codeCurrencyMap();
    }

    @Test
    public void testGetActualRates_Success() {
        List<CurrencyRate> mockRates = Arrays.asList(
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("USD")).currencyRate(1.0).build(),
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("EUR")).currencyRate(0.9).build()
        );
        Mockito.when(mockProvider.getCurrentRates()).thenReturn(mockRates);
        Mockito.doNothing().when(mockRepository).updateCurrencyRate(mockRates);
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class)))
                .thenReturn(Collections.emptyList()).thenReturn(mockRates);

        CurrencyRatesDto dto = service.getActualRates();

        assertEquals(mockRates, dto.currencyRates());
        Mockito.inOrder(mockRepository, mockProvider, mockRepository, mockRepository);
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockRepository).updateCurrencyRate(mockRates);
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }

    @Test
    public void testGetActualRates_SuccessRepositoryResponse() {
        List<CurrencyRate> mockRates = Arrays.asList(
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("USD")).currencyRate(1.0).build(),
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("EUR")).currencyRate(0.9).build()
        );
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenReturn(mockRates);

        CurrencyRatesDto dto = service.getActualRates();

        assertEquals(mockRates, dto.currencyRates());
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    public void testGetActualRates_EmptyProviderResponse() {
        List<CurrencyRate> emptyList = Collections.emptyList();
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenReturn(emptyList);
        Mockito.when(mockProvider.getCurrentRates()).thenReturn(emptyList);

        CurrencyRatesDto dto = service.getActualRates();

        assertEquals(dto.currencyRates(), emptyList);
        assertTrue(dto.currencyRates().isEmpty());
        Mockito.inOrder(mockRepository, mockProvider);
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }

    @Test
    public void testGetActualRates_ErrorProviderResponse() {
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenReturn(Collections.emptyList());
        String exceptionMessage = "Failed to get";
        Mockito.doThrow(new RuntimeException()).when(mockProvider).getCurrentRates();

        CurrencyRatesDto dto = service.getActualRates();

        assertTrue(dto.exception().get().getMessage().startsWith(exceptionMessage));
        assertTrue(dto.exception().get() instanceof RateDataSourceException);
        Mockito.inOrder(mockRepository, mockProvider);
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }

    @Test
    public void testGetActualRates_ErrorRepositoryResponse() {
        List<CurrencyRate> mockRates = Arrays.asList(
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("USD")).currencyRate(1.0).build(),
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("EUR")).currencyRate(0.9).build()
        );
        Mockito.doThrow(new RuntimeException()).when(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.when(mockProvider.getCurrentRates()).thenReturn(mockRates);

        CurrencyRatesDto dto = service.getActualRates();

        assertEquals(mockRates, dto.currencyRates());
        assertFalse(dto.isFallible());
        assertFalse(dto.isCached());
        Mockito.inOrder(mockRepository, mockProvider, mockRepository);
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockRepository).updateCurrencyRate(mockRates);
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }

    @Test
    void testGetRatesByDate_ReturnsEmptyList() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        List<CurrencyRate> emptyList = Collections.emptyList();
        Mockito.when(mockRepository.getCurrencyRates(date)).thenReturn(emptyList);

        CurrencyRatesDto dto =  service.getRates(date);

        assertEquals(dto.currencyRates(), emptyList);
        assertTrue(dto.exception().isEmpty());
        Mockito.verify(mockRepository).getCurrencyRates(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void testGetRatesByDate_Fail() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        String exceptionMessage = String.format("Failed to get %s currency rates:", date);
        Mockito.doThrow(new RuntimeException()).when(mockRepository).getCurrencyRates(date);

        CurrencyRatesDto dto = service.getRates(date);

        assertTrue(dto.exception().get() instanceof RateDataSourceException);
        assertTrue(dto.exception().get().getMessage().contains(exceptionMessage));
        Mockito.verify(mockRepository).getCurrencyRates(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void testGetRatesByDate_Success() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        List<CurrencyRate> mockRates = Arrays.asList(
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("USD")).currencyRate(10.0).build(),
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("EUR")).currencyRate(12.9).build()
        );
        Mockito.when(mockRepository.getCurrencyRates(date)).thenReturn(mockRates);

        CurrencyRatesDto dto = service.getRates(date);

        assertEquals(dto.currencyRates(), mockRates);
        Mockito.verify(mockRepository).getCurrencyRates(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }
}
