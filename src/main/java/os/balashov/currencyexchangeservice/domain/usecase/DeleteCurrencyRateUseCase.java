package os.balashov.currencyexchangeservice.domain.usecase;

import java.time.LocalDate;

public interface DeleteCurrencyRateUseCase extends GetCurrencyRatesUseCase {
    void deleteRatesByDate(LocalDate currencyDate);
}
