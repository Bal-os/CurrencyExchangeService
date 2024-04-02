package os.balashov.currencyexchangeservice.infrastructure.data.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateRepository;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateEntity;
import os.balashov.currencyexchangeservice.infrastructure.data.repository.CurrencyRateCacheRepository;
import os.balashov.currencyexchangeservice.infrastructure.data.repository.CurrencyRateHistoryRepository;
import os.balashov.currencyexchangeservice.infrastructure.data.repository.CurrencyRateMutationRepository;
import os.balashov.currencyexchangeservice.infrastructure.data.utils.mapper.RateEntityMapper;
import os.balashov.currencyexchangeservice.infrastructure.data.utils.mapper.RateHistoryMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class CurrencyRateAdapter implements CurrencyRateRepository {
    private final CurrencyRateCacheRepository rateCacheRepository;
    private final CurrencyRateHistoryRepository rateHistoryRepository;
    private final CurrencyRateMutationRepository historyMutationRepository;
    private final RateHistoryMapper currencyRateHistoryMapper;
    private final RateEntityMapper currencyRateEntityMapper;

    @Override
    @Async
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void updateCurrencyRate(List<CurrencyRate> currencyRates) {
        log.info("Updating currency rates: {}", currencyRates);
        if (currencyRates.isEmpty()) {
            return;
        }

        if (isRateCacheNotEmpty()) {
            LocalDate date = currencyRates.isEmpty() ? LocalDate.now() : currencyRates.get(0).currencyDate();
            rateHistoryRepository.deleteByDate(date);
            historyMutationRepository.copyDataFromActualTableToHistory();
        }
        rateCacheRepository.deleteAll();

        List<CurrencyRateEntity> rateEntities = currencyRates.parallelStream()
                .map(currencyRateEntityMapper::toCurrencyRateEntity)
                .toList();
        rateCacheRepository.saveAllAndFlush(rateEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyRate> getCurrencyRates(LocalDate date) {
        log.info("Getting currency rates for date: {}", date);
        if (LocalDate.now().isAfter(date)) {
            return rateHistoryRepository.findByDate(date)
                    .parallelStream()
                    .map(currencyRateHistoryMapper::toCurrencyRate)
                    .toList();
        } else {
            return rateCacheRepository.findAll()
                    .parallelStream()
                    .map(currencyRateEntityMapper::toCurrencyRate)
                    .filter(currencyRate -> isNotOld(date, currencyRate))
                    .toList();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteCurrencyRate(LocalDate date) {
        log.info("Deleting currency rates for date: {}", date);
        if (!LocalDate.now().equals(date)) {
            rateHistoryRepository.deleteByDate(date);
        }
    }

    private boolean isRateCacheNotEmpty() {
        return !rateCacheRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .parallel()
                .filter(this::isRateEntityValid)
                .toList()
                .isEmpty();
    }

    private boolean isRateEntityValid(CurrencyRateEntity rate) {
        return Objects.nonNull(rate.getDate())
                && Objects.nonNull(rate.getRate())
                && Objects.nonNull(rate.getNumberCode())
                && Objects.nonNull(rate.getCode())
                && Objects.nonNull(rate.getName())
                && Objects.nonNull(rate.getTimestamp());
    }

    private boolean isNotOld(LocalDate expectedDay, CurrencyRate currencyRate) {
        return !expectedDay.isAfter(currencyRate.currencyDate());
    }
}
