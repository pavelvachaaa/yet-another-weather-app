package org.pavelvachacz.yetanotherweatherapp.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testFindByName_CountryExists() {
        // Arrange
        Country country = new Country();
        country.setName("Germany");
        countryRepository.save(country);

        // Act
        Optional<Country> foundCountry = countryRepository.findByName("Germany");

        // Assert
        assertTrue(foundCountry.isPresent());
        assertEquals("Germany", foundCountry.get().getName());
    }

    @Test
    public void testFindByName_CountryDoesNotExist() {
        // Act
        Optional<Country> foundCountry = countryRepository.findByName("UnknownCountry");

        // Assert
        assertFalse(foundCountry.isPresent());
    }
}
