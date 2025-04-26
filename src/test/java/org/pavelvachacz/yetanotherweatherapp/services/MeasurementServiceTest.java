package org.pavelvachacz.yetanotherweatherapp.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pavelvachacz.yetanotherweatherapp.dtos.MeasurementAggregateDTO;
import org.pavelvachacz.yetanotherweatherapp.exceptions.CityNotFoundException;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.pavelvachacz.yetanotherweatherapp.repositories.CityRepository;
import org.pavelvachacz.yetanotherweatherapp.repositories.MeasurementRepository;
import org.pavelvachacz.yetanotherweatherapp.vendor.owm.WeatherService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class MeasurementServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private MeasurementService measurementService;

    private City city;
    private Measurement measurement;

    @Before
    public void setUp() {
        city = new City();
        city.setId(1);
        city.setName("City 1");

        measurement = new Measurement();
        measurement.setId(1);
        measurement.setTemp(20.0);
        measurement.setCity(city);
    }

    @Test
    public void testGetMeasurements() {
        when(measurementRepository.findAll()).thenReturn(List.of(measurement));

        List<Measurement> measurements = measurementService.getMeasurements();

        assertEquals(1, measurements.size());
        verify(measurementRepository).findAll();
    }

    @Test
    public void testGetMeasurementsByCityName() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        when(measurementRepository.findByCityId(1)).thenReturn(List.of(measurement));

        List<Measurement> result = measurementService.getMeasurementsByCityName("City 1");

        assertEquals(1, result.size());
        verify(measurementRepository).findByCityId(1);
    }

    @Test(expected = CityNotFoundException.class)
    public void testGetMeasurementsByCityName_CityNotFound() {
        when(cityRepository.findByName("Unknown City")).thenReturn(Optional.empty());

        measurementService.getMeasurementsByCityName("Unknown City");
    }

    @Test
    public void testGetLatestMeasurement() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        when(measurementRepository.findLatestMeasurement(eq(1), any(Pageable.class)))
                .thenReturn(List.of(measurement));

        Measurement latest = measurementService.getLatestMeasurement("City 1");

        assertEquals(measurement, latest);
    }

    @Test(expected = RuntimeException.class)
    public void testGetLatestMeasurement_NoMeasurements() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        when(measurementRepository.findLatestMeasurement(eq(1), any(Pageable.class)))
                .thenReturn(Collections.emptyList());

        measurementService.getLatestMeasurement("City 1");
    }

    @Test
    public void testGetWeeklyAverage() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        MeasurementAggregateDTO dto = new MeasurementAggregateDTO(20, 1013, 50, 18, 23, 5);
        when(measurementRepository.findWeeklyAverage(eq(1), any(Pageable.class)))
                .thenReturn(List.of(dto));

        MeasurementAggregateDTO result = measurementService.getWeeklyAverage("City 1");

        assertEquals(dto, result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetWeeklyAverage_NoData() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        when(measurementRepository.findWeeklyAverage(eq(1), any(Pageable.class)))
                .thenReturn(Collections.emptyList());

        measurementService.getWeeklyAverage("City 1");
    }

    @Test
    public void testGetDailyAverage() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        MeasurementAggregateDTO dto = new MeasurementAggregateDTO(22, 1015, 55, 20, 24, 4);
        when(measurementRepository.findDailyAverage(eq(1), any(Pageable.class)))
                .thenReturn(List.of(dto));

        MeasurementAggregateDTO result = measurementService.getDailyAverage("City 1");

        assertEquals(dto, result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDailyAverage_NoData() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        when(measurementRepository.findDailyAverage(eq(1), any(Pageable.class)))
                .thenReturn(Collections.emptyList());

        measurementService.getDailyAverage("City 1");
    }

    @Test
    public void testSaveMeasurement() {
        measurementService.save(measurement);

        verify(measurementRepository).save(measurement);
    }

    @Test
    public void testDeleteMeasurement() {
        measurementService.delete(measurement);

        verify(measurementRepository).delete(measurement);
    }

    @Test
    public void testDeleteByCity() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));

        measurementService.deleteByCity("City 1");

        verify(measurementRepository).deleteByCityId(1);
    }

    @Test(expected = CityNotFoundException.class)
    public void testDeleteByCity_CityNotFound() {
        when(cityRepository.findByName("Unknown City")).thenReturn(Optional.empty());

        measurementService.deleteByCity("Unknown City");
    }
}
