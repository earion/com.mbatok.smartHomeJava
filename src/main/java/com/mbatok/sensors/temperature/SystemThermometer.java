package com.mbatok.sensors.temperature;

import com.mbatok.sensors.AbstractSensor;
import com.mbatok.sensors.SensorResult;
import com.pi4j.system.SystemInfo;

import java.io.IOException;

/**
 * Created by mateusz on 25.08.16.
 */
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
