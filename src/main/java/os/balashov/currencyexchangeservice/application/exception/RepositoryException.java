package os.balashov.currencyexchangeservice.application.exception;

public class RepositoryException extends RateDataSourceException {
    public RepositoryException(String message, RuntimeException exception) {
        super(message, exception);
    }
}
