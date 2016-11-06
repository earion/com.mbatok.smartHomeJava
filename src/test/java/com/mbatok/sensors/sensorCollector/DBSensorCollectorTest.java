package com.mbatok.sensors.sensorCollector;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mateusz on 05.11.16.
 */
public class DBSensorCollectorTest {


    @Test
    public void checkIfSensorsWereRead() throws IOException {
        DBSensorCollector dbs = new DBSensorCollector();
        assertThat(dbs.getSensorsNumber()).isGreaterThan(0);
    }



}