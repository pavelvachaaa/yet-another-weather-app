package org.pavelvachacz.yetanotherweatherapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementAggregateDTO {
    private double avgTemp;
    private double avgPressure;
    private double avgHumidity;
    private double avgTempMin;
    private double avgTempMax;
    private double avgWindSpeed;
}