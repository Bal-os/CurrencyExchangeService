package os.balashov.currencyexchangeservice.infrastructure.data.repository;

public interface CurrencyRateMutationRepository {
    void copyDataFromActualTableToHistory();
}
