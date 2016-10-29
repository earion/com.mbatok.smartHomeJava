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
public class WeatherStation {

    private final I2cLCD lcd;
    private final ThermometerCollector thermometerCollector;
    private final DHT22 dht22;

    public static void main(String[] args) throws IOException, InterruptedException, I2CFactory.UnsupportedBusNumberException {
        WeatherStation ws = new WeatherStation();
        ws.run();
    }


    public WeatherStation() throws IOException, I2CFactory.UnsupportedBusNumberException {
        lcd = LCDFactory.createLCD(I2CBus.BUS_1,0x27);
        thermometerCollector = new ThermometerCollector("classes/thempSensorsList.csv");
        dht22 = new DHT22();
    }


    public void run() throws IOException, InterruptedException {
        while(true) {
            runOneReadCycle();
            Thread.sleep(2000);
        }
    }


    public  void showAsciiTable(I2cLCD lcd) throws IOException {
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


    public  void runOneReadCycle() throws IOException {
        lcd.setTextForWholeLcdLength(0, getSystemDateAsString());
        for (int i = 0; i < thermometerCollector.getTempSensorsNumber(); i++) {
            try {
                lcd.setTextForWholeLcdLength(i + 1, getSpecyficTemperatureSensor(i));
            } catch (IOException e) {
                lcd.setText(i+1,e.getMessage());
            }
        }
        lcd.setTextForWholeLcdLength(3, dht22.getDescriptionAndHumidityValue());
    }

    private String getSpecyficTemperatureSensor(int i) throws IOException {
        return thermometerCollector.getSensorsList().get(i).getDescriptionAndTemperatureValue();
    }


    private String getSystemDateAsString() {
        Instant instant = Instant.now();
        ZoneOffset zoneOffset = ZoneOffset.of( "+02:00" );
        OffsetDateTime odt = OffsetDateTime.ofInstant( instant , zoneOffset );
        return odt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
