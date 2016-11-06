package com.mbatok.sensors.temperature;

import com.mbatok.sensors.sensor.AbstractSensor;
import com.mbatok.sensors.sensor.SensorResult;

import javax.persistence.Entity;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by mateusz on 25.08.16.
 */
@Entity
public class DS18S20 extends AbstractSensor {

    private static final String path = "/sys/bus/w1/devices";
    private static final String tempheratureFile = "w1_slave";



    public DS18S20() {
        super(DS18S20.generateDeegreSymbolForLCDDisplay());
    }

    public void setName(String sensorName) throws IOException {
        checkSensorExists(sensorName);
        checkIfSensorIsThermometer(sensorName);
        super.setName(sensorName);
    }

    private void checkIfSensorIsThermometer(String sensorName) throws IOException {
        if(!sensorName.startsWith("10") && !sensorName.startsWith("28")) throw new IOException("Device " + sensorName + " is not thermometer");
    }

    public void checkSensorExists(String sensorName) throws IOException {
        File sensor = new File(path + "/" + sensorName);
        if(!sensor.exists()) throw new IOException("Sensor " + sensorName + " does not exit");
    }

    public Float readTemperature() throws IOException {
        checkSensorExists(getName());
        String thempFileDir = path + "/" + getName() + "/" + tempheratureFile;
        File tempFile = new File(thempFileDir);
        List<String> tempFileContent = Files.readAllLines(tempFile.toPath());
        checkIfSensorReadinghasGoodChecksum(tempFileContent.get(0));
        return parseTemperature(tempFileContent.get(1));
    }

    private Float parseTemperature(String tempLine) {
        String tempRead = tempLine.substring(tempLine.indexOf("=")+1);
        Float accurateRead = Float.valueOf(tempRead) / 1000;
        return accurateRead;
    }

    public static String generateDeegreSymbolForLCDDisplay() {
        return Character.toString((char) 161) + "C";
    }

    private void checkIfSensorReadinghasGoodChecksum(String lineWIthCrcReading) throws IOException {
        if(!lineWIthCrcReading.endsWith("YES")) throw new IOException("Sensor " + getName()  + " damaged");
    }

    @Override
    public SensorResult read() throws IOException {
        return new SensorResult(readTemperature(),this);
    }


}



