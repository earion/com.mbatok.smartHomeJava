import com.mbatok.i2c.lcd.I2cLCD;
import com.mbatok.i2c.lcd.LCDFactory;
import com.mbatok.sensors.sensor.SensorResult;
import com.mbatok.sensors.sensorCollector.DBSensorCollector;
import com.mbatok.sensors.sensorCollector.SensorCollector;
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
    private final SensorCollector sensorCollector;

    public static void main(String[] args) throws IOException, InterruptedException, I2CFactory.UnsupportedBusNumberException {
        WeatherStation ws = new WeatherStation();
        ws.run();
    }

    public WeatherStation() throws IOException, I2CFactory.UnsupportedBusNumberException {
        lcd = LCDFactory.createLCD(I2CBus.BUS_1,0x27);
        //sensorCollector = new SensorCollector("classes/thempSensorsList.csv");
        sensorCollector = new DBSensorCollector();
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

    public  void runOneReadCycle() throws IOException, InterruptedException {
        lcd.setTextForWholeLcdLength(0, getSystemDateAsString());
        for (int i = 0; i < sensorCollector.getSensorsNumber(); i++) {
            SensorResult sr = sensorCollector.getSensorsList().get(i).read();
            putSensorReadOnTheLcdLine(i, sr);
            sr.save();
        }
        Thread.sleep(60000);
    }

    private void putSensorReadOnTheLcdLine(int lcdLine,SensorResult sr) throws IOException {
        if(lcdLine < 3) {
            try {
                lcd.setTextForWholeLcdLength(lcdLine + 1, getSpecyficSensorReading(lcdLine, sr));
            } catch (IOException e) {
                lcd.setText(lcdLine + 1, e.getMessage());
            }
        }
    }

    private String getSpecyficSensorReading(int i,SensorResult sr) throws IOException {
        return sensorCollector.getSensorsList().get(i).getDescription() + " " +
                 String.format("%2.1f" , sr.getValue())
                + sensorCollector.getSensorsList().get(i).getUnit();
    }


    private String getSystemDateAsString() {
        Instant instant = Instant.now();
        ZoneOffset zoneOffset = ZoneOffset.of( "+01:00" );
        OffsetDateTime odt = OffsetDateTime.ofInstant( instant , zoneOffset );
        return odt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
