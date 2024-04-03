package os.balashov.currencyexchangeservice.application.exception;

public class RateDataSourceException extends RuntimeException {
    public RateDataSourceException(String message, RuntimeException exception) {
        super(message, exception);
    }

    public RateDataSourceException(String message) {
        super(message);
    }
}
