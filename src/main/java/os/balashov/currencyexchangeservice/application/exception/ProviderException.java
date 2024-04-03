package os.balashov.currencyexchangeservice.application.exception;

public class ProviderException extends CurrencyRateException {
    public ProviderException(String message, RuntimeException exception) {
        super(message, exception);
    }
}
