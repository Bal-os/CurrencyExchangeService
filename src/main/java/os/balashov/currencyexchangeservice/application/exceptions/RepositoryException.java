package os.balashov.currencyexchangeservice.application.exceptions;

public class RepositoryException extends CurrencyRateException {
    public RepositoryException(String massage, RuntimeException exception) {
        super(massage, exception);
    }
}
