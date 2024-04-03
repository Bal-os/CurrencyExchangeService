package os.balashov.currencyexchangeservice.application.exception;

public class RepositoryException extends CurrencyRateException {
    public RepositoryException(String message, RuntimeException exception) {
        super(message, exception);
    }
}
