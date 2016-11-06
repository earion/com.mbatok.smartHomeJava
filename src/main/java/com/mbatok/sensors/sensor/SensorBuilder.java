package com.mbatok.sensors.sensor;

import com.mbatok.hibernateUtil.HibernateUtil;
import com.mbatok.sensors.dummySensor.DummySensor;
import com.mbatok.sensors.humidity.DHT22Humidity;
import com.mbatok.sensors.humidity.DHT22Temperature;
import com.mbatok.sensors.temperature.DS18S20;
import com.mbatok.sensors.temperature.SystemThermometer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

/**
 * Created by mateusz on 31.10.16.
 */
public class SensorBuilder {
    private AbstractSensor sensor;

    public SensorBuilder(String sensorType)  {
        switch (sensorType) {
            case "DS1820" : {
                sensor = new DS18S20();
                break;
            } case "DHT22T" : {
                sensor = new DHT22Temperature();
                 break;
            } case "DHT22H" : {
                sensor = new DHT22Humidity();
                break;
            } case "CPUT" : {
                sensor  = new SystemThermometer();
                break;
            } case "Dummy": {
                sensor = new DummySensor();
                break;
            } default: {
                throw new IllegalArgumentException("Sensor " + sensorType + " Not supported");
            }
        }
    }

    public SensorBuilder setName(String name) throws IOException {
        sensor.setName(name);
        return this;
    }

    public SensorBuilder setDesctiption(String desctiption) {
        sensor.setDescription(desctiption);
        return this;
    }

    public SensorBuilder setUnit(String unit) {
        sensor.setUnit(unit);
        return this;
    }

    public AbstractSensor build() {
        if(sensor.getName() == null) throw new IllegalArgumentException("name not set on sensor");
        if(sensor.getDescription() == null) throw new IllegalArgumentException("description not set on sensor");
        if(sensor.getUnit() == null) throw new IllegalArgumentException("unit not set on sensor");
        //WriteSensorToDatabase();
        return sensor;
    }

    private void WriteSensorToDatabase() {
        Session session = HibernateUtil.getSession();
        Transaction ts =  session.beginTransaction();
        session.save(sensor);
        ts.commit();
    }

}
