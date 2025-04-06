package org.pavelvachacz.yetanotherweatherapp.services;

import org.pavelvachacz.yetanotherweatherapp.daos.MeasurementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasurementService {

    private final MeasurementDAO measurementDAO;

    @Autowired
    public MeasurementService(MeasurementDAO measurementDAO) {
        this.measurementDAO = measurementDAO;
    }
}
