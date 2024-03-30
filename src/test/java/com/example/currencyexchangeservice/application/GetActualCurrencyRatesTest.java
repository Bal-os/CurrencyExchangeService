package com.example.currencyexchangeservice.application;

import com.example.currencyexchangeservice.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import os.balashov.currencyexchangeservice.application.exeptions.ProviderException;
import os.balashov.currencyexchangeservice.application.exeptions.RepositoryException;
import os.balashov.currencyexchangeservice.application.service.GetActualCurrencyRatesService;
import os.balashov.currencyexchangeservice.application.utils.CurrencyRateBuilder;
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

public class GetActualCurrencyRatesTest implements TestUtils {
    @Mock
    private CurrencyRateProvider mockProvider;
    @Mock
    private CurrencyRateRepository mockRepository;
    private GetActualCurrencyRatesService service;
    private Map<String, Currency> codeCurrencyMap;
    @BeforeEach
    public void setUp() {
        mockProvider = Mockito.mock(CurrencyRateProvider.class);
        mockRepository = Mockito.mock(CurrencyRateRepository.class);
        service = new GetActualCurrencyRatesService(mockRepository, mockProvider);

        codeCurrencyMap = codeCurrencyMap();
    }

    @Test
    public void testGetActualRates_Success() throws ProviderException, RepositoryException {
        List<CurrencyRate> mockRates = Arrays.asList(
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("USD")).currencyRate(1.0).build(),
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("EUR")).currencyRate(0.9).build()
        );
        Mockito.when(mockProvider.getCurrentRates()).thenReturn(mockRates);
        Mockito.doNothing().when(mockRepository).updateCurrencyRate(mockRates);
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class)))
                .thenReturn(Collections.emptyList()).thenReturn(mockRates);

        List<CurrencyRate> actualRates = service.getActualRates();

        assertEquals(mockRates, actualRates);
        Mockito.inOrder(mockRepository, mockProvider, mockRepository, mockRepository);
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockRepository).updateCurrencyRate(mockRates);
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }

    @Test
    public void testGetActualRates_EmptyProviderResponse() {
        List<CurrencyRate> emptyList = Collections.emptyList();
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenReturn(emptyList);
        Mockito.when(mockProvider.getCurrentRates()).thenReturn(emptyList);

        List<CurrencyRate> actualRates = service.getActualRates();

        assertEquals(actualRates, emptyList);
        assertTrue(actualRates.isEmpty());
        Mockito.inOrder(mockRepository, mockProvider);
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }

    @Test
    public void testGetActualRates_ErrorProviderResponse() {
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenReturn(Collections.emptyList());
        Exception expectedException = new RuntimeException("mock");
        Mockito.doThrow(expectedException).when(mockProvider).getCurrentRates();

        Exception exception = assertThrows(ProviderException.class, () -> service.getActualRates());

        assertTrue(exception.getMessage().endsWith(expectedException.getMessage()));
        Mockito.inOrder(mockRepository, mockProvider);
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }

    @Test
    public void testGetActualRates_SuccessRepositoryResponse() {
        List<CurrencyRate> mockRates = Arrays.asList(
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("USD")).currencyRate(1.0).build(),
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("EUR")).currencyRate(0.9).build()
        );
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenReturn(mockRates);

        List<CurrencyRate> actualRates = service.getActualRates();

        assertEquals(mockRates, actualRates);
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    public void testGetActualRates_UnsuccessfulRepositoryUpdate() {
        List<CurrencyRate> mockRates = Arrays.asList(
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("USD")).currencyRate(1.0).build(),
                CurrencyRateBuilder.builder().currency(codeCurrencyMap.get("EUR")).currencyRate(0.9).build()
        );
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenReturn(Collections.emptyList());
        Mockito.when(mockProvider.getCurrentRates()).thenReturn(mockRates);
        Mockito.doThrow(new RuntimeException("mock")).when(mockRepository).updateCurrencyRate(mockRates);

        List<CurrencyRate> actualRates = service.getActualRates();

        assertEquals(mockRates, actualRates);
        Mockito.inOrder(mockRepository, mockProvider, mockRepository);
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockRepository).updateCurrencyRate(mockRates);
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }
}
