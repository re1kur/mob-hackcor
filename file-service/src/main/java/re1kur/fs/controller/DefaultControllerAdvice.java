package re1kur.fs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import re1kur.core.exception.FileNotFoundException;
import re1kur.core.exception.UrlUpdateException;

@RestControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler(exception = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body("Invalid file id: " + ex.getMessage());
    }

    @ExceptionHandler(exception = FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(exception = UrlUpdateException.class)
    public ResponseEntity<String> handleUrlUpdateException(UrlUpdateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
