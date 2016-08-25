import com.mbatok.i2c.lcd.I2cLCD;
import com.mbatok.i2c.lcd.LCDFactory;
import com.mbatok.temp.ThermometerCollector;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.system.SystemInfo;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by mateusz on 17.08.16.
 */



public class Run {
    private static GpioPinDigitalOutput RSpin;
    private static GpioPinDigitalOutput ENpin;

    public static void main(String[] args) throws IOException, InterruptedException, I2CFactory.UnsupportedBusNumberException {
        System.out.println(SystemInfo.getCpuTemperature());


        I2cLCD lcd = LCDFactory.createLCD(I2CBus.BUS_1,0x27);

        ThermometerCollector tc = new ThermometerCollector("classes/thempSensorsList.csv");
        while(true) {
            lcd.setText(0,Run.getSystemDateAsString() );
            for (int i = 0; i < 3; i++) {
                lcd.setText(i + 1, tc.getSensorsList().get(i).getDesctipiontAndTemperatureValue());
            }
            Thread.sleep(10000);
           /* lcd.setBacklight(Color.OFF);
            Thread.sleep(4000);
            lcd.setBacklight(Color.ON);*/
        }


    }

    private static String getSystemDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
