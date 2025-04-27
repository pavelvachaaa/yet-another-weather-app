package org.pavelvachacz.yetanotherweatherapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pavelvachacz.yetanotherweatherapp.models.Country;
import org.pavelvachacz.yetanotherweatherapp.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@ActiveProfiles("test")
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCountries_whenCountriesExist_shouldReturnOk() throws Exception {
        Country usa = new Country();
        usa.setId(1);
        usa.setName("USA");

        Country canada = new Country();
        canada.setId(2);
        canada.setName("Canada");

        Mockito.when(countryService.getCountries()).thenReturn(Arrays.asList(usa, canada));

        mockMvc.perform(get("/country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("USA"))
                .andExpect(jsonPath("$[1].name").value("Canada"));
    }

    @Test
    void getAllCountries_whenNoCountries_shouldReturnNoContent() throws Exception {
        Mockito.when(countryService.getCountries()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/country"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCountryById_shouldReturnCountry() throws Exception {
        Country germany = new Country();
        germany.setId(1);
        germany.setName("Germany");

        Mockito.when(countryService.getCountryById(1)).thenReturn(germany);

        mockMvc.perform(get("/country/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Germany"));
    }

    @Test
    void getCountryByName_shouldReturnCountry() throws Exception {
        Country france = new Country();
        france.setId(2);
        france.setName("France");

        Mockito.when(countryService.getCountryByName("France")).thenReturn(france);

        mockMvc.perform(get("/country/name/France"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("France"));
    }

    @Test
    void createCountry_shouldReturnCreated() throws Exception {
        Country inputCountry = new Country();
        inputCountry.setName("Spain");

        Country createdCountry = new Country();
        createdCountry.setId(3);
        createdCountry.setName("Spain");

        Mockito.when(countryService.createCountry(any(Country.class))).thenReturn(createdCountry);

        mockMvc.perform(post("/country")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputCountry)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Spain"));
    }

    @Test
    void updateCountry_shouldReturnUpdatedCountry() throws Exception {
        Country updateRequest = new Country();
        updateRequest.setName("Italy");

        Country updatedCountry = new Country();
        updatedCountry.setId(1);
        updatedCountry.setName("Italy");

        Mockito.when(countryService.updateCountry(eq(1), any(Country.class))).thenReturn(updatedCountry);

        mockMvc.perform(put("/country/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Italy"));
    }

    @Test
    void deleteCountry_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(countryService).deleteCountryById(1);

        mockMvc.perform(delete("/country/1"))
                .andExpect(status().isNoContent());
    }
}
