package org.pavelvachacz.yetanotherweatherapp.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
    @Id
    private int id;
    private LocalDateTime datetime;
    private double temp;
    private int pressure;
    private int humidity;
    private double temp_min;
    private double temp_max;
    private String weather;
    private String weather_desc;
    private double wind_speed;
    private int wind_deg;
    private int city_id;
}
