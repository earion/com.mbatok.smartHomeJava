package com.mbatok.temp;

import java.io.IOException;

/**
 * Created by mateusz on 25.08.16.
 */
public interface Thermometer {
    Float readThemperature() throws IOException, InterruptedException;

    String readTemperatureAsString() throws IOException;

    String getDescription();

    String getDesctipiontAndTemperatureValue() throws IOException;
}
