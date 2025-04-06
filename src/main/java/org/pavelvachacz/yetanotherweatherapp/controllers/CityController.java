package org.pavelvachacz.yetanotherweatherapp.controllers;

import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Create a new city
    @PostMapping
    public ResponseEntity<String> createCity(@RequestBody City city) {
        boolean created = cityService.createCity(city);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("City created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create city");
        }
    }

    // Create multiple cities in batch
    @PostMapping("/batch")
    public ResponseEntity<String> createCities(@RequestBody List<City> cities) {
        int[] results = cityService.createCities(cities);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created " + results.length + " cities");
    }

    // Update an existing city
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCity(@PathVariable String id, @RequestBody City city) {
        try {
            int cityId = Integer.parseInt(id);

            // Ensure the path ID matches the city object ID
            if (city.getId() != cityId) {
                return ResponseEntity.badRequest().body("Path ID does not match city ID in request body");
            }

            // Check if city exists
            Optional<City> existingCity = cityService.getCityById(cityId);
            if (existingCity.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            boolean updated = cityService.updateCity(city);
            if (updated) {
                return ResponseEntity.ok("City updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update city");
            }
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Invalid city ID format");
        }
    }
    // Update resource partially - existing city
    @PatchMapping("/{id}")
    public ResponseEntity<String> partialUpdateCity(@PathVariable String id, @RequestBody City cityUpdate) {
        try {
            int cityId = Integer.parseInt(id);

            boolean updated = cityService.partialUpdateCity(cityId, cityUpdate);
            if (updated) {
                return ResponseEntity.ok("City updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Invalid city ID format");
        }
    }

    // Delete a city
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable String id) {
        try {
            int cityId = Integer.parseInt(id);

            // Check if city exists
            Optional<City> existingCity = cityService.getCityById(cityId);
            if (existingCity.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            boolean deleted = cityService.deleteCity(cityId);
            if (deleted) {
                return ResponseEntity.ok("City deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete city");
            }
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Invalid city ID format");
        }
    }


}
