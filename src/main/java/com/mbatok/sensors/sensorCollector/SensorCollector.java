package com.mbatok.sensors.sensorCollector;

import com.mbatok.sensors.sensor.Sensor;
import com.mbatok.sensors.sensor.SensorBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mateusz on 25.08.16.
 */
public class SensorCollector {

    private ArrayList<Sensor> sensorsList;

    protected SensorCollector() {
        sensorsList = new ArrayList<>();
    }

    public SensorCollector(String sensorsFileName) throws IOException {

        File sensorsFile = new File(sensorsFileName);
        if (!sensorsFile.exists()) throw new IOException("Sensor files does not exist " + sensorsFileName);
        List<String> tempFileContent = Files.readAllLines(new File(sensorsFileName).toPath());
        for (String s : tempFileContent) {
            String[] device = s.split(",");
            SensorBuilder sb = buildSensor(device);
            sensorsList.add(sb.build());
        }
    }

    protected void setSensorsList(ArrayList<Sensor> list) {
        this.sensorsList = list;
    }

    private SensorBuilder buildSensor(String[] device) throws IOException {
        String type = device[0];
        String description = device[1];
        String systemDeviceName = device[2];
        return new SensorBuilder(type)
                    .setName(systemDeviceName)
                    .setDesctiption(description);
    }


    public int getSensorsNumber() {
        return sensorsList.size();
    }


    public ArrayList<Sensor> getSensorsList() {
        return sensorsList;
    }
}


