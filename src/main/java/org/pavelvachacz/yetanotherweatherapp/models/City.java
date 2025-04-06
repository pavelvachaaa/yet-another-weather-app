package org.pavelvachacz.yetanotherweatherapp.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    private int id;

    private String name;
    private double longitude;
    private double latitude;
    private int country_id;
}