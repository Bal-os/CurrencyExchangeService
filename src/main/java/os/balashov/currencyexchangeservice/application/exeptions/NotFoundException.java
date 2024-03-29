package os.balashov.currencyexchangeservice.application.exeptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(RuntimeException exception) {
        super(exception);
    }
    public NotFoundException(String massage, RuntimeException exception) {
        super(massage, exception);
    }
    public NotFoundException(String massage) {
        super(massage);
    }
}
