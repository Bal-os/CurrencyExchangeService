package os.balashov.currencyexchangeservice.application.exeptions;

public class RepositoryException extends RuntimeException {
    boolean isEmpty;
    public RepositoryException(RuntimeException exception) {
        super(exception);
    }
    public RepositoryException(String massage, RuntimeException exception) {
        super(massage, exception);
    }
    public RepositoryException(String massage) {
        super(massage);
    }
}
