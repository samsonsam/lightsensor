package de.htw.samuelerb.lightsensor;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;

/**
 * Created by samuelerb on 27.12.18.
 * Matr_nr: s0556350
 * Package: lightsensor
 */
public class GPIOController {
    private final Pin pin;
    private GpioPinDigitalMultipurpose gpioPin;

    public GPIOController(Pin pin) {
        this.pin = pin;
        this.gpioPin = GpioFactory.getInstance().provisionDigitalMultipurposePin(pin, PinMode.DIGITAL_OUTPUT);
    }

    public int measureLight() {
        int count = 0;
        int timeDelta;
        synchronized (this.pin) {
            this.gpioPin.setMode(PinMode.DIGITAL_OUTPUT);
            this.gpioPin.low();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.gpioPin.setMode(PinMode.DIGITAL_INPUT);

            long start = System.nanoTime();
            while (this.gpioPin.isLow()) {
                count++;
            }
            long stop = System.nanoTime();

            timeDelta = (int) (stop - start);
        }
        return timeDelta;
    }
}
