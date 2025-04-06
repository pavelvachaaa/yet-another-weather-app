package org.pavelvachacz.yetanotherweatherapp.services;

import org.pavelvachacz.yetanotherweatherapp.daos.CityDAO;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityDAO cityDAO;

    @Autowired
    public CityService(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }

    // Get all cities
    public List<City> getAllCities() {
        return cityDAO.getCities();
    }

    // Get city by ID
    public Optional<City> getCityById(int id) {
        return cityDAO.getCity(id);
    }

    // Get city by name
    public Optional<City>  getCityByName(String name) {
        return cityDAO.getCity(name);
    }


}
