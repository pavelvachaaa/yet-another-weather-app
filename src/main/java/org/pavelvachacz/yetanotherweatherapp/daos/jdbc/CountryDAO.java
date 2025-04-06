package org.pavelvachacz.yetanotherweatherapp.daos.jdbc;

import org.pavelvachacz.yetanotherweatherapp.mappers.CountryMapper;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CountryDAO {

    private final NamedParameterJdbcOperations jdbc;
    private final CountryMapper countryMapper;

    // Queries
    public List<Country> getCountries() {
        return jdbc.query("SELECT * FROM Country", countryMapper);
    }

    public Optional<Country> getCountry(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            Country country = jdbc.queryForObject(
                    "SELECT * FROM Country WHERE id = :id",
                    params,
                    countryMapper
            );
            return Optional.ofNullable(country);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Country> getCountry(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        try {
            Country country = jdbc.queryForObject(
                    "SELECT * FROM Country WHERE name = :name",
                    params,
                    countryMapper
            );
            return Optional.ofNullable(country);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Updates
    public boolean update(Country country) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(country);
        return jdbc.update("UPDATE Country SET name = :name WHERE id = :id", params) == 1;
    }

    // Inserts
    public boolean create(Country country) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(country);
        return jdbc.update("INSERT INTO Country (name) VALUES (:name)", params) == 1;
    }

    public int[] create(List<Country> countries) {
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(countries.toArray());
        return jdbc.batchUpdate("INSERT INTO Country (name) VALUES (:name)", params);
    }

    // Deletes
    public boolean delete(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.update("DELETE FROM Country WHERE id = :id", params) == 1;
    }
}
