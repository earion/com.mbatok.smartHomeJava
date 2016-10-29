package com.mbatok.sensors.humidity;

import java.io.IOException;

/**
 * Created by mateusz on 27.10.16.
 */
public interface HumiditySensor {

    Float readHumidity() throws IOException, InterruptedException;

    String readHumidityAsString() throws IOException;

    String getDescription();

    String getDescriptionAndHumidityValue() throws IOException;
}
