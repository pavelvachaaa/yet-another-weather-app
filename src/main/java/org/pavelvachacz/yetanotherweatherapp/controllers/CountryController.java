package org.pavelvachacz.yetanotherweatherapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.services.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
@Tag(name = "Country Management", description = "APIs for managing country information")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(summary = "Get all countries", description = "Returns a list of all countries in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Countries found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Country.class)))),
            @ApiResponse(responseCode = "204", description = "No countries found")
    })
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryService.getCountries();
        return countries.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(countries);
    }

    @Operation(summary = "Get country by ID", description = "Returns a country based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Country found",
                    content = @Content(schema = @Schema(implementation = Country.class))),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(
            @Parameter(description = "ID of the country to retrieve") @PathVariable int id) {
        Country country = countryService.getCountryById(id);
        return ResponseEntity.ok(country);
    }

    @Operation(summary = "Get country by name", description = "Returns a country based on its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Country found",
                    content = @Content(schema = @Schema(implementation = Country.class))),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Country> getCountryByName(
            @Parameter(description = "Name of the country to retrieve") @PathVariable String name) {
        Country country = countryService.getCountryByName(name);
        return ResponseEntity.ok(country);
    }

    @Operation(summary = "Create a new country", description = "Creates a new country entry in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Country created successfully",
                    content = @Content(schema = @Schema(implementation = Country.class))),
            @ApiResponse(responseCode = "400", description = "Invalid country data provided")
    })
    @PostMapping
    public ResponseEntity<Country> createCountry(
            @Parameter(description = "Country object to be created", required = true,
                    schema = @Schema(implementation = Country.class, description = "Country with name"))
            @RequestBody Country country) {
        Country createdCountry = countryService.createCountry(country);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCountry);
    }

    @Operation(summary = "Update a country", description = "Updates all fields of an existing country")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Country updated successfully",
                    content = @Content(schema = @Schema(implementation = Country.class))),
            @ApiResponse(responseCode = "404", description = "Country not found"),
            @ApiResponse(responseCode = "400", description = "Invalid country data provided")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(
            @Parameter(description = "ID of the country to update") @PathVariable int id,
            @Parameter(description = "Updated country information", required = true,
                    schema = @Schema(implementation = Country.class, description = "Country with updated name"))
            @RequestBody Country countryUpdate) {
        Country updatedCountry = countryService.updateCountry(id, countryUpdate);
        return ResponseEntity.ok(updatedCountry);
    }

    @Operation(summary = "Delete a country", description = "Deletes a country from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Country deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(
            @Parameter(description = "ID of the country to delete") @PathVariable int id) {
        countryService.deleteCountryById(id);
        return ResponseEntity.noContent().build(); // 204 No Content for delete success
    }
}