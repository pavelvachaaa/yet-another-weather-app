package org.pavelvachacz.yetanotherweatherapp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle the case where path variable type mismatch occurs (e.g., non-numeric input)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPathVariable(MethodArgumentTypeMismatchException ex) {
        // Construct a custom error message
        String errorMessage = String.format("Invalid value for %s: '%s'. Expected type: %s.",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        ErrorResponse errorResponse = new ErrorResponse("Invalid Path Variable", errorMessage);

        // Return the error response with HTTP 400 Bad Request
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // ErrorResponse class
    @Setter
    @Getter
    public static class ErrorResponse {
        // Getters and Setters
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

    }
}
