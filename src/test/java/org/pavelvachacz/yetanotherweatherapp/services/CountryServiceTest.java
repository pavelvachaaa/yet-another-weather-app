package org.pavelvachacz.yetanotherweatherapp.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pavelvachacz.yetanotherweatherapp.exceptions.CountryNotFoundException;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.repositories.CountryRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private Country country1;
    private Country country2;

    @Before
    public void setUp() {
        country1 = new Country();
        country2 = new Country();

        country1.setId(1);
        country2.setId(2);

        country1.setName("Country 1");
        country2.setName("Country 2");
    }

    @Test
    public void testGetCountries() {
        when(countryRepository.findAll()).thenReturn(Arrays.asList(country1, country2));

        List<Country> countries = countryService.getCountries();

        assertEquals(2, countries.size());
        assertTrue(countries.contains(country1));
        assertTrue(countries.contains(country2));
    }

    @Test
    public void testGetCountries_EmptyList() {
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Country> countries = countryService.getCountries();

        assertTrue(countries.isEmpty());
    }

    @Test
    public void testGetCountryByName_CountryExists() {
        when(countryRepository.findByName("Country 1")).thenReturn(Optional.of(country1));

        Country result = countryService.getCountryByName("Country 1");

        assertEquals(country1, result);
    }

    @Test(expected = CountryNotFoundException.class)
    public void testGetCountryByName_CountryNotFound() {
        when(countryRepository.findByName("Unknown Country")).thenReturn(Optional.empty());

        countryService.getCountryByName("Unknown Country");
    }

    @Test
    public void testGetCountryById_CountryExists() {
        when(countryRepository.findById(1)).thenReturn(Optional.of(country1));

        Country result = countryService.getCountryById(1);

        assertEquals(country1, result);
    }

    @Test(expected = CountryNotFoundException.class)
    public void testGetCountryById_CountryNotFound() {
        when(countryRepository.findById(99)).thenReturn(Optional.empty());

        countryService.getCountryById(99);
    }

    @Test
    public void testCreateCountry() {
        when(countryRepository.save(country1)).thenReturn(country1);

        Country result = countryService.createCountry(country1);

        assertEquals(country1, result);
        verify(countryRepository).save(country1);
    }

    @Test
    public void testUpdateCountry_CountryExists() {
        when(countryRepository.findById(1)).thenReturn(Optional.of(country1));
        when(countryRepository.save(any(Country.class))).thenReturn(country1);

        Country update = new Country();
        update.setName("Updated Country");

        Country updatedCountry = countryService.updateCountry(1, update);

        assertEquals("Updated Country", updatedCountry.getName());
        verify(countryRepository).save(country1);
    }

    @Test(expected = CountryNotFoundException.class)
    public void testUpdateCountry_CountryNotFound() {
        when(countryRepository.findById(99)).thenReturn(Optional.empty());

        Country update = new Country();
        update.setName("Nonexistent");

        countryService.updateCountry(99, update);
    }

    @Test
    public void testDeleteCountryById_CountryExists() {
        when(countryRepository.findById(1)).thenReturn(Optional.of(country1));

        countryService.deleteCountryById(1);

        verify(countryRepository).delete(country1);
    }

    @Test(expected = CountryNotFoundException.class)
    public void testDeleteCountryById_CountryNotFound() {
        when(countryRepository.findById(99)).thenReturn(Optional.empty());

        countryService.deleteCountryById(99);
    }
}
