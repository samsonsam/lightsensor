package de.htw.samuelerb.graph;

import de.htw.samuelerb.lightsensor.SqliteConnector;

import java.util.HashMap;


public class App {
    public App() {
    }

// ****************************************************************************
// * JFREECHART DEVELOPER GUIDE                                               *
// * The JFreeChart Developer Guide, written by David Gilbert, is available   *
// * to purchase from Object Refinery Limited:                                *
// *                                                                          *
// * http://www.object-refinery.com/jfreechart/guide.html                     *
// *                                                                          *
// * Sales are used to provide funding for the JFreeChart project - please    *
// * support us so that we can continue developing free software.             *
// ****************************************************************************

    public static void main(final String[] args) {
        Boolean mode = true;
        SqliteConnector connector = new SqliteConnector();


        if (mode) {
            Graph g = new Graph("Lichtintensitätsmessung");
            g.setVisible(true);

            de.htw.samuelerb.lightsensor.App.tableNames.forEach(tn -> {
                HashMap m = connector.getAllVallues(tn);
                g.addSeries(tn, m);
                g.pack();
//                RefineryUtilities.centerFrameOnScreen(g);
            });
        } else {
            de.htw.samuelerb.lightsensor.App.tableNames.forEach(tn -> {
                Graph g = new Graph(tn);
                g.setVisible(true);
                g.addSeries(tn, connector.getAllVallues(tn));
                g.pack();
//                RefineryUtilities.centerFrameOnScreen(g);
            });
        }


    }

}