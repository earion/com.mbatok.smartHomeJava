package com.mbatok.sensors;

import java.io.IOException;

public interface Sensor {

    SensorResult read() throws IOException;

    String getDescription();

    String getType();

    String getName();

    String getUnit();

}
