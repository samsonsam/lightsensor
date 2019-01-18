package de.htw.samuelerb.lightsensor.threads;

import de.htw.samuelerb.graph.Graph;
import de.htw.samuelerb.lightsensor.App;
import de.htw.samuelerb.lightsensor.SqliteConnector;

import java.util.Objects;

/**
 * Created by samuelerb on 28.12.18.
 * Matr_nr: s0556350
 * Package: lightsensor.threads
 */
public class DataBaseWriterThread implements Runnable {
    private SqliteConnector connector;
    private Graph g;

    public DataBaseWriterThread(SqliteConnector connector, Graph g) {
        this.connector = connector;
        this.g = g;
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
            if (!App.lightList.isEmpty()) {
                Object[] obj = App.lightList.poll();
                this.addToDatabase(obj);
            }
        }

    }

    private boolean addToDatabase(Object[] obj) {
        try {

            String table = (String) Objects.requireNonNull(obj)[0];
            int lux = (Integer) obj[1];
            int light = (Integer) obj[2];
            connector.insertValues(table, lux, light);
            synchronized (App.valueMap) {
                App.valueMap.get(table).put(light, lux);
            }
            g.addEntry(table, lux, light);
            g.pack();
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
