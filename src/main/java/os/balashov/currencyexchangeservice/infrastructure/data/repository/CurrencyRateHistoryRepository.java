package os.balashov.currencyexchangeservice.infrastructure.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateHistory;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateHistoryRepository extends JpaRepository<CurrencyRateHistory, Long> {
    List<CurrencyRateHistory> findByDate(LocalDate date);
    void deleteByDate(LocalDate date);
}
