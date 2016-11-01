package com.mbatok.sensors.humidity;

import com.mbatok.sensors.Sensor;
import com.mbatok.sensors.SensorResult;

import java.io.IOException;

/**
 * Created by mateusz on 31.10.16.
 */
public class DHT22Humidity extends DHT22 implements Sensor{

    public DHT22Humidity() {
        super(DHT22Humidity.class.getName(),"%");
    }


    @Override
    public SensorResult read() throws IOException {
        return  new SensorResult(readHumidity(),getUnit(),getDescription());
    }
}
