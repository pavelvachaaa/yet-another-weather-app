package org.pavelvachacz.yetanotherweatherapp.services;

import jakarta.transaction.Transactional;
import org.pavelvachacz.yetanotherweatherapp.exceptions.CityNotFoundException;
import org.pavelvachacz.yetanotherweatherapp.exceptions.CountryNotFoundException;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.repositories.CityRepository;
import org.pavelvachacz.yetanotherweatherapp.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public CityService(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    public List<City> getCities() {
        return StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public City getCityByName(String name) {
        return cityRepository.findByName(name)
                .orElseThrow(() -> new CityNotFoundException("City not found: " + name));
    }

    public City getCityById(int id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("City not found with ID: " + id));
    }

    public City createCity(City city) {
        return cityRepository.save(city);
    }

    @Transactional
    public City updateCity(int id, City cityUpdate) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("City not found with ID: " + id));

        if (cityUpdate.getName() != null) {
            city.setName(cityUpdate.getName());
        }
        if (cityUpdate.getLatitude() != null) {
            city.setLatitude(cityUpdate.getLatitude());
        }
        if (cityUpdate.getLongitude() != null) {
            city.setLongitude(cityUpdate.getLongitude());
        }
        if (cityUpdate.getCountry() != null && cityUpdate.getCountry().getId() != null) {
            Country country = countryRepository.findById(cityUpdate.getCountry().getId())
                    .orElseThrow(() -> new CountryNotFoundException("Country not found with ID: " + cityUpdate.getCountry().getId()));
            city.setCountry(country);
        }

        return cityRepository.save(city);
    }

    public void deleteCityById(int id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("City not found with ID: " + id));
        cityRepository.delete(city);
    }

    public void deleteCityByName(String name) {
        City city = cityRepository.findByName(name)
                .orElseThrow(() -> new CityNotFoundException("City not found: " + name));
        cityRepository.delete(city);
    }
}
