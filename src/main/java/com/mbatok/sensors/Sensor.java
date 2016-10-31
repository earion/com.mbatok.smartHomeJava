package com.mbatok.sensors;

import java.io.IOException;

/**
 * Created by mateusz on 31.10.16.
 */
public interface Sensor {

    float read() throws IOException;

    String getDescription();

    String getSensorType();

}
