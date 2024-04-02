package os.balashov.currencyexchangeservice.application.exception;

public class ProviderException extends CurrencyRateException {
    public ProviderException(String massage, RuntimeException exception) {
        super(massage, exception);
    }
}
