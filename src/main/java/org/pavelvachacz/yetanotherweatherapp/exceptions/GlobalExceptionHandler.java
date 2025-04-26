package org.pavelvachacz.yetanotherweatherapp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle invalid path variable types (e.g., /city/abc instead of /city/123)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPathVariable(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format(
                "Invalid value for %s: '%s'. Expected type: %s.",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()
        );
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Path Variable",
                errorMessage,
                HttpStatus.BAD_REQUEST.value(),
                Instant.now().toString()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Handle CityNotFoundException
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCityNotFoundException(CityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "City Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // (Optional) Handle any other unexpected exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @Setter
    @Getter
    public static class ErrorResponse {
        private String error;
        private String message;
        private int status;
        private String timestamp;

        public ErrorResponse(String error, String message, int status, String timestamp) {
            this.error = error;
            this.message = message;
            this.status = status;
            this.timestamp = timestamp;
        }
    }
}
