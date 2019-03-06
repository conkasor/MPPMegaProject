package repository;

public class RepositoryException extends RuntimeException {
     RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(Exception ex) {
        super(ex);
    }
}
