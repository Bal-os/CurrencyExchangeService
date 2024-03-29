package os.balashov.currencyexchangeservice.application.exeptions;

public class ProviderException extends RuntimeException {
    public ProviderException(RuntimeException exception) {
        super(exception);
    }
    public ProviderException(String massage, RuntimeException exception) {
        super(massage, exception);
    }
    public ProviderException(String massage) {
        super(massage);
    }
}
