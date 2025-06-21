package re1kur.core.exception;

public class TaskAttemptNotFoundException extends RuntimeException {
    public TaskAttemptNotFoundException(String message) {
        super(message);
    }
}
