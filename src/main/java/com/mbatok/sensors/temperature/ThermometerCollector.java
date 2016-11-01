package com.mbatok.sensors.temperature;

import com.mbatok.sensors.Sensor;
import com.mbatok.sensors.SensorBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mateusz on 25.08.16.
 */
public class ThermometerCollector {

    private ArrayList<Sensor> sensorsList;

    public ThermometerCollector(String sensorsFileName) throws IOException {
        File sensorsFile = new File(sensorsFileName);
        if (!sensorsFile.exists()) throw new IOException("Sensor files does not exist " + sensorsFileName);
        sensorsList = new ArrayList<>();
        List<String> tempFileContent = Files.readAllLines(new File(sensorsFileName).toPath());
        for (String s : tempFileContent) {
            String[] device = s.split(",");
            SensorBuilder sb = buildSensor(device);
            sensorsList.add(sb.build());
        }
    }

    private SensorBuilder buildSensor(String[] device) throws IOException {
        String type = device[0];
        String description = device[1];
        String systemDeviceName = device[2];
        return new SensorBuilder(type)
                    .setName(systemDeviceName)
                    .setDesctiption(description);
    }

    public void addSystemSensor() {
        sensorsList.add(new SystemThermometer());
    }

    public int getTempSensorsNumber() {
        return sensorsList.size();
    }


    public ArrayList<Sensor> getSensorsList() {
        return sensorsList;
    }
}


