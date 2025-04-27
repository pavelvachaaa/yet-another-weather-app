package org.pavelvachacz.yetanotherweatherapp.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pavelvachacz.yetanotherweatherapp.dtos.MeasurementAggregateDTO;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class MeasurementRepositoryTest {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    private City city;

    @BeforeEach
    public void setUp() {
        Country country = new Country();
        country.setName("TestCountry");
        country = countryRepository.save(country);

        city = new City();
        city.setName("TestCity");
        city.setLatitude(50.0);
        city.setLongitude(8.0);
        city.setCountry(country);
        city = cityRepository.save(city);
    }

    @Test
    public void testFindByCityId() {
        Measurement m = createMeasurement(20.0);
        measurementRepository.save(m);

        List<Measurement> measurements = measurementRepository.findByCityId(city.getId());

        assertEquals(1, measurements.size());
        assertEquals(20.0, measurements.get(0).getTemp());
    }

    @Test
    public void testFindLatestMeasurement() {
        Measurement older = createMeasurement(18.0);
        older.setDatetime(LocalDateTime.now().minusDays(1));
        measurementRepository.save(older);

        Measurement latest = createMeasurement(22.0);
        latest.setDatetime(LocalDateTime.now());
        measurementRepository.save(latest);

        List<Measurement> latestMeasurements = measurementRepository.findLatestMeasurement(city.getId(), PageRequest.of(0, 1));

        assertEquals(1, latestMeasurements.size());
        assertEquals(22.0, latestMeasurements.get(0).getTemp());
    }

    @Test
    public void testFindDailyAverage() {
        Measurement m1 = createMeasurement(18.0);
        Measurement m2 = createMeasurement(22.0);
        m2.setDatetime(LocalDateTime.now().minusHours(2));

        measurementRepository.save(m1);
        measurementRepository.save(m2);

        List<MeasurementAggregateDTO> dailyAvg = measurementRepository.findDailyAverage(city.getId(), PageRequest.of(0, 1));

        assertEquals(1, dailyAvg.size());
        double avgTemp = (18.0 + 22.0) / 2;
        assertEquals(avgTemp, dailyAvg.get(0).getAvgTemp(), 4);
    }

    @Test
    public void testFindWeeklyAverage() {
        Measurement m1 = createMeasurement(18.0);
        Measurement m2 = createMeasurement(22.0);
        m2.setDatetime(LocalDateTime.now().minusDays(2));

        measurementRepository.save(m1);
        measurementRepository.save(m2);

        List<MeasurementAggregateDTO> weeklyAvg = measurementRepository.findWeeklyAverage(city.getId(), PageRequest.of(0, 1));

        assertEquals(1, weeklyAvg.size());
        double avgTemp = (18.0 + 22.0) / 2;
        assertEquals(avgTemp, weeklyAvg.get(0).getAvgTemp(), 0.01);
    }

    @Test
    public void testDeleteByCityId() {
        Measurement m = createMeasurement(20.0);
        measurementRepository.save(m);

        measurementRepository.deleteByCityId(city.getId());

        List<Measurement> measurements = measurementRepository.findByCityId(city.getId());
        assertTrue(measurements.isEmpty());
    }

    private Measurement createMeasurement(double temp) {
        Measurement m = new Measurement();
        m.setCity(city);
        m.setDatetime(LocalDateTime.now());
        m.setTemp(temp);
        m.setPressure(1000);
        m.setHumidity(50);
        m.setTemp_min(temp - 2);
        m.setTemp_max(temp + 2);
        m.setWeather("Clear");
        m.setWeather_desc("Clear sky");
        m.setWind_speed(5.0);
        m.setWind_deg(180);
        return m;
    }
}
