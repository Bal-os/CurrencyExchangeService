package os.balashov.currencyexchangeservice.infrastucture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateEntity;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateHistory;
import os.balashov.currencyexchangeservice.infrastructure.data.repository.CurrencyRateCacheRepository;
import os.balashov.currencyexchangeservice.infrastructure.data.repository.CurrencyRateHistoryRepository;
import os.balashov.currencyexchangeservice.infrastructure.data.repository.CurrencyRateMutationRepository;
import os.balashov.currencyexchangeservice.infrastructure.data.service.CurrencyRateAdapter;
import os.balashov.currencyexchangeservice.infrastructure.data.utils.mapper.RateEntityMapper;
import os.balashov.currencyexchangeservice.infrastructure.data.utils.mapper.RateEntityMapperImpl;
import os.balashov.currencyexchangeservice.infrastructure.data.utils.mapper.RateHistoryMapper;
import os.balashov.currencyexchangeservice.utils.TestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CurrencyRateAdapterTest implements TestUtils {
    private RateHistoryMapper currencyRateHistoryMapper;
    private RateEntityMapper currencyRateEntityMapper;
    @InjectMocks
    private CurrencyRateAdapter currencyRateAdapter;
    @Mock
    private CurrencyRateCacheRepository rateCacheRepository;
    @Mock
    private CurrencyRateHistoryRepository rateHistoryRepository;
    @Mock
    private CurrencyRateMutationRepository historyMutationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyRateHistoryMapper = new RateEntityMapperImpl();
        currencyRateEntityMapper = new RateEntityMapperImpl();
        currencyRateAdapter = new CurrencyRateAdapter(rateCacheRepository, rateHistoryRepository, historyMutationRepository, currencyRateHistoryMapper, currencyRateEntityMapper);
    }

    private CurrencyRateEntity createMockCurrencyRateEntity() {
        return createMockCurrencyRateEntity(LocalDate.now());
    }

    private CurrencyRateEntity createMockCurrencyRateEntity(LocalDate date) {
        return currencyRateEntityMapper.toCurrencyRateEntity(createMockCurrencyRates(date).get(0));
    }

    private CurrencyRateHistory createMockCurrencyRateHistory(LocalDate date) {
        return currencyRateHistoryMapper.toCurrencyRateHistoryEntity(createMockCurrencyRates(date).get(0));
    }

    @Test
    void updateCurrencyRate_shouldDeleteCacheAndSaveEntities() {
        List<CurrencyRate> currencyRates = createMockCurrencyRates(LocalDate.now());

        currencyRateAdapter.updateCurrencyRate(currencyRates);

        verify(rateCacheRepository, times(1)).deleteAll();
        verify(rateCacheRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    public void updateCurrencyRate_shouldUpdateCurrencyRateWithValidData() {
        LocalDate date = LocalDate.now();
        LocalDate historyDate = date.minusDays(1);
        List<CurrencyRate> mockRates = createMockCurrencyRates(date);
        List<CurrencyRate> historyRates = createMockCurrencyRates(historyDate);
        List<CurrencyRateEntity> mockEntities = mockRates.stream()
                .map(currencyRateEntityMapper::toCurrencyRateEntity)
                .toList();
        List<CurrencyRateHistory> historyEntities = historyRates.stream()
                .map(currencyRateHistoryMapper::toCurrencyRateHistoryEntity)
                .toList();
        when(rateCacheRepository.isValidDataPresent()).thenReturn(true);
        when(rateCacheRepository.findAllByDateGreaterThanEqual(date)).thenReturn(mockEntities);
        when(rateHistoryRepository.findByDate(historyDate)).thenReturn(historyEntities);
        when(rateCacheRepository.saveAllAndFlush(mockEntities)).thenReturn(mockEntities);

        currencyRateAdapter.updateCurrencyRate(mockRates);

        verify(rateHistoryRepository, times(1)).deleteByDate(date);
        verify(historyMutationRepository, times(1)).copyDataFromActualTableToHistory();
        verify(rateCacheRepository, times(1)).deleteAll();
        verify(rateCacheRepository, times(1)).saveAllAndFlush(mockEntities);
    }

    @Test
    public void testGetCurrencyRates_ShouldReturnCurrencyRatesFromCache() {
        LocalDate today = LocalDate.now();
        List<CurrencyRateEntity> mockEntities = Collections.singletonList(createMockCurrencyRateEntity());
        when(rateCacheRepository.findAllByDateGreaterThanEqual(today)).thenReturn(mockEntities);

        List<CurrencyRate> retrievedRates = currencyRateAdapter.getCurrencyRates(today);

        List<CurrencyRate> expectedRates = mockEntities.stream()
                .map(currencyRateEntityMapper::toCurrencyRate)
                .collect(Collectors.toList());
        assertEquals(expectedRates, retrievedRates);
    }

    @Test
    public void testGetCurrencyRates_ShouldReturnCurrencyRatesFromHistory() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        List<CurrencyRateHistory> mockHistory = Collections.singletonList(createMockCurrencyRateHistory(pastDate));
        when(rateHistoryRepository.findByDate(pastDate)).thenReturn(mockHistory);

        List<CurrencyRate> retrievedRates = currencyRateAdapter.getCurrencyRates(pastDate);

        List<CurrencyRate> expectedRates = mockHistory.stream()
                .map(currencyRateHistoryMapper::toCurrencyRate)
                .collect(Collectors.toList());
        assertEquals(expectedRates, retrievedRates);
    }

    @Test
    public void testDeleteCurrencyRate_ShouldDeleteCurrencyRatePastDate() {
        LocalDate date = LocalDate.now().minusDays(1);

        currencyRateAdapter.deleteCurrencyRate(date);

        verify(rateHistoryRepository, times(1)).deleteByDate(date);
    }

    @Test
    public void testDeleteCurrencyRate_ShouldVerifyNoInteractionWithHistoryRepository() {
        LocalDate today = LocalDate.now();

        currencyRateAdapter.deleteCurrencyRate(today);

        verify(rateHistoryRepository, times(0)).deleteByDate(today);
    }
}
