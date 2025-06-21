package re1kur.uas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re1kur.core.exception.*;

@RestControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload: \n" + ex.getMessage());
    }

    @ExceptionHandler(exception = TaskAlreadyExistException.class)
    public ResponseEntity<String> handleTaskAlreadyExistException(TaskAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(exception = TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(exception = TaskAttemptNotFoundException.class)
    public ResponseEntity<String> handleTaskAttemptNotFoundException(TaskAttemptNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(exception = UserTaskNotFoundException.class)
    public ResponseEntity<String> handleUserDailyTaskNotFoundException(UserTaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(exception = StatusUpdateFailedException.class)
    public ResponseEntity<String> handleStatusUpdateFailedException(StatusUpdateFailedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
