package org.pavelvachacz.yetanotherweatherapp.mappers;
import org.pavelvachacz.yetanotherweatherapp.models.City;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CityMapper implements RowMapper<City>  {

    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
        City city = new City();
        city.setId(rs.getInt("id"));
        city.setName(rs.getString("name"));
        city.setLatitude(rs.getDouble("latitude"));
        city.setLongitude(rs.getDouble("longitude"));
        city.setCountry_id(rs.getInt("country_id"));
        return city;
    }

}
