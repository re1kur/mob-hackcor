package re1kur.core.exception;

public class TokenHasExpiredException extends RuntimeException {
    public TokenHasExpiredException(String message) {
        super(message);
    }
}
