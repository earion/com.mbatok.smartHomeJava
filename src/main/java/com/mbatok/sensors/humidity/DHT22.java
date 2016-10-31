package com.mbatok.sensors.humidity;

import com.mbatok.sensors.Sensor;
import com.mbatok.sensors.temperature.ThermometerSensor;
import com.mbatok.sys.cli.CLIProcess;

import java.io.IOException;

/**
 * Created by mateusz on 27.10.16.
 */
public abstract class DHT22 implements Sensor {

    private float temperature;
    private float humidity;
    private String description;

    protected DHT22()  {
        temperature=0;
        humidity = 0;
    }

    @Override
    public String getSensorType() {
        return "DHT22";
    }

        protected void readTemperatureAndHumidity() {
        try {
            String dht22output = executeReadingAndGetResult();
            extractHumidityAndTemeperatureData(dht22output);
        } catch (IOException e) {
        }
    }

    protected void extractHumidityAndTemeperatureData(String dht22output) {
        String[]  dht22goodOutputArray  = dht22output.split(",");
        if(Float.valueOf(dht22goodOutputArray[0]) != 0) {
            humidity = Float.valueOf(dht22goodOutputArray[0]);
        }
        temperature = Float.valueOf(dht22goodOutputArray[1]);
    }

    protected String executeReadingAndGetResult() throws IOException {
        CLIProcess runDht22read = new CLIProcess("sudo /home/pi/dev/dht22");
        String dht22output =  runDht22read.executeWithTimeoutInSeconds(3).getSuccessMessage();
        if(dht22output.contains("IO Error")) {
            throw  new IOException("DHT22 IO Error");
        }
        return dht22output;
    }

    protected Float readHumidity() {
        readTemperatureAndHumidity();
        return humidity;
    }

    protected Float readTemperature()  {
        readTemperatureAndHumidity();
        return temperature;
    }


}
