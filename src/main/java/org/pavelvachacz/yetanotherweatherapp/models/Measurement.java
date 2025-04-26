package org.pavelvachacz.yetanotherweatherapp.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "city")
@Entity
@Table(name = "Measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "City_id")
    private City city;

}
