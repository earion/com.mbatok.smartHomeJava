package com.mbatok.sensors.humidity;

import com.mbatok.sensors.Sensor;

import java.io.IOException;

/**
 * Created by mateusz on 31.10.16.
 */
public class DHT22Humidity extends DHT22 implements Sensor{


    @Override
    public float read() throws IOException {
        return readHumidity();
    }

    @Override
    public String getDescription() {
        return null;
    }


}
