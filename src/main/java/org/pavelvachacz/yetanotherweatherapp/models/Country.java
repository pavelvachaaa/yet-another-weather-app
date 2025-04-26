package org.pavelvachacz.yetanotherweatherapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString(exclude = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<City> cities;

}
