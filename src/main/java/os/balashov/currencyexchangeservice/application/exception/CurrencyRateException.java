package os.balashov.currencyexchangeservice.application.exception;

public class CurrencyRateException extends RuntimeException {
    public CurrencyRateException(String massage, RuntimeException exception) {
        super(massage, exception);
    }

    public CurrencyRateException(String massage) {
        super(massage);
    }
}
