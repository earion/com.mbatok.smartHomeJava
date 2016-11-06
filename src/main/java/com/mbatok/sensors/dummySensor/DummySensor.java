package com.mbatok.sensors.dummySensor;

import com.mbatok.sensors.sensor.AbstractSensor;
import com.mbatok.sensors.sensor.Sensor;
import com.mbatok.sensors.sensor.SensorResult;

import javax.persistence.Entity;
import java.io.IOException;
import java.util.Random;

@Entity

public class DummySensor extends AbstractSensor implements Sensor {

    public DummySensor() {
        super("DUMM");
    }

    @Override
    public SensorResult read() throws IOException {
        Random rand = new Random();
        float finalX = rand.nextFloat() * (100 - 20) + 20;
        return new SensorResult(finalX,this);
    }




}
