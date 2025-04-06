package org.pavelvachacz.yetanotherweatherapp.daos;

import org.pavelvachacz.yetanotherweatherapp.mappers.MeasurementMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MeasurementDAO {

    private final NamedParameterJdbcOperations jdbc;
    private final MeasurementMapper measurementMapper;


}
