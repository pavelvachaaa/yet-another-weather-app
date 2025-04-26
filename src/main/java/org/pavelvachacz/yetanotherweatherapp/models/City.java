package org.pavelvachacz.yetanotherweatherapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@ToString(exclude = {"country", "measurements"})
@EqualsAndHashCode(exclude = "country")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="City")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique=true)
    private String name;

    private Double longitude;
    private Double latitude;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "city")
    @JsonIgnore
    private List<Measurement> measurements;

}