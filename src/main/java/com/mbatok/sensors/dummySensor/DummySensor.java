package com.mbatok.sensors.dummySensor;

import com.mbatok.sensors.AbstractSensor;
import com.mbatok.sensors.Sensor;
import com.mbatok.sensors.SensorResult;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.IOException;

@Entity

public class DummySensor extends AbstractSensor implements Sensor {

    public DummySensor() {
        super(DummySensor.class.getSimpleName(),"DUMM");
    }

    @Override
    public SensorResult read() throws IOException {
        return null;
    }




}
