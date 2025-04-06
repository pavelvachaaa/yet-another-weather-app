package org.pavelvachacz.yetanotherweatherapp.daos;

import org.pavelvachacz.yetanotherweatherapp.mappers.MeasurementMapper;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MeasurementDAO {

    private final NamedParameterJdbcOperations jdbc;
    private final MeasurementMapper measurementMapper;

    // Queries
    public List<Measurement> getMeasurements() {
        return jdbc.query("SELECT * FROM Measurement", measurementMapper);
    }

    public List<Measurement> getMeasurementsByCityId(int city_id) {
        MapSqlParameterSource params = new MapSqlParameterSource("city_id", city_id);
        return jdbc.query("SELECT * FROM Measurement WHERE City_id = :city_id", params, measurementMapper);
    }

    public Measurement getMeasurementById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.query("SELECT * FROM Measurement WHERE id = :id", params, measurementMapper).get(0);
    }

    // Updates
    public boolean update(Measurement measurement){
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(measurement);
        return jdbc.update(
                "UPDATE Measurement SET " +
                        "datetime = :datetime, " +
                        "temp = :temp, " +
                        "pressure = :pressure, " +
                        "humidity = :humidity, " +
                        "temp_min = :temp_min, " +
                        "temp_max = :temp_max, " +
                        "weather = :weather, " +
                        "weather_desc = :weather_desc, " +
                        "wind_speed = :wind_speed, " +
                        "wind_deg = :wind_deg, " +
                        "City_id = :city_id WHERE id = :id", params) == 1;
    }

    // Inserts
    public boolean create(Measurement measurement) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(measurement);
        return jdbc.update("INSERT INTO Measurement " +
                "(datetime, temp, pressure, humidity, temp_min, temp_max, weather, weather_desc, wind_speed, wind_deg, City_id) Values " +
                "(:datetime, :temp, :pressure, :humidity, :temp_min, :temp_max, :weather, :weather_desc, :wind_speed, :wind_deg, :city_id)", params) == 1;
    }

    public int[] create(List<Measurement> measurements){
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(measurements.toArray());
        return jdbc.batchUpdate("INSERT INTO Measurement " +
                "(datetime, temp, pressure, humidity, temp_min, temp_max, weather, weather_desc, wind_speed, wind_deg, City_id) Values " +
                "(:datetime, :temp, :pressure, :humidity, :temp_min, :temp_max, :weather, :weather_desc, :wind_speed, :wind_deg, :city_id)", params);
    }

    // Deletes
    public boolean delete(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.update("DELETE FROM Measurement WHERE id = :id", params) == 1;
    }

}
