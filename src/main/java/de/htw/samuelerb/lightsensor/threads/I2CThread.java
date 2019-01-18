package de.htw.samuelerb.lightsensor.threads;

import de.htw.samuelerb.lightsensor.App;
import de.htw.samuelerb.lightsensor.GYController;

/**
 * Created by samuelerb on 27.12.18.
 * Matr_nr: s0556350
 * Package: lightsensor.threads
 */
public class I2CThread implements Runnable {
    private GYController gyController;

    public I2CThread(GYController gyController) {
        this.gyController = gyController;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        int lux = this.gyController.getLux();
        App.luxList.add(lux);
//        System.out.println("Lux: " + lux);
    }
}
