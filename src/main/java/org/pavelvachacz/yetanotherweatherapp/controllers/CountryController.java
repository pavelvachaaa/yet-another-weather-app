package org.pavelvachacz.yetanotherweatherapp.controllers;

import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.services.CountryService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/country")
public class CountryController {


    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // Get all countries
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    // Get country by ID
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable int id) {
        Optional<Country> country = countryService.getCountryById(id);
        return country.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get country by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Country> getCountryByName(@PathVariable String name) {
        Optional<Country> country = countryService.getCountryByName(name);
        return country.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new country
    @PostMapping
    public ResponseEntity<Boolean> createCountry(@RequestBody Country country) {
        return ResponseEntity.ok(countryService.createCountry(country));
    }

    // Create multiple countries
    @PostMapping("/batch")
    public ResponseEntity<int[]> createCountries(@RequestBody List<Country> countries) {
        return ResponseEntity.ok(countryService.createCountries(countries));
    }

    // Update country
    @PutMapping
    public ResponseEntity<Boolean> updateCountry(@RequestBody Country country) {
        return ResponseEntity.ok(countryService.updateCountry(country));
    }

    // Delete country
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCountry(@PathVariable int id) {
        return ResponseEntity.ok(countryService.deleteCountry(id));
    }
}
