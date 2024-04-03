package os.balashov.currencyexchangeservice.application.exception;

public class CurrencyRateException extends RuntimeException {
    public CurrencyRateException(String message, RuntimeException exception) {
        super(message, exception);
    }

    public CurrencyRateException(String message) {
        super(message);
    }
}
