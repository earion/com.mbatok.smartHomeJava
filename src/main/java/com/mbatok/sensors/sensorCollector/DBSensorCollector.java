package com.mbatok.sensors.sensorCollector;

import com.mbatok.hibernateUtil.HibernateUtil;
import com.mbatok.sensors.sensor.AbstractSensor;
import com.mbatok.sensors.sensor.Sensor;
import org.hibernate.Session;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mateusz on 05.11.16.
 */
public class DBSensorCollector extends SensorCollector {

    public DBSensorCollector() throws IOException {
        super();
        Session session = HibernateUtil.getSession();
        ArrayList<Sensor> sensorList = (ArrayList<Sensor>) session.createQuery("from AbstractSensor").list();
        setSensorsList(sensorList);
    }
}
