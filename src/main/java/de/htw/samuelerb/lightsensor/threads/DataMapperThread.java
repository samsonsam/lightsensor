package de.htw.samuelerb.lightsensor.threads;

import de.htw.samuelerb.lightsensor.App;
import de.htw.samuelerb.lightsensor.SqliteConnector;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.HashMap;

/**
 * Created by samuelerb on 30.12.18.
 * Matr_nr: s0556350
 * Package: lightsensor.threads
 */
public class DataMapperThread implements Runnable {

    private SqliteConnector connector;

    public DataMapperThread(SqliteConnector connector) {
        this.connector = connector;
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
        while (true) {
            try {
                Thread.sleep(2 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.calculate();
        }


    }

    public void calculate() {

        App.tableNames.forEach(t -> {
            HashMap<Integer, Integer> map = connector.getAllVallues(t);
            SimpleRegression simpleRegression = new SimpleRegression(true);
            map.forEach(simpleRegression::addData);
            System.out.println("f(x)=" + simpleRegression.getSlope() + "x+" + simpleRegression.getIntercept());
        });


    }
}
