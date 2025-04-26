package org.pavelvachacz.yetanotherweatherapp.exceptions;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String cityName) {
        super("City not found: " + cityName);
    }
}