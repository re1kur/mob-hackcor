package re1kur.core.exception;

public class TokenDidNotPassVerificationException extends RuntimeException {
    public TokenDidNotPassVerificationException(String message) {
        super(message);
    }
}
