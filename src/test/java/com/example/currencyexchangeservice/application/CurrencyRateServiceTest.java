package com.example.currencyexchangeservice.application;

import com.example.currencyexchangeservice.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exceptions.RepositoryException;
import os.balashov.currencyexchangeservice.application.service.CurrencyRateService;
import os.balashov.currencyexchangeservice.application.utils.CurrencyRateBuilder;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.domain.entity.Currency;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyRateServiceTest implements TestUtils {
    @Mock
    private CurrencyRateRepository mockRepository;
    private CurrencyRateService service;
    private Map<String, Currency> codeCurrencyMap;

    @BeforeEach
    public void setUp() {
        mockRepository = Mockito.mock(CurrencyRateRepository.class);
        service = new CurrencyRateService(mockRepository);

        codeCurrencyMap = codeCurrencyMap();
    }

    @Test
    public void testDeleteRatesByDate_Success() throws RepositoryException {
        LocalDate date = LocalDate.of(2024, 3, 29);
        Mockito.doNothing().when(mockRepository).deleteCurrencyRate(date);

        service.deleteRatesByDate(date);

        Mockito.verify(mockRepository).deleteCurrencyRate(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void testDeleteRatesByDate_Fail() throws RepositoryException {
        LocalDate date = LocalDate.of(2023, 12, 31);
        String message = String.format("Failed to delete %s currency rates:", date);
        Mockito.doThrow(new RuntimeException()).when(mockRepository).deleteCurrencyRate(date);

        CurrencyRatesDto dto = service.deleteRatesByDate(date);

        assertTrue(dto.getException().getMessage().contains(message));
        Mockito.verify(mockRepository).deleteCurrencyRate(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void testGetRatesByDate_ReturnsEmptyList() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        List<CurrencyRate> emptyList = Collections.emptyList();
        Mockito.when(mockRepository.getCurrencyRates(date)).thenReturn(emptyList);

        CurrencyRatesDto dto =  service.getRatesByDate(date);

        assertEquals(dto.getCurrencyRates(), emptyList);
        assertNull(dto.getException());
        Mockito.verify(mockRepository).getCurrencyRates(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    void testGetRatesByDate_Fail() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        String exceptionMessage = String.format("Failed to get %s currency rates:", date);
        Mockito.doThrow(new RuntimeException()).when(mockRepository).getCurrencyRates(date);

        CurrencyRatesDto dto = service.getRatesByDate(date);

        assertTrue(dto.getException() instanceof RepositoryException);
        assertTrue(dto.getException().getMessage().contains(exceptionMessage));
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

        CurrencyRatesDto dto = service.getRatesByDate(date);

        assertEquals(dto.getCurrencyRates(), mockRates);
        Mockito.verify(mockRepository).getCurrencyRates(date);
        Mockito.verifyNoMoreInteractions(mockRepository);
    }
}
