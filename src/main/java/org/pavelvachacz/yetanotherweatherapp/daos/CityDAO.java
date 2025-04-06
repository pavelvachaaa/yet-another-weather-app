package org.pavelvachacz.yetanotherweatherapp.daos;
import org.pavelvachacz.yetanotherweatherapp.mappers.CityMapper;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CityDAO {

    private final NamedParameterJdbcOperations jdbc;

    // Queries
    public List<City> getCities(){
        return jdbc.query("SELECT * FROM City", new CityMapper());
    }

    public List<City> getCitiesByCountryId(int country_id){
        MapSqlParameterSource params = new MapSqlParameterSource("country_id", country_id);
        return jdbc.query("SELECT * FROM City WHERE Country_id = :country_id", params, new CityMapper());
    }

    public Optional<City> getCity(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<City> cities = jdbc.query("SELECT * FROM City WHERE id = :id", params, new CityMapper());
        return cities.isEmpty() ? Optional.empty() : Optional.of(cities.get(0)); // Return Optional
    }

    public Optional<City> getCity(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        List<City> cities = jdbc.query("SELECT * FROM City WHERE name = :name", params, new CityMapper());
        return cities.isEmpty() ? Optional.empty() : Optional.of(cities.get(0)); // Return Optional
    }
    // Updates
    public boolean update(City city){
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(city);
        return jdbc.update("UPDATE City SET name = :name, latitude = :latitude, longitude = :longitude, Country_id = :country_id WHERE id = :id ", params) == 1;
    }

    // Inserts
    public boolean create(City city){
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(city);
        return jdbc.update("INSERT INTO City (name, latitude, longitude, Country_id) VALUES (:name, :latitude, :longitude, :country_id)", params) == 1;
    }

    public int[] create(List<City> cities){
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(cities.toArray());
        return jdbc.batchUpdate("INSERT INTO City (name, latitude, longitude, Country_id) VALUES (:name, :latitude, :longitude, :country_id)", params);
    }

    // Deletes
    public boolean delete(int id){
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.update("DELETE FROM City WHERE id = :id", params) == 1;
    }
}
