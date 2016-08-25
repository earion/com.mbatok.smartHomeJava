package com.mbatok.temp;

import com.pi4j.system.SystemInfo;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by mateusz on 25.08.16.
 */
public class SystemThermometer implements Thermometer{

    @Override
    public Float readThemperature() throws IOException {
        try {
            return SystemInfo.getCpuTemperature();
        } catch (InterruptedException e ) {
            return 0.0F;
        }
    }

    @Override
    public String readTemperatureAsString() throws IOException {
        Float  temp = readThemperature();
        DecimalFormat df=new DecimalFormat("0.0");
        return df.format(temp);
    }

    @Override
    public String getDescription() {
        return "System CPU";
    }

    @Override
    public String getDesctipiontAndTemperatureValue() throws IOException {
        return getDescription() + " " + readTemperatureAsString();
    }
}
