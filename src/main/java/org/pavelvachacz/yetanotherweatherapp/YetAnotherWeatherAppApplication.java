package org.pavelvachacz.yetanotherweatherapp;

import org.pavelvachacz.yetanotherweatherapp.daos.CityDAO;
import org.pavelvachacz.yetanotherweatherapp.daos.CountryDAO;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class YetAnotherWeatherAppApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(YetAnotherWeatherAppApplication.class, args);




		CountryDAO countryDAO = context.getBean(CountryDAO.class);
		// CREATE
		Country germany = new Country(0, "Germany");
		boolean created = countryDAO.create(germany);
		System.out.println("Country created: " + created);

		// READ by name
		Optional<Country> retrieved = countryDAO.getCountry("Germany");
		retrieved.ifPresentOrElse(
				country -> System.out.println("Retrieved by name: " + country),
				() -> System.out.println("Country not found.")
		);

		// READ by ID (assuming ID was auto-incremented to 1 or known beforehand)
		if (retrieved.isPresent()) {
			int id = retrieved.get().getId();
			countryDAO.getCountry(id).ifPresent(country -> System.out.println("Retrieved by ID: " + country));
		}

		// UPDATE
		retrieved.ifPresent(country -> {
			country.setName("Germany (Updated)");
			boolean updated = countryDAO.update(country);
			System.out.println("Updated: " + updated);
		});

		// LIST all
		List<Country> allCountries = countryDAO.getCountries();
		System.out.println("All countries:");
		allCountries.forEach(System.out::println);

		// Get the CityDAO bean
		CityDAO cityDAO = context.getBean(CityDAO.class);

		City newCity = new City(0, "New York", 40.7128, -74.0060, 1);
		cityDAO.create(newCity);

		Optional<City> retrievedCity = cityDAO.getCity("New York");
		System.out.println(retrievedCity);





	}

}
