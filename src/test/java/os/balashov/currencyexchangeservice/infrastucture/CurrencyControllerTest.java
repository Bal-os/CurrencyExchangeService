package os.balashov.currencyexchangeservice.infrastucture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import os.balashov.currencyexchangeservice.TestUtils;
import os.balashov.currencyexchangeservice.application.dto.CurrencyRatesDto;
import os.balashov.currencyexchangeservice.application.exception.CurrencyRateException;
import os.balashov.currencyexchangeservice.application.usecase.DeleteCurrencyRateUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetActualCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.application.usecase.GetCurrencyRatesUseCase;
import os.balashov.currencyexchangeservice.infrastructure.rest.CurrencyController;
import os.balashov.currencyexchangeservice.infrastructure.rest.CurrencyResponseDto;

import java.time.LocalDate;
import java.util.Collections;

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
    public void testGetActualRates() {
    CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
            .currencyRates(createMockCurrencyRates(LocalDate.now()))
            .build();

    when(getActualCurrencyRatesUseCase.getActualRates()).thenReturn(currencyRatesDto);

    ResponseEntity<CurrencyResponseDto> response = currencyController.getActualRates();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(getActualCurrencyRatesUseCase, times(1)).getActualRates();
    }

    @Test
    public void testGetRatesByDate() {
        String date = "2022-12-01";
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
                .currencyRates(createMockCurrencyRates(LocalDate.parse(date)))
                .build();
        when(getCurrencyRatesUseCase.getRatesByDate(any(LocalDate.class))).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.getRatesByDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertNull(response.getBody().getMessage());
        assertEquals(currencyRatesDto.getCurrencyRates(), response.getBody().getCurrencyRates());
        verify(getCurrencyRatesUseCase, times(1)).getRatesByDate(any(LocalDate.class));
    }

    @Test
    public void testGetActualRatesWhenNoRates() {
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
                .currencyRates(Collections.emptyList())
                .build();
        when(getActualCurrencyRatesUseCase.getActualRates()).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.getActualRates();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertNull(response.getBody().getMessage());
        verify(getActualCurrencyRatesUseCase, times(1)).getActualRates();
    }

    @Test
    public void testGetRatesByDateWhenNoRates() {
        String date = "2022-12-01";
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
                .currencyRates(Collections.emptyList())
                .build();
        when(getCurrencyRatesUseCase.getRatesByDate(any(LocalDate.class))).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.getRatesByDate(date);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertNull(response.getBody().getMessage());
        verify(getCurrencyRatesUseCase, times(1)).getRatesByDate(any(LocalDate.class));
    }

    @Test
    public void testGetRatesByDateWhenDateIsEmpty() {
        String date = "";
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
                .currencyRates(createMockCurrencyRates(LocalDate.now()))
                .build();
        when(getActualCurrencyRatesUseCase.getActualRates()).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.getRatesByDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertNull(response.getBody().getMessage());
        assertEquals(currencyRatesDto.getCurrencyRates(), response.getBody().getCurrencyRates());
        verify(getActualCurrencyRatesUseCase, times(1)).getActualRates();
    }

    @Test
    public void testDeleteRatesByDate() {
        String date = "2022-12-01";
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
                .currencyRates(Collections.emptyList())
                .build();
        when(deleteCurrencyRateUseCase.deleteRatesByDate(any(LocalDate.class))).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.deleteRatesByDate(date);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertNull(response.getBody().getMessage());
        verify(deleteCurrencyRateUseCase, times(1)).deleteRatesByDate(any(LocalDate.class));
    }

    @Test
    public void testDeleteRatesByDateWhenDateIsInvalid() {
        String date = "invalid-date";
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
                .currencyRates(createMockCurrencyRates(LocalDate.now()))
                .build();
        when(deleteCurrencyRateUseCase.deleteRatesByDate(any(LocalDate.class))).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.deleteRatesByDate(date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertNotNull(response.getBody().getMessage());
        verify(deleteCurrencyRateUseCase, times(0)).deleteRatesByDate(any(LocalDate.class));
    }

    @Test
    public void testDeleteRatesByDateWhenRatesExist() {
        String date = "2022-12-01";
        CurrencyRateException exception = new CurrencyRateException("Failed to delete rates");
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
                .exception(exception)
                .build();
        when(deleteCurrencyRateUseCase.deleteRatesByDate(any(LocalDate.class))).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.deleteRatesByDate(date);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertNotNull(response.getBody().getMessage());
        assertTrue(response.getBody().getMessage().startsWith(exception.getMessage()));
        verify(deleteCurrencyRateUseCase, times(1)).deleteRatesByDate(any(LocalDate.class));
    }

    @Test
    public void testDeleteRatesByDateWhenRatesNotExist() {
        String date = "2022-12-01";
        CurrencyRatesDto currencyRatesDto = CurrencyRatesDto.builder()
                .currencyRates(Collections.emptyList())
                .build();
        when(deleteCurrencyRateUseCase.deleteRatesByDate(any(LocalDate.class))).thenReturn(currencyRatesDto);

        ResponseEntity<CurrencyResponseDto> response = currencyController.deleteRatesByDate(date);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertNull(response.getBody().getMessage());
        verify(deleteCurrencyRateUseCase, times(1)).deleteRatesByDate(any(LocalDate.class));
    }
}
