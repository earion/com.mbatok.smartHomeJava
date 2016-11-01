package com.mbatok.sensors;

/**
 * Created by mateusz on 31.10.16.
 */
public class SensorResult {

    float value;
    String unit;
    String sensorDesctiption;

    public float getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getSensorDesctiption() {
        return sensorDesctiption;
    }

    public SensorResult(float value, String unit, String sensorDesctiption) {

        this.value = value;
        this.unit = unit;
        this.sensorDesctiption = sensorDesctiption;
    }
}
