package org.pavelvachacz.yetanotherweatherapp.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testFindByName_CityExists() {
        Country country = new Country();
        country.setName("Germany");
        country = countryRepository.save(country);

        City city = new City();
        city.setName("Berlin");
        city.setLatitude(52.52);
        city.setLongitude(13.405);
        city.setCountry(country);

        cityRepository.save(city);

        Optional<City> foundCity = cityRepository.findByName("Berlin");

        assertTrue(foundCity.isPresent());
        assertEquals("Berlin", foundCity.get().getName());
    }

    @Test
    public void testFindByName_CityNotExists() {
        Optional<City> foundCity = cityRepository.findByName("NonExistent");

        assertFalse(foundCity.isPresent());
    }

    @Test
    public void testFindByCountryId_CitiesExist() {
        Country country = new Country();
        country.setName("France");
        country = countryRepository.save(country);

        City paris = new City();
        paris.setName("Paris");
        paris.setLatitude(48.8566);
        paris.setLongitude(2.3522);
        paris.setCountry(country);

        City lyon = new City();
        lyon.setName("Lyon");
        lyon.setLatitude(45.75);
        lyon.setLongitude(4.85);
        lyon.setCountry(country);

        cityRepository.save(paris);
        cityRepository.save(lyon);

        List<City> cities = cityRepository.findByCountryId(country.getId());

        assertEquals(2, cities.size());
    }

    @Test
    public void testFindByCountryId_NoCities() {
        List<City> cities = cityRepository.findByCountryId(999);

        assertTrue(cities.isEmpty());
    }

    @Test
    public void testUpdateCity() {
        Country country = new Country();
        country.setName("Spain");
        country = countryRepository.save(country);

        City city = new City();
        city.setName("Barcelona");
        city.setLatitude(41.3851);
        city.setLongitude(2.1734);
        city.setCountry(country);
        city = cityRepository.save(city);

        // Act - update
        cityRepository.updateCity(
                city.getId(),
                "Madrid",
                40.4168,
                -3.7038,
                country.getId()
        );

        Optional<City> updatedCity = cityRepository.findById(city.getId());

        assertTrue(updatedCity.isPresent());
        assertEquals(41.3851, updatedCity.get().getLatitude());
        assertEquals(2.1734, updatedCity.get().getLongitude());
    }
}
