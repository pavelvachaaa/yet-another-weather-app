package org.pavelvachacz.yetanotherweatherapp.services;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.pavelvachacz.yetanotherweatherapp.repositories.CityRepository;
import org.pavelvachacz.yetanotherweatherapp.repositories.MeasurementRepository;
import org.pavelvachacz.yetanotherweatherapp.vendor.owm.WeatherService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class WeatherServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private WeatherService weatherService;

    private City testCity;

    @Before
    public void setUp() {
        Country country = new Country();
        country.setId(1);
        country.setName("FakeCountry");

        testCity = new City(
                1,
                "City 1",
                12.0,
                5.0,
                country,
                Collections.emptyList()
        );    }

    @Test
    public void testUpdateMeasurements_CityNotFound() {
        when(cityRepository.findById(1)).thenReturn(Optional.empty());

        try {
            weatherService.updateMeasurements(testCity, 100L, 200L);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("City not found with ID: 1", e.getMessage());
        }
    }

    @Test
    public void testUpdateMeasurements_LatestMeasurementToday() {
        Measurement measurement = new Measurement();
        measurement.setDatetime(LocalDateTime.now());

        when(cityRepository.findById(1)).thenReturn(Optional.of(testCity));
        when(measurementRepository.findLatestMeasurement(eq(1), any()))
                .thenReturn(List.of(measurement));

        weatherService.updateMeasurements(testCity, 100L, 200L);

        verify(measurementRepository, never()).deleteByCityId(anyInt());
        verify(measurementRepository, never()).saveAll(any());
    }

    @Test
    public void testUpdateMeasurements_WithOldMeasurements() {
        Measurement oldMeasurement = new Measurement();
        oldMeasurement.setDatetime(LocalDateTime.now().minusDays(5));

        when(cityRepository.findById(1)).thenReturn(Optional.of(testCity));
        when(measurementRepository.findLatestMeasurement(eq(1), any()))
                .thenReturn(List.of(oldMeasurement));

        // Simulate a basic response
        String mockJsonResponse = """
        {
          "list": [
            {
              "dt": 1605182400,
              "main": {
                "temp": 10.5,
                "temp_min": 9.0,
                "temp_max": 12.0,
                "pressure": 1013,
                "humidity": 87
              },
              "wind": {
                "speed": 4.1,
                "deg": 80
              },
              "weather": [{
                "main": "Clouds",
                "description": "scattered clouds"
              }]
            }
          ]
        }
        """;

        WeatherService spyService = spy(weatherService);
        doReturn(mockJsonResponse).when(spyService).getHistoricalWeather(eq(testCity), anyLong(), anyLong());

        spyService.updateMeasurements(testCity, 100L, 200L);

        verify(measurementRepository).deleteByCityId(testCity.getId());
        verify(measurementRepository).saveAll(any());
    }

    @Test
    public void testParseResponse_ValidJson() {
        String json = """
        {
          "list": [
            {
              "dt": 1605182400,
              "main": {
                "temp": 10.5,
                "temp_min": 9.0,
                "temp_max": 12.0,
                "pressure": 1013,
                "humidity": 87
              },
              "wind": {
                "speed": 4.1,
                "deg": 80
              },
              "weather": [{
                "main": "Clouds",
                "description": "scattered clouds"
              }]
            }
          ]
        }
        """;

        List<Measurement> measurements = weatherService.parseResponse(json, testCity);

        assertEquals(1, measurements.size());
        assertEquals("Clouds", measurements.get(0).getWeather());
    }
}
