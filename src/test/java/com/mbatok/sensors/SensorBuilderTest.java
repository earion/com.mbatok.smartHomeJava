package com.mbatok.sensors;
import static org.assertj.core.api.Assertions.*;

import com.mbatok.hibernateUtil.HibernateUtil;
import com.mbatok.sensors.sensor.Sensor;
import com.mbatok.sensors.sensor.SensorBuilder;
import com.mbatok.sensors.sensor.SensorResult;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by mateusz on 31.10.16.
 */
public class SensorBuilderTest {

    private Sensor ds;
    private Session session;

    @Before
    public void makeMeDummySensor() throws IOException {
         ds = createDummySensor();
        session = HibernateUtil.getSession();

    }

    @Test
    public void dummySensorCreated() throws IOException {
        //given
        //when
        assertThat(ds.getDescription()).isEqualTo("Dummy_Description");
        assertThat(ds.getUnit()).isEqualTo("dup");
        assertThat(ds.getName()).isEqualTo("Dummy_Name");
    }

    @Test
    public void saveDummySensor() throws IOException {
        Transaction tx = session.beginTransaction();
        session.save(ds);
        tx.commit();
    }

    private Sensor createDummySensor() throws IOException {
        SensorBuilder sb = new SensorBuilder("Dummy");
        return sb.setName("Dummy_Name").setDesctiption("Dummy_Description").setUnit("dup").build();
    }

    @Test
    public void saveMaasurement() throws IOException {
        //given
        SensorResult sr = ds.read();
        Transaction ts = session.beginTransaction();
        //when
        session.save(sr);
        ts.commit();
        List<SensorResult> measuresList = session.createQuery("from SensorResult").list();
        //then
        assertThat(measuresList.get(measuresList.size() -1).getUpdateDate().toString()).isNotEmpty();
    }

    @Test
    public void checkSensorAndMeasurementsCorelation() throws IOException {
        SensorBuilder sb = new SensorBuilder("Dummy");
        sb.setName("TEST").setUnit("Tup").setDesctiption("Unidentyfied");
        Sensor sensor = sb.build();
        Transaction ts = session.beginTransaction();

        session.save(sensor);
        SensorResult result = sensor.read();
        session.save(result);
        ts.commit();
        List<SensorResult> measuresList = session.createQuery("from SensorResult").list();
       assertThat(measuresList.get(measuresList.size()-1).getSensor()).isEqualTo(sensor);

    }


    @AfterClass()
    public static void closeSql() throws IOException {
        HibernateUtil.closeSession();
    }
}
