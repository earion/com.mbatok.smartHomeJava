package com.mbatok.sensors.humidity;

import com.mbatok.sensors.Sensor;
import com.mbatok.sensors.SensorResult;
import com.mbatok.sensors.temperature.DS18S20;

import javax.persistence.Entity;
import java.io.IOException;

/**
 * Created by mateusz on 31.10.16.
 */
@Entity
public class DHT22Temperature extends DHT22 implements Sensor{

    public DHT22Temperature() {
        super(DS18S20.generateDeegreSymbolForLCDDisplay());
    }

    @Override
    public SensorResult read() throws IOException{
       return new SensorResult(readTemperature(),this);
    }
}
