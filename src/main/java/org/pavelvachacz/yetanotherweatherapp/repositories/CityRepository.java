package org.pavelvachacz.yetanotherweatherapp.repositories;

import jakarta.transaction.Transactional;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {

    @Query("SELECT c FROM City c WHERE c.name = :name")
    public Optional<City> findByName(@Param("name")String name);

    @Query("SELECT c FROM City c WHERE c.country.id = :Country_id")
    public List<City> findByCountryId(@Param("Country_id")int country_id);

    @Modifying
    @Transactional
    @Query("UPDATE City c SET c.name = :name, c.latitude = :latitude, c.longitude = :longitude, c.country.id = :countryId WHERE c.id = :id")
    void updateCity(
            @Param("id") int id,
            @Param("name") String name,
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("countryId") int countryId
    );

}