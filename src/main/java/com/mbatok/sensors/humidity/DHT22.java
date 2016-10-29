package com.mbatok.sensors.humidity;

import com.mbatok.sensors.temperature.ThermometerSensor;
import com.mbatok.sys.cli.CLIProcess;

import java.io.IOException;

/**
 * Created by mateusz on 27.10.16.
 */
public class DHT22  implements HumiditySensor,ThermometerSensor {

    private final Runtime runtime;
    private float temperature;
    private float humidity;
    private String description;

    public DHT22()  {
        runtime = Runtime.getRuntime();
        description = "DHT22";
        temperature=0;
        humidity = 0;
    }

    private void readTemperatureAndHumidity() {
        try {
            String dht22output = executeReadingAndGetResult();
            extractHumidityAndTemeperatureData(dht22output);
        } catch (IOException e) {
        }
    }

    private void extractHumidityAndTemeperatureData(String dht22output) {
        String[]  dht22goodOutputArray  = dht22output.split(",");
        humidity = Float.valueOf(dht22goodOutputArray[0]);
        temperature = Float.valueOf(dht22goodOutputArray[1]);
    }

    private String executeReadingAndGetResult() throws IOException {
        CLIProcess runDht22read = new CLIProcess("sudo /home/pi/dev/dht22");
        String dht22output =  runDht22read.executeWithTimeoutInSeconds(3).getSuccessMessage();
        if(dht22output.contains("IO Error")) throw  new IOException("DHT22 IO Error");
        return dht22output;
    }


    @Override
    public Float readHumidity() {
        readTemperatureAndHumidity();
        return humidity;
    }

    @Override
    public String readHumidityAsString()  {
        return String.format("%1$.1f",readHumidity());
    }

    @Override
    public Float readTemperature()  {
        readTemperatureAndHumidity();
        return temperature;
    }

    @Override
    public String readTemperatureAsString() {
       return readTemperature().toString();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDescriptionAndTemperatureValue() throws IOException {
        return "Wewnatrz T:" + " " + readTemperatureAsString() + " C";
    }

    @Override
    public String getDescriptionAndHumidityValue() throws IOException {
        return "Wilgotnosc" + " " + readHumidityAsString() + " %";
    }
}
