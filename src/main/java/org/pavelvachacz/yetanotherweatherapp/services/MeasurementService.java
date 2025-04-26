package org.pavelvachacz.yetanotherweatherapp.services;

import org.pavelvachacz.yetanotherweatherapp.dtos.MeasurementAggregateDTO;
import org.pavelvachacz.yetanotherweatherapp.exceptions.CityNotFoundException;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.pavelvachacz.yetanotherweatherapp.repositories.CityRepository;
import org.pavelvachacz.yetanotherweatherapp.repositories.MeasurementRepository;
import org.pavelvachacz.yetanotherweatherapp.vendor.owm.WeatherService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final CityRepository cityRepository;
    private final WeatherService weatherService;

    public MeasurementService(MeasurementRepository measurementRepository,
                              CityRepository cityRepository,
                              WeatherService weatherService) {
        this.measurementRepository = measurementRepository;
        this.cityRepository = cityRepository;
        this.weatherService = weatherService;
    }

    public List<Measurement> getMeasurements() {
        return StreamSupport.stream(measurementRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Measurement> getMeasurementsByCityName(String cityName) {
        City city = findCityByNameOrThrow(cityName);
        return measurementRepository.findByCityId(city.getId());
    }

    public Measurement getLatestMeasurement(String cityName) {
        City city = updateMeasurements(cityName);
        Pageable pageable = PageRequest.of(0, 1);
        List<Measurement> latestMeasurements = measurementRepository.findLatestMeasurement(city.getId(), pageable);
        if (latestMeasurements.isEmpty()) {
            throw new RuntimeException("No measurements found for city: " + cityName);
        }
        return latestMeasurements.get(0);
    }

    public MeasurementAggregateDTO getWeeklyAverage(String cityName) {
        City city = updateMeasurements(cityName);
        Pageable pageable = PageRequest.of(0, 1);
        List<MeasurementAggregateDTO> aggregates = measurementRepository.findWeeklyAverage(city.getId(), pageable);
        if (aggregates.isEmpty()) {
            throw new RuntimeException("No weekly average found for city: " + cityName);
        }
        return aggregates.get(0);
    }

    public MeasurementAggregateDTO getDailyAverage(String cityName) {
        City city = updateMeasurements(cityName);
        Pageable pageable = PageRequest.of(0, 1);
        List<MeasurementAggregateDTO> aggregates = measurementRepository.findDailyAverage(city.getId(), pageable);
        if (aggregates.isEmpty()) {
            throw new RuntimeException("No daily average found for city: " + cityName);
        }
        return aggregates.get(0);
    }

    private City updateMeasurements(String cityName) {
        City city = findCityByNameOrThrow(cityName);

        Long end = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Long start = LocalDateTime.now().minusDays(7).toEpochSecond(ZoneOffset.UTC);

        weatherService.updateMeasurements(city, start, end);
        return city;
    }

    public void save(Measurement measurement) {
        measurementRepository.save(measurement);
    }

    public void delete(Measurement measurement) {
        measurementRepository.delete(measurement);
    }

    public void deleteByCity(String cityName) {
        City city = findCityByNameOrThrow(cityName);
        measurementRepository.deleteByCityId(city.getId());
    }

    private City findCityByNameOrThrow(String cityName) {
        return cityRepository.findByName(cityName)
                .orElseThrow(() -> new CityNotFoundException("City not found: " + cityName));
    }
}
