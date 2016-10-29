package com.mbatok.sensors.temperature;

import java.io.IOException;

/**
 * Created by mateusz on 25.08.16.
 */
public interface ThermometerSensor {
    Float readTemperature() throws IOException, InterruptedException;

    String readTemperatureAsString() throws IOException;

    String getDescription();

    String getDescriptionAndTemperatureValue() throws IOException;
}
