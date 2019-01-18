package de.htw.samuelerb.lightsensor;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

enum InstructionSet {

    POWER_DOWN(0x00), // No active state
    POWER_ON(0x01), // Power on
    RESET(0x07), // Reset data register value

    // Start measurement at 1lx resolution. Time typically 120ms
    CONTINUOUS_HIGH_RES_MODE_1(0x10),

    // Start measurement at 0.5lx resolution. Time typically 120ms
    CONTINUOUS_HIGH_RES_MODE_2(0x11),

    // Start measurement at 4lx resolution. Time typically 16ms.
    CONTINUOUS_LOW_RES_MODE(0x13),

    // Start measurement at 1lx resolution. Time typically 120ms
    // Device is automatically set to Power Down after measurement.
    ONE_TIME_HIGH_RES_MODE_1(0x20),

    // Start measurement at 0.5lx resolution. Time typically 120ms
    // Device is automatically set to Power Down after measurement.
    ONE_TIME_HIGH_RES_MODE_2(0x21),

    // Start measurement at 1lx resolution. Time typically 120ms
    // Device is automatically set to Power Down after measurement.
    ONE_TIME_LOW_RES_MODE(0x23);

    public int instruction;

    InstructionSet(int i) {
        this.instruction = i;
    }
}

/**
 * This class handles the GY-30 light sensor
 */
public class GYController {
    private I2CBus bus;
    private I2CDevice gy30;

    public GYController() {
        try {
            this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
            this.gy30 = bus.getDevice(0x23);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int parseUINTBigEndian(byte[] bytes) {
        int highByte = 0xFF & bytes[0];
        int lowByte = 0xFF & bytes[1];
        int unsignedHigh = Integer.parseUnsignedInt(String.valueOf(highByte));
        int unsignedLow = Integer.parseUnsignedInt(String.valueOf(lowByte));
        int returnNumber = unsignedHigh << 8 | unsignedLow;
        return returnNumber;
    }

    public double applyScale(int twoBytes) {
        return twoBytes / 1.2;
    }

    public int getLux() {
        byte[] readBuffer = new byte[2];
        int readOffset = 0;
        int readSize = 2;

        synchronized (this.bus) {
            try {
                this.gy30.read(
                        (byte) InstructionSet.ONE_TIME_HIGH_RES_MODE_2.instruction,    // int address
                        readBuffer,                                             // byte[] buffer,
                        readOffset,                                             // int offset,
                        readSize                                                // int size
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (int) Math.round(applyScale(parseUINTBigEndian(readBuffer)));
    }
}
