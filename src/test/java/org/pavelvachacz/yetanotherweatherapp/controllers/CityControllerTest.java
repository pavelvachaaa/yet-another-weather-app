package org.pavelvachacz.yetanotherweatherapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCities_whenCitiesExist_shouldReturnOk() throws Exception {
        City city1 = new City();
        city1.setId(1);
        city1.setName("New York");
        city1.setLatitude(40.7128);
        city1.setLongitude(-74.0060);

        City city2 = new City();
        city2.setId(2);
        city2.setName("Los Angeles");
        city2.setLatitude(34.0522);
        city2.setLongitude(-118.2437);

        when(cityService.getCities()).thenReturn(Arrays.asList(city1, city2));

        mockMvc.perform(get("/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("New York"))
                .andExpect(jsonPath("$[1].name").value("Los Angeles"));
    }

    @Test
    void getAllCities_whenNoCities_shouldReturnNoContent() throws Exception {
        when(cityService.getCities()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/city"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCityById_shouldReturnCity() throws Exception {
        City city = new City();
        city.setId(1);
        city.setName("Berlin");
        city.setLatitude(52.5200);
        city.setLongitude(13.4050);

        when(cityService.getCityById(1)).thenReturn(city);

        mockMvc.perform(get("/city/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Berlin"));
    }

    @Test
    void getCityByName_shouldReturnCity() throws Exception {
        City city = new City();
        city.setId(2);
        city.setName("Tokyo");
        city.setLatitude(35.6895);
        city.setLongitude(139.6917);

        when(cityService.getCityByName("Tokyo")).thenReturn(city);

        mockMvc.perform(get("/city/name/Tokyo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tokyo"));
    }

    @Test
    void createCity_shouldReturnCreated() throws Exception {
        City inputCity = new City();
        inputCity.setName("Paris");
        inputCity.setLatitude(48.8566);
        inputCity.setLongitude(2.3522);

        City createdCity = new City();
        createdCity.setId(3);
        createdCity.setName("Paris");
        createdCity.setLatitude(48.8566);
        createdCity.setLongitude(2.3522);

        when(cityService.createCity(any(City.class))).thenReturn(createdCity);

        mockMvc.perform(post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputCity)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Paris"));
    }

    @Test
    void updateCity_shouldReturnUpdatedCity() throws Exception {
        City updateRequest = new City();
        updateRequest.setName("Rome");
        updateRequest.setLatitude(41.9028);
        updateRequest.setLongitude(12.4964);

        City updatedCity = new City();
        updatedCity.setId(1);
        updatedCity.setName("Rome");
        updatedCity.setLatitude(41.9028);
        updatedCity.setLongitude(12.4964);

        when(cityService.updateCity(eq(1), any(City.class))).thenReturn(updatedCity);

        mockMvc.perform(put("/city/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rome"));
    }

    @Test
    void partialUpdateCity_shouldReturnPartiallyUpdatedCity() throws Exception {
        City partialUpdate = new City();
        partialUpdate.setName("Updated Name Only");

        City updatedCity = new City();
        updatedCity.setId(1);
        updatedCity.setName("Updated Name Only");
        updatedCity.setLatitude(0.0);  // Assuming default values
        updatedCity.setLongitude(0.0);

        when(cityService.updateCity(eq(1), any(City.class))).thenReturn(updatedCity);

        mockMvc.perform(patch("/city/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name Only"));
    }

    @Test
    void deleteCity_shouldReturnNoContent() throws Exception {
        doNothing().when(cityService).deleteCityById(1);

        mockMvc.perform(delete("/city/1"))
                .andExpect(status().isNoContent());
    }
}
