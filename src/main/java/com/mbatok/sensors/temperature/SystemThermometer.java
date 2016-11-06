package com.mbatok.sensors.temperature;

import com.mbatok.sensors.sensor.AbstractSensor;
import com.mbatok.sensors.sensor.SensorResult;
import com.pi4j.system.SystemInfo;

import javax.persistence.Entity;
import java.io.IOException;

/**
 * Created by mateusz on 25.08.16.
 */
@Entity
public class SystemThermometer extends AbstractSensor {


    public SystemThermometer() {
        super(DS18S20.generateDeegreSymbolForLCDDisplay());
    }

    public Float readTemperature() throws IOException {
        try {
            return SystemInfo.getCpuTemperature();
        } catch (InterruptedException e ) {
            return 0.0F;
        }
    }

    @Override
    public SensorResult read() throws IOException {
        return new SensorResult(readTemperature(),this );
    }




}
