package com.mbatok.temp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mateusz on 25.08.16.
 */
public class ThermometerCollector {

    private ArrayList<Thermometer> sensorsList;

    public ThermometerCollector(String sensorsFileName) throws IOException {
        File sensorsFile = new File(sensorsFileName);
        if (!sensorsFile.exists()) throw new IOException("Thermometers files does not exist " + sensorsFileName);
        sensorsList = new ArrayList<>();
        List<String> tempFileContent = Files.readAllLines(new File(sensorsFileName).toPath());
        for (String s : tempFileContent) {
            String[] device = s.split(",");
            String description = device[0];
            String systemDeviceName = device[1];
            sensorsList.add(new DallasThermometer(systemDeviceName, description));
        }
        sensorsList.add(new SystemThermometer());
    }

    public ArrayList<Thermometer> getSensorsList() {
        return sensorsList;
    }
}


