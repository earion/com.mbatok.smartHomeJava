import com.mbatok.i2c.lcd.I2cLCD;
import com.mbatok.i2c.lcd.LCDFactory;
import com.mbatok.sensors.humidity.DHT22;
import com.mbatok.sensors.temperature.ThermometerCollector;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


/**
 * Created by mateusz on 17.08.16.
 */





public class Run {
    public static void main(String[] args) throws IOException, InterruptedException, I2CFactory.UnsupportedBusNumberException {
        I2cLCD lcd = LCDFactory.createLCD(I2CBus.BUS_1,0x27);

        ThermometerCollector tc = new ThermometerCollector("classes/thempSensorsList.csv");
        while(true) {
            runWheatherStation(lcd, tc);
            //showAsciiTable(lcd);
            //lcd.setBacklight();

           /* lcd.setBacklight(Color.OFF);
            Thread.sleep(4000);
            lcd.setBacklight(Color.ON);*/
        }
    }


    private static void showAsciiTable(I2cLCD lcd) throws IOException {
        int row = 0;
        String line = new String();
        for (int i=161;i<240;i++) {
            if(i % 20 == 0) {
                lcd.setText(row,line);
                row++;
                line = "";
            }
            line +=Character.toString((char) i);
        }
    }


    private static void runWheatherStation(I2cLCD lcd, ThermometerCollector tc) throws IOException, InterruptedException {
        lcd.setText(0,Run.getSystemDateAsString());
        for (int i = 0; i < tc.getTempSensorsNumber(); i++) {
            try {
                lcd.setText(i + 1, tc.getSensorsList().get(i).getDescriptionAndTemperatureValue());
            } catch (IOException e) {
                lcd.setText(i+1,e.getMessage());
            }
        }
        DHT22 dht22 = new DHT22();
        lcd.setText(3,dht22.getDescriptionAndHumidityValue());
        Thread.sleep(2000);
    }


    private static String getSystemDateAsString() {
        Instant instant = Instant.now();
        ZoneOffset zoneOffset = ZoneOffset.of( "+02:00" );
        OffsetDateTime odt = OffsetDateTime.ofInstant( instant , zoneOffset );
        return odt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
