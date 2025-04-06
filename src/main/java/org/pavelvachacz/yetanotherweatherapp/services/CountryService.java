package org.pavelvachacz.yetanotherweatherapp.services;


import org.pavelvachacz.yetanotherweatherapp.daos.CityDAO;
import org.pavelvachacz.yetanotherweatherapp.daos.CountryDAO;
import org.pavelvachacz.yetanotherweatherapp.daos.MeasurementDAO;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryDAO countryDAO;

    @Autowired
    public CountryService(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    public List<Country> getAllCountries() {
        return countryDAO.getCountries();
    }

    public Optional<Country> getCountryById(int id) {
       return countryDAO.getCountry(id);
    }

    public Optional<Country> getCountryByName(String name) {
        return countryDAO.getCountry(name);
    }

    public boolean createCountry(Country country) {
        return countryDAO.create(country);
    }

    public int[] createCountries(List<Country> countries) {
        return countryDAO.create(countries);
    }

    public boolean updateCountry(Country country) {
        return countryDAO.update(country);
    }

    public boolean deleteCountry(int id) {
        return countryDAO.delete(id);
    }
}
