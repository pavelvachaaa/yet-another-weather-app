package org.pavelvachacz.yetanotherweatherapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    private int id;
    private String name;
}
