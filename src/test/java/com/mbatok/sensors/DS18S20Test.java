package com.mbatok.sensors;

import com.mbatok.sensors.temperature.DS18S20;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by mateusz on 25.08.16.
 */
public class DS18S20Test {


    @Test
    public void shoudlCheckIfThermomterExist() {

            DS18S20 dt = new DS18S20();


        fail("Device must not exist");

    }

}



