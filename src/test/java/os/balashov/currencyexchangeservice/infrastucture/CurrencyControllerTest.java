package os.balashov.currencyexchangeservice.infrastucture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.infrastructure.rest.CurrencyController;
import os.balashov.currencyexchangeservice.infrastructure.rest.CurrencyResponseDto;
import os.balashov.currencyexchangeservice.utils.CurrencyRatesDtoBuilder;
import os.balashov.currencyexchangeservice.utils.TestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyControllerTest implements TestUtils {
    @Mock
    private DeleteCurrencyRateUseCase deleteCurrencyRateUseCase;
    @Mock
    private GetActualCurrencyRatesUseCase getActualCurrencyRatesUseCase;
    @Mock
    private GetCurrencyRatesUseCase getCurrencyRatesUseCase;
    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRatesByDateWhenDateIsValid() {
        String date = "2022-12-01";
        LocalDate localDate = LocalDate.parse(date);
        List<CurrencyRate> currencyRates = createMockCurrencyRates(localDate);
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDtoBuilder.builder()
                .currencyRates(currencyRates)
                .build();
        when(getCurrencyRatesUseCase.getRates(any(LocalDate.class))).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.getRatesByDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
        assertEquals(currencyRates, response.getBody().getData());
        assertTrue(response.getBody().getData().stream().anyMatch(currencyRate -> currencyRate.currencyDate().equals(localDate)));
        verify(getCurrencyRatesUseCase, times(1)).getRates(any(LocalDate.class));
    }

    @Test
    public void testGetRatesByDateWhenDateIsInvalid() {
        String date = "invalid-date";

        ResponseEntity<CurrencyResponseDto> response = currencyController.getRatesByDate(date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        verify(getCurrencyRatesUseCase, times(0)).getRates(any(LocalDate.class));
    }

    @Test
    public void testGetRatesByDateWhenDateIsEmpty() {
        String date = "";
        LocalDate now = LocalDate.now();
        List<CurrencyRate> currencyRates = createMockCurrencyRates(now);
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDtoBuilder.builder()
                .currencyRates(currencyRates)
                .build();
        when(getActualCurrencyRatesUseCase.getActualRates()).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.getRatesByDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
        assertEquals(currencyRates, response.getBody().getData());
        assertTrue(response.getBody().getData().stream().anyMatch(currencyRate -> currencyRate.currencyDate().equals(now)));
        verify(getActualCurrencyRatesUseCase, times(1)).getActualRates();
    }

    @Test
    public void testDeleteRatesByDateWhenDateIsValid() {
        String date = "2022-12-01";
        when(deleteCurrencyRateUseCase.deleteRates(any(LocalDate.class))).thenReturn(Optional.empty());

        ResponseEntity<CurrencyResponseDto> response = currencyController.deleteRatesByDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        verify(deleteCurrencyRateUseCase, times(1)).deleteRates(any(LocalDate.class));
    }

    @Test
    public void testDeleteRatesByDateWhenDateIsInvalid() {
        String date = "invalid-date";

        ResponseEntity<CurrencyResponseDto> response = currencyController.deleteRatesByDate(date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        verify(deleteCurrencyRateUseCase, times(0)).deleteRates(any(LocalDate.class));
    }

    @Test
    public void testDeleteRatesByDateWhenDateIsEmpty() {
        String date = "";

        ResponseEntity<CurrencyResponseDto> response = currencyController.deleteRatesByDate(date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        verify(deleteCurrencyRateUseCase, times(0)).deleteRates(any(LocalDate.class));
    }
}
