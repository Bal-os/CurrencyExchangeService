package os.balashov.currencyexchangeservice.application.exceptions;

public class ProviderException extends CurrencyRateException {
    public ProviderException(String massage, RuntimeException exception) {
        super(massage, exception);
    }
}
