package org.pavelvachacz.yetanotherweatherapp.services;

import org.pavelvachacz.yetanotherweatherapp.exceptions.CountryNotFoundException;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getCountries() {
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Country getCountryByName(String name) {
        return countryRepository.findByName(name)
                .orElseThrow(() -> new CountryNotFoundException("Country not found: " + name));
    }

    public Country getCountryById(int id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found with ID: " + id));
    }

    public Country createCountry(Country country) {
        return countryRepository.save(country);
    }

    public Country updateCountry(int id, Country countryUpdate) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found with ID: " + id));

        if (countryUpdate.getName() != null) {
            country.setName(countryUpdate.getName());
        }

        return countryRepository.save(country);
    }

    public void deleteCountryById(int id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found with ID: " + id));
        countryRepository.delete(country);
    }
}
