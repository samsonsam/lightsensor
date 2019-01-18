package de.htw.samuelerb.lightsensor.threads;

import de.htw.samuelerb.lightsensor.App;
import de.htw.samuelerb.lightsensor.GPIOController;
import de.htw.samuelerb.lightsensor.SqliteConnector;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by samuelerb on 27.12.18.
 * Matr_nr: s0556350
 * Package: lightsensor.threads
 */
public class GPIOTread implements Runnable {
    private GPIOController gpioController;
    private String table;
    private SqliteConnector connector;

    public GPIOTread(GPIOController gpioController, SqliteConnector connector, String tableName) {
        this.gpioController = gpioController;
        this.table = tableName;
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
        int light = this.gpioController.measureLight();
        Integer lux = App.luxList.peek();
        App.lightList.add(new Object[]{this.table, lux, light});

        System.out.println(table + " " + lux + " " + light + " Closest val: " + this.getCorrespondantLuxLevel(light));

    }

    private int getCorrespondantLuxLevel(int lightNanoS) {
        HashMap<Integer, Integer> db;
        synchronized (App.valueMap) {
            db = new HashMap<>(App.valueMap.get(table));
        }
//        Der Datenbestand der sich aus den zuvor gemessenen Werten zusammensetzt

//      TreeMap<lightNanoS, lux> database;
        TreeMap<Integer, Integer> database = new TreeMap<>(db);
//        Der aktuelle Messwert vom LDR -> Zeit in Nanosekunden die es braucht bis der Kondensator
//        bis 3,3V geladen ist
        Integer key = lightNanoS;

//        Die floorEntry(Integer key)-Methode wird benutzt um ein key-value mapping zu liefern,
//        dass den groessten Key, der kleiner oder gleich dem gegebenen key ist, enthaelt.
        Map.Entry<Integer, Integer> low = database.floorEntry(key);
//        Genauso wie die floorEntry()-Methode mit dem Unterschied, dass das mapping den  kleinsten Key enthaelt,
//        der groesser oder gleich dem gegebenen key ist
        Map.Entry<Integer, Integer> high = database.ceilingEntry(key);

//        Zusammengefasst: Das mapping wird an der stelle x geteilt und es wird ein Satz Messdaten oberhalb
//        und unterhalb entnommen (low und high). Das x stellt dabei die Nanosekunden des LDR dar.

        int res = -1;
//        Wenn die key-value mappings low und high einen Wert enthalten
        if (low != null && high != null) {
//            Wenn die Differenz von dem low-key kleiner ist
            res = Math.abs(key - low.getKey()) < Math.abs(key - high.getKey())
//                    wird der low-value zurueck gegeben
                    ? low.getValue()
//                    Andernfalls der high-value
                    : high.getValue();
//            Wenn einer der key-value mappings gefunden wurde
        } else if (low != null || high != null) {
            res = low != null ? low.getValue() : high.getValue();
        }
        return res;
    }

}
