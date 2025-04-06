package org.pavelvachacz.yetanotherweatherapp.mappers;

import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MeasurementMapper implements RowMapper<Measurement> {

    @Override
    public Measurement mapRow(ResultSet rs, int rowNum) throws SQLException {
        Measurement m = new Measurement();
        m.setId(rs.getInt("id"));
        m.setCity_id(rs.getInt("City_id"));
        m.setDatetime(rs.getTimestamp("datetime").toLocalDateTime());
        m.setTemp(rs.getDouble("temp"));
        m.setPressure(rs.getInt("pressure"));
        m.setHumidity(rs.getInt("humidity"));
        m.setTemp_min(rs.getDouble("temp_min"));
        m.setTemp_max(rs.getDouble("temp_max"));
        m.setWeather(rs.getString("weather"));
        m.setWeather_desc(rs.getString("weather_desc"));
        m.setWind_speed(rs.getDouble("wind_speed"));
        m.setWind_deg(rs.getInt("wind_deg"));
        return m;
    }
}
