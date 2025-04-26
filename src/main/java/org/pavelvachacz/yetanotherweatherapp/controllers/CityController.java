package org.pavelvachacz.yetanotherweatherapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.services.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
@Tag(name = "City Management", description = "APIs for managing city information")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Operation(summary = "Get all cities", description = "Returns a list of all cities in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cities found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = City.class)))),
            @ApiResponse(responseCode = "204", description = "No cities found")
    })
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getCities();
        return cities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cities);
    }

    @Operation(summary = "Get city by ID", description = "Returns a city based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City found",
                    content = @Content(schema = @Schema(implementation = City.class))),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(
            @Parameter(description = "ID of the city to retrieve") @PathVariable int id) {
        City city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    @Operation(summary = "Get city by name", description = "Returns a city based on its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City found",
                    content = @Content(schema = @Schema(implementation = City.class))),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<City> getCityByName(
            @Parameter(description = "Name of the city to retrieve") @PathVariable String name) {
        City city = cityService.getCityByName(name);
        return ResponseEntity.ok(city);
    }

    @Operation(summary = "Create a new city", description = "Creates a new city entry in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "City created successfully",
                    content = @Content(schema = @Schema(implementation = City.class))),
            @ApiResponse(responseCode = "400", description = "Invalid city data provided")
    })
    @PostMapping
    public ResponseEntity<City> createCity(
            @Parameter(description = "City object to be created", required = true,
                    schema = @Schema(implementation = City.class,
                            description = "City with name, longitude, latitude and country"))
            @RequestBody City city) {
        City createdCity = cityService.createCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCity);
    }

    @Operation(summary = "Update a city", description = "Updates all fields of an existing city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City updated successfully",
                    content = @Content(schema = @Schema(implementation = City.class))),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "400", description = "Invalid city data provided")
    })
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(
            @Parameter(description = "ID of the city to update") @PathVariable int id,
            @Parameter(description = "Updated city information", required = true,
                    schema = @Schema(implementation = City.class,
                            description = "City with updated name, longitude, latitude and/or country"))
            @RequestBody City cityUpdate) {
        City updatedCity = cityService.updateCity(id, cityUpdate);
        return ResponseEntity.ok(updatedCity);
    }

    @Operation(summary = "Partially update a city", description = "Updates specific fields of an existing city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City partially updated successfully",
                    content = @Content(schema = @Schema(implementation = City.class))),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "400", description = "Invalid city data provided")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<City> partialUpdateCity(
            @Parameter(description = "ID of the city to partially update") @PathVariable int id,
            @Parameter(description = "Fields to update in the city", required = true,
                    schema = @Schema(implementation = City.class,
                            description = "City with fields that need to be updated. Null fields will be ignored."))
            @RequestBody City cityUpdate) {
        City updatedCity = cityService.updateCity(id, cityUpdate);
        return ResponseEntity.ok(updatedCity);
    }

    @Operation(summary = "Delete a city", description = "Deletes a city from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "City deleted successfully"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(
            @Parameter(description = "ID of the city to delete") @PathVariable int id) {
        cityService.deleteCityById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}