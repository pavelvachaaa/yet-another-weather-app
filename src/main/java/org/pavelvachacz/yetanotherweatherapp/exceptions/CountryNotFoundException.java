package org.pavelvachacz.yetanotherweatherapp.exceptions;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(String message) {
        super(message);
    }
}