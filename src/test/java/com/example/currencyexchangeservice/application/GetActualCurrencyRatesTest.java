package com.example.currencyexchangeservice.application;

import com.example.currencyexchangeservice.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exceptions.ProviderException;
import os.balashov.currencyexchangeservice.application.exceptions.RepositoryException;
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

        CurrencyRatesDto dto = service.getActualRates();

        assertEquals(mockRates, dto.getCurrencyRates());
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

        assertEquals(mockRates, dto.getCurrencyRates());
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    public void testGetActualRates_EmptyProviderResponse() {
        List<CurrencyRate> emptyList = Collections.emptyList();
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenReturn(emptyList);
        Mockito.when(mockProvider.getCurrentRates()).thenReturn(emptyList);

        CurrencyRatesDto dto = service.getActualRates();

        assertEquals(dto.getCurrencyRates(), emptyList);
        assertTrue(dto.getCurrencyRates().isEmpty());
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

        assertTrue(dto.getException().getMessage().startsWith(exceptionMessage));
        assertTrue(dto.getException() instanceof ProviderException);
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
        String exceptionMessage = "Failed to get";
        Mockito.doThrow(new RuntimeException()).when(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.when(mockProvider.getCurrentRates()).thenReturn(mockRates);
        Mockito.when(mockRepository.getCurrencyRates(any(LocalDate.class))).thenThrow(new RuntimeException());

        CurrencyRatesDto dto = service.getActualRates();

        assertEquals(mockRates, dto.getCurrencyRates());
        assertTrue(dto.getException().getMessage().startsWith(exceptionMessage));
        assertTrue(dto.getException() instanceof RepositoryException);
        Mockito.inOrder(mockRepository, mockProvider, mockRepository);
        Mockito.verify(mockProvider).getCurrentRates();
        Mockito.verify(mockRepository).getCurrencyRates(any(LocalDate.class));
        Mockito.verify(mockRepository).updateCurrencyRate(mockRates);
        Mockito.verifyNoMoreInteractions(mockProvider, mockRepository);
    }
}
