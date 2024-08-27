package farm.exceptions;

public class RepeatedInstanceException extends RuntimeException {
    public RepeatedInstanceException(String message) {
        super(message);
    }
}
