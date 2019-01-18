package de.htw.samuelerb.lightsensor;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by samuelerb on 27.12.18.
 * Matr_nr: s0556350
 * Package: lightsensor
 */
public class SqliteConnector {
    private final Boolean dbLock = false;
    String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/lightData.sqlite";

    public SqliteConnector() {

    }

    private Connection connect() throws SQLException {
        synchronized (this.dbLock) {
            return DriverManager.getConnection(url);
        }
    }

    public void createTableifNotExists(String table) {
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " ("
                + " id integer primary key autoincrement,"
                + " luxVal integer NOT NULL,"
                + " lightVal integer NOT NULL"
                + ");";
        try (Connection conn = this.connect()) {
            conn.prepareStatement(sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void insertValues(String table, int lux, int lightVal) {
        String sql = "INSERT INTO " + table + "(luxVal,lightVal) VALUES(?,?)";

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, lux);
            pstmt.setInt(2, lightVal);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet getValuesById(String table, int id) {
        String sql = "SELECT luxVal,lightVal FROM " + table + " WHERE id = " + id + ";";
        Statement st;

        try (Connection conn = this.connect()) {
            st = conn.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, Integer> getAllVallues(String table) {
        String sql = "SELECT luxVal,lightVal FROM " + table + ";";
        Statement st;
        try (Connection conn = this.connect()) {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            HashMap<Integer, Integer> map = new HashMap<>();
            try {
                while (rs.next()) {
                    map.put(rs.getInt(1), rs.getInt(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dropTable(String table) {
        String sql = "Drop TABLE " + table + ";";
        try (Connection conn = this.connect()) {
            conn.prepareStatement(sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
