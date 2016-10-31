package com.mbatok.sensors.temperature;

import com.mbatok.sensors.Sensor;
import com.pi4j.system.SystemInfo;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by mateusz on 25.08.16.
 */
public class SystemThermometer implements Sensor {


    public Float readTemperature() throws IOException {
        try {
            return SystemInfo.getCpuTemperature();
        } catch (InterruptedException e ) {
            return 0.0F;
        }
    }

    public String readTemperatureAsString() throws IOException {
        Float  temp = readTemperature();
        DecimalFormat df=new DecimalFormat("0.0");
        return df.format(temp);
    }

    @Override
    public float read() throws IOException {
        return readTemperature();
    }

    @Override
    public String getDescription() {
        return "System CPU";
    }

    @Override
    public String getSensorType() {
        return "System CPU Temp";
    }


    public String getDescriptionAndTemperatureValue() throws IOException {
        return getDescription() + " " + readTemperatureAsString();
    }
}
