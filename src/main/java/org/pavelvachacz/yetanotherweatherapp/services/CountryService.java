package org.pavelvachacz.yetanotherweatherapp.services;


import org.pavelvachacz.yetanotherweatherapp.daos.CityDAO;
import org.pavelvachacz.yetanotherweatherapp.daos.CountryDAO;
import org.pavelvachacz.yetanotherweatherapp.daos.MeasurementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    private final CountryDAO countryDAO;

    @Autowired
    public CountryService(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }
}
