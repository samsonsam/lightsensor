package de.htw.samuelerb.lightsensor;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import de.htw.samuelerb.graph.Graph;
import de.htw.samuelerb.lightsensor.threads.DataBaseWriterThread;
import de.htw.samuelerb.lightsensor.threads.DataMapperThread;
import de.htw.samuelerb.lightsensor.threads.GPIOTread;
import de.htw.samuelerb.lightsensor.threads.I2CThread;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by samuelerb on 07.12.18.
 * Matr_nr: s0556350
 * Package: lightsensor
 */
public class App {
    public static final ConcurrentLinkedQueue<Object[]> lightList = new ConcurrentLinkedQueue<>();
    public static final HashMap<String, HashMap<Integer, Integer>> valueMap = new HashMap<>();
    public static ConcurrentLinkedQueue<Integer> luxList = new ConcurrentLinkedQueue<>();
    public static List<String> tableNames = Arrays.asList(
            "orange",
            "green",
            "blue");

    public static void main(String[] args) {
        Pin orange = RaspiPin.getPinByAddress(0);
        Pin green = RaspiPin.getPinByAddress(2);
        Pin blue = RaspiPin.getPinByAddress(3);

        tableNames.forEach(t -> valueMap.put(t, new HashMap<Integer, Integer>()));

        SqliteConnector connector = new SqliteConnector();

        GYController gyController = new GYController();
        GPIOController sevenGpio = new GPIOController(orange);
        GPIOController zeroGpio = new GPIOController(green);
        GPIOController twoGpio = new GPIOController(blue);

        connector.dropTable("orange");
        connector.dropTable("green");
        connector.dropTable("blue");

        connector.createTableifNotExists(tableNames.get(0));
        connector.createTableifNotExists(tableNames.get(1));
        connector.createTableifNotExists(tableNames.get(2));

        Graph g = new Graph("Lichtintensitätsmessung");

        DataBaseWriterThread dataBaseWriterThread = new DataBaseWriterThread(connector, g);
        Thread dbThread = new Thread(dataBaseWriterThread);

        dbThread.start();

        g.setVisible(true);


        DataMapperThread dataMapperThread = new DataMapperThread(connector);
        Thread dmt = new Thread(dataMapperThread);
        dmt.start();

        while (true) {
            while (luxList.size() > 1) {
                luxList.poll();
            }

            GPIOTread sevengpioTread = new GPIOTread(sevenGpio, connector, tableNames.get(0));
            GPIOTread zerogpioTread = new GPIOTread(zeroGpio, connector, tableNames.get(1));
            GPIOTread twogpioTread = new GPIOTread(twoGpio, connector, tableNames.get(2));

            I2CThread i2CThread = new I2CThread(gyController);

            Thread seven = new Thread(sevengpioTread);
            Thread zero = new Thread(zerogpioTread);
            Thread two = new Thread(twogpioTread);
            Thread i = new Thread(i2CThread);

            i.start();
            seven.start();
            zero.start();
            two.start();


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println();
        }
    }
}
