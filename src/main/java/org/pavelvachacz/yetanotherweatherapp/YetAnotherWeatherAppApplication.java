package org.pavelvachacz.yetanotherweatherapp;

import org.pavelvachacz.yetanotherweatherapp.daos.CityDAO;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

@SpringBootApplication
public class YetAnotherWeatherAppApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(YetAnotherWeatherAppApplication.class, args);

		// Get the CityDAO bean
		CityDAO cityDAO = context.getBean(CityDAO.class);

		City newCity = new City(0, "New York", 40.7128, -74.0060, 1);
		cityDAO.create(newCity);

		Optional<City> retrievedCity = cityDAO.getCity("New York");
		System.out.println(retrievedCity);
	}

}
