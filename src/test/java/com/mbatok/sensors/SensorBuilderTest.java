package com.mbatok.sensors;
import static org.assertj.core.api.Assertions.*;

import com.mbatok.hibernateUtil.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by mateusz on 31.10.16.
 */
public class SensorBuilderTest {


    @Test
    public void makeMeDummySensor() throws IOException {
        SensorBuilder sb = new SensorBuilder("Dummy");
        Sensor ds = sb.setName("Dummy_Name").setDesctiption("Dummy_Description").setUnit("dup").build();
        assertThat(ds.getDescription()).isEqualTo("Dummy_Description");
        assertThat(ds.getUnit()).isEqualTo("dup");
    }


    @Test
    public void saveDummySensor() throws IOException {
        SensorBuilder sb = new SensorBuilder("Dummy");
        Sensor ds = sb.setName("Dummy_Name").setDesctiption("Dummy_Description").setUnit("dup").build();
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        session.save(ds);
        tx.commit();
        HibernateUtil.closeSession();
    }
}
