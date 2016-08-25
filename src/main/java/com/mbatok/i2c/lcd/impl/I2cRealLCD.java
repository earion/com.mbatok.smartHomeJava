package com.mbatok.i2c.lcd.impl;

import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.mbatok.i2c.lcd.Color;
import com.mbatok.i2c.lcd.I2cLCD;


import java.io.IOException;

/**
 * Created by mateusz on 23.08.16.
 */
public class I2cRealLCD implements I2cLCD {

    //LCD Address
    private static final int LCD_I2C_ADDRESS = 0x27;
    private static final int BUS_1 = 1;

    // LCD Commands
    private static final int LCD_CLEARDISPLAY = 0x01;
    private static final int LCD_RETURNHOME = 0x02;
    private static final int LCD_ENTRYMODESET = 0x04;
    private static final int LCD_DISPLAYCONTROL = 0x08;
    private static final int LCD_CURSORSHIFT = 0x10;
    private static final int LCD_FUNCTIONSET = 0x20;
    private static final int LCD_SETCGRAMADDR = 0x40;
    private static final int LCD_SETDDRAMADDR = 0x80;

    // Flags for display on/off control
    private static final int LCD_DISPLAYON = 0x04;
    private static final int LCD_DISPLAYOFF = 0x00;
    private static final int LCD_CURSORON = 0x02;
    private static final int LCD_CURSOROFF = 0x00;
    private static final int LCD_BLINKON = 0x01;
    private static final int LCD_BLINKOFF = 0x00;

    // Flags for display entry mode
    private static final int LCD_ENTRYRIGHT = 0x00;
    private static final int LCD_ENTRYLEFT = 0x02;
    private static final int LCD_ENTRYSHIFTINCREMENT = 0x01;
    private static final int LCD_ENTRYSHIFTDECREMENT = 0x00;

    // Flags for display/cursor shift
    private static final int LCD_DISPLAYMOVE = 0x08;
    private static final int LCD_CURSORMOVE = 0x00;
    private static final int LCD_MOVERIGHT = 0x04;
    private static final int LCD_MOVELEFT = 0x00;







    //flags for function set
    private static final int LCD_8BITMODE = 0x10;
    private static final int LCD_4BITMODE = 0x00;
    private static final int LCD_2LINE = 0x08;
    private static final int LCD_1LINE = 0x00;
    private static final int LCD_5x10DOTS = 0x04;
    private static final int LCD_5x8DOTS = 0x00;

    //flags for backlight control
    private static final int LCD_BACKLIGHT = 0x08;
    private static final int LCD_NOBACKLIGHT = 0x00;


    //conroll commands
    private static final int En = 0b00000100; // Enable bit
    private static final int Rw = 0b00000010; // Read/Write bit
    private static final int Rs = 0b00000001; // Register select bit
    private final I2CDevice lcd;
    private final int lcdMaxLines;
    private int[] ROW_OFFSETS = {0x80,0xC0,0x94,0xD4};
    private String[] lcdContent;



    private int displayControl = LCD_DISPLAYON | LCD_CURSOROFF | LCD_BLINKOFF;
    private int backlighgState= 1;

    public I2cRealLCD(int lines) throws IOException, I2CFactory.UnsupportedBusNumberException {
        lcd = I2CFactory.getInstance(BUS_1).getDevice(LCD_I2C_ADDRESS);
        initialize();
        lcdContent = new String[4];
        lcdMaxLines = lines;
    }

