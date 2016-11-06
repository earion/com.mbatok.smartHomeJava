package com.mbatok.hibernateUtil;

import com.mbatok.sensors.sensor.AbstractSensor;
import com.mbatok.sensors.sensor.SensorResult;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

/**
 * Contains utility methods
 *
 * @author srccodes.com
 * @version 1.0
 *
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory = null;
    private static StandardServiceRegistry serviceRegistry = null;

    private static SessionFactory configureSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(AbstractSensor.class);
        configuration.addAnnotatedClass(com.mbatok.sensors.dummySensor.DummySensor.class);
        configuration.addAnnotatedClass(com.mbatok.sensors.temperature.DS18S20.class);
        configuration.addAnnotatedClass(com.mbatok.sensors.temperature.SystemThermometer.class);
        configuration.addAnnotatedClass(com.mbatok.sensors.humidity.DHT22Humidity.class);
        configuration.addAnnotatedClass(com.mbatok.sensors.humidity.DHT22Temperature.class);

        configuration.addAnnotatedClass(SensorResult.class);


        Properties properties = configuration.getProperties();

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(properties).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }

    // We need to configure session factory once.
    // Rest of the time we will get session using the same.
    static {
        configureSessionFactory();
    }

    private HibernateUtil() {}

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void closeSession() {
        sessionFactory.close();
    }
}