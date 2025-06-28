package re1kur.ums.controller;

import org.apache.el.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re1kur.core.exception.*;

import java.util.stream.Collectors;

@RestControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().
                getFieldErrors().stream().
                map(err -> "%s : %s".formatted(err.getField(),
                        err.getDefaultMessage())).collect(Collectors.joining("|"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request accessToken:\n" + errors);
    }

    @ExceptionHandler(exception = UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(exception = UserAlreadyRegisteredException.class)
    public ResponseEntity<String> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(exception = InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(exception = ParseException.class)
    public ResponseEntity<String> handleParseException(ParseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: " + ex.getMessage());
    }

    @ExceptionHandler(exception = TokenDidNotPassVerificationException.class)
    public ResponseEntity<String> handleInvalidTokenException(TokenDidNotPassVerificationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(exception = TokenNotFoundException.class)
    public ResponseEntity<String> handleTokenNotFoundException(TokenNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(exception = TokenMismatchException.class)
    public ResponseEntity<String> handleTokenMismatchException(TokenMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(exception = TokenHasExpiredException.class)
    public ResponseEntity<String> handleTokenHasExpiredException(TokenHasExpiredException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(ex.getMessage());
    }

    @ExceptionHandler(exception = CodeMismatchException.class)
    public ResponseEntity<String> handleCodeMismatchException(CodeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(exception = CodeHasExpiredException.class)
    public ResponseEntity<String> handleCodeHasExpiredException(CodeHasExpiredException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(ex.getMessage());
    }

    @ExceptionHandler(exception = CodeNotFoundException.class)
    public ResponseEntity<String> handleCodeNotFoundException(CodeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(exception = UserAlreadyVerifiedException.class)
    public ResponseEntity<String> handleUserAlreadyVerifiedException(UserAlreadyVerifiedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
