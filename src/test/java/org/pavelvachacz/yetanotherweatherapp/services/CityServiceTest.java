package org.pavelvachacz.yetanotherweatherapp.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pavelvachacz.yetanotherweatherapp.exceptions.CityNotFoundException;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.repositories.CityRepository;
import org.pavelvachacz.yetanotherweatherapp.repositories.CountryRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityService cityService;

    private List<City> mockCities;

    @Before
    public void setUp() {
        mockCities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            City city = new City();
            city.setName("City " + i);
            city.setId(i);
            mockCities.add(city);
        }
    }

    @Test
    public void testGetCities() {
        when(cityRepository.findAll()).thenReturn(mockCities);
        List<City> cities = cityService.getCities();

        assertFalse(cities.isEmpty());
        assertEquals(5, cities.size());
    }


    @Test
    public void testGetCityByName_CityExists() {
        Country country = new Country();
        country.setId(1);
        country.setName("FakeCountry");

        City city = new City(
                1,
                "City 1",
                12.0,
                5.0,
                country,
                Collections.emptyList()
        );

        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));

        City result = cityService.getCityByName("City 1");

        assertEquals(city, result);
    }

    @Test
    public void testGetCityByName_CityNotFound() {
        when(cityRepository.findByName("City 1")).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> {
            cityService.getCityByName("City 1");
        });
    }

    @Test
    public void testGetCityById_CityExists() {
        Country country = new Country();
        country.setId(1);
        country.setName("FakeCountry");

        City city = new City(
                1,
                "City 1",
                12.0,
                5.0,
                country,
                Collections.emptyList()
        );
        when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        City result = cityService.getCityById(1);

        assertEquals(city, result);
    }


    @Test
    public void testSave() {
        Country country = new Country();
        country.setId(1);
        country.setName("FakeCountry");

        City city = new City(
                1,
                "City 1",
                12.0,
                5.0,
                country,
                Collections.emptyList()
        );


        cityService.createCity(city);

        verify(cityRepository).save(city);
    }



    @Test
    public void testDeleteByName_CityExists() {
        Country country = new Country();
        country.setId(1);
        country.setName("FakeCountry");

        City city = new City(
                1,
                "City 1",
                12.0,
                5.0,
                country,
                Collections.emptyList()
        );

        cityService.createCity(city);

        when(cityRepository.findByName("City 1")).thenReturn(Optional.of(city));
        cityService.deleteCityByName("City 1");

        verify(cityRepository).delete(city);
    }

}
