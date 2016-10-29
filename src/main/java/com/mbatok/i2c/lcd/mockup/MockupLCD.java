/*
 *  Copyright (C) 2014 Marcus Hirt
 *                     www.hirt.se
 *
 * This software is free:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (C) Marcus Hirt, 2014
 */
package com.mbatok.i2c.lcd.mockup;

import com.mbatok.i2c.lcd.I2cLCD;
import com.mbatok.i2c.lcd.impl.I2CRealLCDAdafruit.Direction;
import com.pi4j.io.i2c.I2CBus;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class MockupLCD implements I2cLCD {
	private static final int DDRAM_SIZE = 40;
	private volatile int cursorColumn;
	private volatile int cursorRow;
	private volatile int maskVal;
	private final char[] FIRST_ROW = new char[DDRAM_SIZE];
	private final char[] SECOND_ROW = new char[DDRAM_SIZE];

	private final int bus;
	private final int address;
	private final JTextArea textArea = new JTextArea(2, 16);
	private JFrame frame;
	private com.mbatok.i2c.lcd.Color color = com.mbatok.i2c.lcd.Color.WHITE;
	
	public MockupLCD() {
		// This seems to be the default for AdaFruit 1115.
		this(I2CBus.BUS_1, 0x20);
	}

	public MockupLCD(int bus, int address) {
		this.bus = bus;
		this.address = address;
		initialize();
	}

	private void initialize() {
		textArea.setEditable(false);
		frame = new JFrame(String.format("LCD %d@%xd", bus, address));
		frame.setSize(200, 150);
		frame.getContentPane().add(textArea, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void setText(String s) {
		String[] str = s.split("\n");
		for (int i = 0; i < str.length; i++) {
			setText(i, str[i]);
		}
	}

	public void setText(int row, String string) {
		setCursorPosition(row, 0);
		internalWrite(string);
	}

	@Override
	public void setTextForWholeLcdLength(int row, String string) throws IOException {
		throw new RuntimeException("Not implemented");

	}

	@Override
	public void setTextNearCursor(String text) throws IOException {

	}

	private void internalWrite(String s) {
		char[] buffer = cursorRow == 0 ? FIRST_ROW : SECOND_ROW;
		for (int i = 0; i < s.length() && cursorColumn < DDRAM_SIZE; i++) {
			buffer[cursorColumn++] = s.charAt(i);
		}
		textArea.setText(createStringFromBuffers());
	}

	private String createStringFromBuffers() {
		return String.format("%16s\n%16s", new String(FIRST_ROW), new String(
				SECOND_ROW));
	}

	public void setCursorPosition(int row, int column) {
		cursorRow = row;
		cursorColumn = column;
	}

	public void stop() {
		frame.setVisible(false);
		frame.dispose();
	}

	public void clear() {
		Arrays.fill(FIRST_ROW, (char) 0);
		Arrays.fill(SECOND_ROW, (char) 0);		
	}

	public void home() {
	}

	public void setCursorEnabled(boolean enable) {
	}

	public boolean isCursorEnabled() {
		return false;
	}

	public void setDisplayEnabled(boolean enable) {
	}

	public boolean isDisplayEnabled() {
		return true;
	}

	public void setBlinkEnabled(boolean enable) {
	}

	public boolean isBlinkEnabled() {
		return false;
	}

	public void setBacklight(com.mbatok.i2c.lcd.Color color) {
		this.color = color;
	}

	public void scrollDisplay(Direction direction) {
	}

	public void setTextFlowDirection(Direction direction) {
	}

	public void setAutoScrollEnabled(boolean enable) {
	}

	public boolean isAutoScrollEnabled() {
		return false;
	}

	@Override
	public String getContent(int i) throws IOException {
		return null;
	}


	public int buttonsPressedBitmask() {
		return maskVal;
	}

	public static void main(String[] args) {
		final MockupLCD lcd = new MockupLCD();
		lcd.clear();
		lcd.setText("Hello World!\nDone!");

	}



	public com.mbatok.i2c.lcd.Color getBacklight() throws IOException {
		return color;
	}
}
