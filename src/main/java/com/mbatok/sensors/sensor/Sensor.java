package com.mbatok.sensors.sensor;

import java.io.IOException;

public interface Sensor {

    SensorResult read() throws IOException;

    String getDescription();

    String getName();

    String getUnit();

    Integer getId();

}
