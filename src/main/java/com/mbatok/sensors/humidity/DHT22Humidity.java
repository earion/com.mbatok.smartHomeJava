package com.mbatok.sensors.humidity;

import com.mbatok.sensors.Sensor;
import com.mbatok.sensors.SensorResult;

import javax.persistence.Entity;
import java.io.IOException;

/**
 * Created by mateusz on 31.10.16.
 */
@Entity
public class DHT22Humidity extends DHT22 implements Sensor{

    public DHT22Humidity() {
        super("%");
    }


    @Override
    public SensorResult read() throws IOException {
        return  new SensorResult(readHumidity(),this );
    }
}
