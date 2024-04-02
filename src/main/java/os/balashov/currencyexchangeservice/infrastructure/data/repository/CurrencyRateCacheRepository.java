package os.balashov.currencyexchangeservice.infrastructure.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateEntity;

public interface CurrencyRateCacheRepository extends JpaRepository<CurrencyRateEntity, Long> {
}
