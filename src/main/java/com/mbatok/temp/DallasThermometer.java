package com.mbatok.temp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by mateusz on 25.08.16.
 */
public class DallasThermometer implements Thermometer {

    private final String path = "/sys/bus/w1/devices";
    private final String tempheratureFile = "w1_slave";
    private final String sensorName;
    private String description;

    public DallasThermometer(String sensorName) throws IOException {
        checkSensorExists(sensorName);
        checkIfSensorIsThermometer(sensorName);
        this.sensorName = sensorName;
        this.description="empty";
    }

    public DallasThermometer(String sensorName,String description) throws IOException {
        this(sensorName);
        this.description = description;
    }

    private void checkIfSensorIsThermometer(String sensorName) throws IOException {
        if(!sensorName.startsWith("10") && !sensorName.startsWith("28")) throw new IOException("Device " + sensorName + " is not thermometer");
    }

    public void checkSensorExists(String sensorName) throws IOException {
        File sensor = new File(path + "/" + sensorName);
        if(!sensor.exists()) throw new IOException("Sensor " + sensorName + " does not exit");
    }


    @Override
    public Float readThemperature() throws IOException {
        checkSensorExists(sensorName);
        String thempFileDir = path + "/" + sensorName + "/" + tempheratureFile;
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


    @Override
    public String readTemperatureAsString() throws IOException {
        Float temp = readThemperature();
        DecimalFormat df=new DecimalFormat("0.0");
        return df.format(temp);
    }


    private void checkIfSensorReadinghasGoodChecksum(String lineWIthCrcReading) throws IOException {
        if(!lineWIthCrcReading.endsWith("YES")) throw new IOException("Sensor " + sensorName  + " damaged");
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDesctipiontAndTemperatureValue() throws IOException {
        return getDescription() + " " + readTemperatureAsString();
    }
}



