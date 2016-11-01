package com.mbatok.sensors.humidity;

import com.mbatok.sensors.AbstractSensor;
import com.mbatok.sensors.Sensor;
import com.mbatok.sys.cli.CLIProcess;

import java.io.IOException;

/**
 * Created by mateusz on 27.10.16.
 */
public abstract class DHT22 extends AbstractSensor implements Sensor {

    private float temperature;
    private float humidity;

    DHT22(String type, String unit) {
        super(type, unit);
    }

    private void readTemperatureAndHumidity() throws IOException{
            String dht22output = executeReadingAndGetResult();
            extractHumidityAndTemperatureData(dht22output);
    }

     private void extractHumidityAndTemperatureData(String dht22output) {
        String[]  dht22goodOutputArray  = dht22output.split(",");
        if(Float.valueOf(dht22goodOutputArray[0]) != 0) {
            humidity = Float.valueOf(dht22goodOutputArray[0]);
        }
        temperature = Float.valueOf(dht22goodOutputArray[1]);
    }

    private String executeReadingAndGetResult() throws IOException {
        CLIProcess runDht22read = new CLIProcess("sudo /home/pi/dev/dht22");
        String dht22output =  runDht22read.executeWithTimeoutInSeconds(3).getSuccessMessage();
        if(dht22output.contains("IO Error")) {
            throw  new IOException("DHT22 IO Error");
        }
        return dht22output;
    }

     Float readHumidity() throws IOException {
        readTemperatureAndHumidity();
        return humidity;
    }

     Float readTemperature() throws IOException {
        readTemperatureAndHumidity();
        return temperature;
    }
}
