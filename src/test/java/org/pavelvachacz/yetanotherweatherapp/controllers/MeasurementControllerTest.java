package org.pavelvachacz.yetanotherweatherapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pavelvachacz.yetanotherweatherapp.dtos.MeasurementAggregateDTO;
import org.pavelvachacz.yetanotherweatherapp.models.City;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.pavelvachacz.yetanotherweatherapp.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeasurementController.class)
class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasurementService measurementService;

    @Autowired
    private ObjectMapper objectMapper;

    private Measurement createSampleMeasurement() {
        City city = new City();
        city.setId(1);
        city.setName("Berlin");
        city.setLatitude(52.5200);
        city.setLongitude(13.4050);

        Measurement measurement = new Measurement();
        measurement.setId(1);
        measurement.setDatetime(LocalDateTime.now());
        measurement.setTemp(20.5);
        measurement.setPressure(1012);
        measurement.setHumidity(60);
        measurement.setTemp_min(18.0);
        measurement.setTemp_max(23.0);
        measurement.setWeather("Clear");
        measurement.setWeather_desc("clear sky");
        measurement.setWind_speed(3.5);
        measurement.setWind_deg(180);
        measurement.setCity(city);

        return measurement;
    }

    @Test
    void getAllMeasurements_whenMeasurementsExist_shouldReturnOk() throws Exception {
        Measurement m1 = createSampleMeasurement();
        Measurement m2 = createSampleMeasurement();
        m2.setId(2);

        when(measurementService.getMeasurements()).thenReturn(Arrays.asList(m1, m2));

        mockMvc.perform(get("/measurement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getAllMeasurements_whenNoMeasurements_shouldReturnNoContent() throws Exception {
        when(measurementService.getMeasurements()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/measurement"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getMeasurementsByCity_shouldReturnMeasurements() throws Exception {
        Measurement m = createSampleMeasurement();

        when(measurementService.getMeasurementsByCityName("Berlin")).thenReturn(Collections.singletonList(m));

        mockMvc.perform(get("/measurement/city/Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].city.name").value("Berlin"));
    }

    @Test
    void getLatestMeasurement_shouldReturnMeasurement() throws Exception {
        Measurement m = createSampleMeasurement();

        when(measurementService.getLatestMeasurement("Berlin")).thenReturn(m);

        mockMvc.perform(get("/measurement/latest/Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city.name").value("Berlin"));
    }
    @Test
    void getDailyAverage_shouldReturnAggregate() throws Exception {
        MeasurementAggregateDTO dto = new MeasurementAggregateDTO();
        dto.setAvgTemp(20.5);
        dto.setAvgPressure(1012.0);
        dto.setAvgHumidity(60.0);
        dto.setAvgTempMin(18.0);
        dto.setAvgTempMax(23.0);
        dto.setAvgWindSpeed(5.5);

        when(measurementService.getDailyAverage("Berlin")).thenReturn(dto);

        mockMvc.perform(get("/measurement/stats/daily/Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avgTemp").value(20.5))
                .andExpect(jsonPath("$.avgPressure").value(1012.0))
                .andExpect(jsonPath("$.avgHumidity").value(60.0))
                .andExpect(jsonPath("$.avgTempMin").value(18.0))
                .andExpect(jsonPath("$.avgTempMax").value(23.0))
                .andExpect(jsonPath("$.avgWindSpeed").value(5.5));
    }

    @Test
    void getWeeklyAverage_shouldReturnAggregate() throws Exception {
        MeasurementAggregateDTO dto = new MeasurementAggregateDTO();
        dto.setAvgTemp(18.7);
        dto.setAvgPressure(1015.0);
        dto.setAvgHumidity(65.0);
        dto.setAvgTempMin(16.0);
        dto.setAvgTempMax(22.0);
        dto.setAvgWindSpeed(4.0);

        when(measurementService.getWeeklyAverage("Berlin")).thenReturn(dto);

        mockMvc.perform(get("/measurement/stats/weekly/Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avgTemp").value(18.7))
                .andExpect(jsonPath("$.avgPressure").value(1015.0))
                .andExpect(jsonPath("$.avgHumidity").value(65.0))
                .andExpect(jsonPath("$.avgTempMin").value(16.0))
                .andExpect(jsonPath("$.avgTempMax").value(22.0))
                .andExpect(jsonPath("$.avgWindSpeed").value(4.0));
    }

    @Test
    void createMeasurement_shouldReturnCreated() throws Exception {
        Measurement measurement = createSampleMeasurement();
        measurement.setId(null); // ID will be generated

        doNothing().when(measurementService).save(any(Measurement.class));

        mockMvc.perform(post("/measurement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(measurement)))
                .andExpect(status().isCreated());
    }

    @Test
    void createMeasurementsBatch_shouldReturnCreated() throws Exception {
        Measurement m1 = createSampleMeasurement();
        m1.setId(null);
        Measurement m2 = createSampleMeasurement();
        m2.setId(null);

        doNothing().when(measurementService).save(any(Measurement.class));

        mockMvc.perform(post("/measurement/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(m1, m2))))
                .andExpect(status().isCreated());
    }

    @Test
    void updateMeasurement_shouldReturnOk() throws Exception {
        Measurement measurement = createSampleMeasurement();

        doNothing().when(measurementService).save(any(Measurement.class));

        mockMvc.perform(put("/measurement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(measurement)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMeasurement_shouldReturnNoContent() throws Exception {
        doNothing().when(measurementService).delete(any(Measurement.class));

        mockMvc.perform(delete("/measurement/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMeasurementsByCity_shouldReturnNoContent() throws Exception {
        doNothing().when(measurementService).deleteByCity("Berlin");

        mockMvc.perform(delete("/measurement/city/Berlin"))
                .andExpect(status().isNoContent());
    }
}