    private void initialize() throws IOException {
        write(0x03);
        write(0x03);
        write(0x03);
        write(0x02);
        write(LCD_FUNCTIONSET | LCD_2LINE | LCD_5x8DOTS | LCD_4BITMODE);
        write(LCD_DISPLAYCONTROL | LCD_DISPLAYON);
        write(LCD_CLEARDISPLAY);
        write(LCD_ENTRYMODESET | LCD_ENTRYLEFT);
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            throw  new IOException(e);
        }

    }


    private void write(int cmd, int mode) throws IOException {
        lcdWrite4Bits(mode | (cmd & 0xF0));
        lcdWrite4Bits(mode | ((cmd << 4) & 0xF0));
    }

    private void write(int cmd) throws IOException {
        write(cmd,0);
    }


    private void lcdWrite4Bits(int data) throws IOException {
        lcd.write((byte) (data | LCD_BACKLIGHT));
        lcdStrobe(data);
    }




    private void lcdStrobe(int data,int bl) throws IOException {
        int[] backlight = {LCD_NOBACKLIGHT,LCD_BACKLIGHT};
        try {
            lcd.write((byte)(data | En | backlight[bl]));
            Thread.sleep(5);
            lcd.write((byte)(((data & ~En) | backlight[bl])));
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw  new IOException(e);
        }

    }

    private void lcdStrobe(int data) throws IOException {
        lcdStrobe(data,backlighgState);
    }

    @Override
    public void setText(String s) throws IOException {
        String[] str = s.split("\n");
        for (int i = 0; i < str.length; i++) {
            setText(i, str[i]);
        }
    }

    @Override
    public void setText(int row, String lineContent) throws IOException {
        if(row > lcdMaxLines) throw new IOException("To much rows in the text, max value is " + Integer.toString(lcdMaxLines));
        goToLine(row);
        setTextNearCursor(lineContent);
        lcdContent[row] = lineContent;
    }


    public void setTextNearCursor(String text) throws IOException {
        char[] word = text.toCharArray();
        for(char c : word) {
            write((int) c,Rs);
        }
    }


    private void goToLine(int row) throws IOException {
        write(ROW_OFFSETS[row]);
    }

    @Override
    public void setCursorPosition(int row, int column) throws IOException {
        write(LCD_SETDDRAMADDR | (column + ROW_OFFSETS[row]));

    }

    @Override
    public void stop() throws IOException {

    }

    @Override
    public void clear() throws IOException {
        write(LCD_CLEARDISPLAY);
        write(LCD_RETURNHOME);
    }

    @Override
    public void home() throws IOException {
        write(LCD_RETURNHOME);
    }

    @Override
    public void setCursorEnabled(boolean enable) throws IOException {
        modifyScreenParameters(LCD_CURSORON,enable);
    }

    @Override
    public boolean isCursorEnabled() {
        return (displayControl & LCD_CURSORON) > 0;
    }

    @Override
    public void setDisplayEnabled(boolean enable) throws IOException {
        modifyScreenParameters(LCD_DISPLAYON,enable);
    }

    @Override
    public boolean isDisplayEnabled() {
        return (displayControl & LCD_DISPLAYON) > 0;
    }

    @Override
    public void setBlinkEnabled(boolean enable) throws IOException {
       modifyScreenParameters(LCD_BLINKON,enable);
    }

    public void modifyScreenParameters(int parameter, boolean value) throws IOException {
        if(value) {
            displayControl |= parameter;
        } else {
            displayControl &= parameter;
        }
        write(LCD_DISPLAYCONTROL | displayControl);
    }

    @Override
    public boolean isBlinkEnabled() {
        return (displayControl & LCD_BLINKON) > 0;
    }


    @Override
    public void setBacklight(Color color) throws IOException {
        switch (color) {
            case OFF: {
                backlighgState =0;
                break;

            }
            case ON : {
                backlighgState =1;
                break;
            }
            default:throw new IOException("This LCD support only ON/OFF Colors");
        }
        setDisplayEnabled(true);

    }


    @Override
    public Color getBacklight() throws IOException {
        if(backlighgState > 0) return Color.ON;
        else return Color.OFF;
    }

    @Override
    public void scrollDisplay(I2CRealLCDAdafruit.Direction direction) throws IOException {

    }

    @Override
    public void setTextFlowDirection(I2CRealLCDAdafruit.Direction direction) throws IOException {

    }

    @Override
    public void setAutoScrollEnabled(boolean enable) throws IOException {

    }

    @Override
    public boolean isAutoScrollEnabled() {
        return false;
    }

    @Override
    public String getContent(int row) throws IOException {
        if(row > lcdMaxLines) throw new IOException("To much rows in the text, max value is " + Integer.toString(lcdMaxLines));
        return lcdContent[row];
    }


}
