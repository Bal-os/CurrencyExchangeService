package os.balashov.currencyexchangeservice.application.exceptions;

public class CurrencyRateException extends RuntimeException {
    public CurrencyRateException(String massage, RuntimeException exception) {
        super(massage, exception);
    }
}
