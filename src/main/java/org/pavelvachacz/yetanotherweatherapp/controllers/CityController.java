package org.pavelvachacz.yetanotherweatherapp.controllers;

import org.pavelvachacz.yetanotherweatherapp.exceptions.GlobalExceptionHandler;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    // Get all cities
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    // Get city by ID
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable String id) {
        // We can parse the string to an integer safely here (with fallback to error handler if invalid)
        try {
            int cityId = Integer.parseInt(id);
            Optional<City> city = cityService.getCityById(cityId);
            return city.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (NumberFormatException ex) {
            // This case will be caught by GlobalExceptionHandler if it's a path variable issue
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<City> getCityByName(@PathVariable String name) {
        Optional<City> city = cityService.getCityByName(name);
        return city.map(ResponseEntity::ok)  // If city is present, return it
                .orElseGet(() -> ResponseEntity.notFound().build()); // If city is absent, return 404
    }


}
