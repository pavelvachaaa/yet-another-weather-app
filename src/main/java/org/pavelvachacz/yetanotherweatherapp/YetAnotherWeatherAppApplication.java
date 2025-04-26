package org.pavelvachacz.yetanotherweatherapp;

import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.services.CityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class YetAnotherWeatherAppApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(YetAnotherWeatherAppApplication.class, args);

		List<City> cities = context.getBean(CityService.class).getCities();
		System.out.println("All countries:");
		cities.forEach(System.out::println);




	}

}
