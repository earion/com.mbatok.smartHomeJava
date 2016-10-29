package com.mbatok.sensors;

import com.mbatok.sensors.temperature.DallasThermometer;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by mateusz on 25.08.16.
 */
public class DallasThermometerTest {


    @Test
    public void shoudlCheckIfThermomterExist() {
        try {
            DallasThermometer dt = new DallasThermometer("10-000802dc0b18");
        } catch (IOException e) {
            assertThat(e.getMessage()).contains("not exists");
        }
        fail("Device must not exist");

    }

}



