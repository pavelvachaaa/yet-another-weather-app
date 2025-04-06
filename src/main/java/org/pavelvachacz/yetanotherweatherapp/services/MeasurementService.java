package org.pavelvachacz.yetanotherweatherapp.services;

import org.pavelvachacz.yetanotherweatherapp.daos.jdbc.MeasurementDAO;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    private final MeasurementDAO measurementDAO;

    @Autowired
    public MeasurementService(MeasurementDAO measurementDAO) {
        this.measurementDAO = measurementDAO;
    }

    // Get all measurements
    public List<Measurement> getAllMeasurements() {
        return measurementDAO.getMeasurements();
    }

    // Get measurements by city id
    public List<Measurement> getMeasurementsByCityId(int cityId) {
        return measurementDAO.getMeasurementsByCityId(cityId);
    }

    // Get measurement by ID
    public Optional<Measurement> getMeasurementById(int id) {
        return Optional.ofNullable(measurementDAO.getMeasurementById(id));
    }

    // Create a new measurement
    public boolean createMeasurement(Measurement measurement) {
        return measurementDAO.create(measurement);
    }

    // Create a batch of measurements
    public int[] createMeasurements(List<Measurement> measurements) {
        return measurementDAO.create(measurements);
    }

    // Update a measurement
    public boolean updateMeasurement(Measurement measurement) {
        return measurementDAO.update(measurement);
    }

    // Delete a measurement by ID
    public boolean deleteMeasurement(int id) {
        return measurementDAO.delete(id);
    }
}
