package org.pavelvachacz.yetanotherweatherapp.services;

import org.pavelvachacz.yetanotherweatherapp.daos.jdbc.CityDAO;
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

    // Create a new city
    public boolean createCity(City city) {
        return cityDAO.create(city);
    }

    // Create multiple cities in batch
    public int[] createCities(List<City> cities) {
        return cityDAO.create(cities);
    }

    // Update an existing city
    public boolean updateCity(City city) {
        return cityDAO.update(city);
    }

    public boolean partialUpdateCity(int id, City cityUpdate) {
        // First check if the city exists
        Optional<City> existingCity = cityDAO.getCity(id);
        if (existingCity.isEmpty()) {
            return false;
        }

        // Perform the partial update
        return cityDAO.partialUpdate(id, cityUpdate);
    }
    // Delete a city by ID
    public boolean deleteCity(int id) {
        return cityDAO.delete(id);
    }
}
