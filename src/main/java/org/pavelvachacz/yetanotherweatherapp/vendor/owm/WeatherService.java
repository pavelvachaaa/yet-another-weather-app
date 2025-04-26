package org.pavelvachacz.yetanotherweatherapp.vendor.owm;

import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.pavelvachacz.yetanotherweatherapp.repositories.CityRepository;
import org.pavelvachacz.yetanotherweatherapp.repositories.MeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public  class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final MeasurementRepository measurementRepository;
    private final CityRepository cityRepository;
    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Value("${openweather.api.key}")
    private String apiKey;

    public WeatherService(MeasurementRepository measurementRepository,
                          CityRepository cityRepository,
                          @Value("${openweather.baseurl}") String baseUrl) {
        this.measurementRepository = measurementRepository;
        this.cityRepository = cityRepository;
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public String getHistoricalWeather(City city, Long from, Long to) {
        try {
            String url = UriComponentsBuilder.fromUriString(baseUrl + "/history/city")
                    .queryParam("lat", city.getLatitude())
                    .queryParam("lon", city.getLongitude())
                    .queryParam("start", from)
                    .queryParam("end", to)
                    .queryParam("units", "metric")
                    .queryParam("appid", apiKey)
                    .toUriString();

            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            logger.error("Failed to fetch historical weather data for city {}: {}", city.getName(), e.getMessage(), e);
            throw new RuntimeException("Failed to fetch historical weather data", e);
        }
    }


    @Transactional
    public void updateMeasurements(City city, Long from, Long to) {
        logger.debug("Updating measurements for city {}", city.getName());
        City foundCity = cityRepository.findById(city.getId())
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + city.getId()));

        Pageable pageable = PageRequest.of(0, 1);
        List<Measurement> latestMeasurements = measurementRepository.findLatestMeasurement(foundCity.getId(), pageable);

        if (!latestMeasurements.isEmpty()) {
            Measurement latestMeasurement = latestMeasurements.get(0);
            if (!latestMeasurement.getDatetime().toLocalDate().isBefore(LocalDate.now())) {
                logger.info("Latest measurement for city {} is up-to-date. Skipping update.", foundCity.getName());
                return;
            }
        }

        logger.info("Updating measurements for city {}", foundCity.getName());

        measurementRepository.deleteByCityId(foundCity.getId());

        String response = getHistoricalWeather(foundCity, from, to);
        List<Measurement> measurements = parseResponse(response, foundCity);
        measurementRepository.saveAll(measurements);
    }

    public List<Measurement> parseResponse(String response, City city) {
        List<Measurement> measurements = new ArrayList<>();

        if (response == null || response.isBlank()) {
            logger.warn("Empty response received for city {}", city.getName());
            return measurements;
        }

        try {
            JSONObject root = new JSONObject(response);
            JSONArray list = root.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject weatherItem = list.getJSONObject(i);

                long dt = weatherItem.getLong("dt");
                LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.systemDefault());

                JSONObject main = weatherItem.getJSONObject("main");
                JSONObject wind = weatherItem.getJSONObject("wind");
                JSONArray weatherArray = weatherItem.getJSONArray("weather");
                JSONObject weatherDetails = weatherArray.getJSONObject(0);

                Measurement measurement = new Measurement(
                        null,
                        dateTime,
                        main.getDouble("temp"),
                        main.getInt("pressure"),
                        main.getInt("humidity"),
                        main.getDouble("temp_min"),
                        main.getDouble("temp_max"),
                        weatherDetails.getString("main"),
                        weatherDetails.getString("description"),
                        wind.getDouble("speed"),
                        wind.getInt("deg"),
                        city
                );
                measurements.add(measurement);
            }
        } catch (Exception e) {
            logger.error("Error parsing weather response for city {}: {}", city.getName(), e.getMessage(), e);
            throw new RuntimeException("Failed to parse weather data", e);
        }

        return measurements;
    }
}