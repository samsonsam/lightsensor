import de.htw.samuelerb.lightsensor.SqliteConnector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by samuelerb on 27.12.18.
 * Matr_nr: s0556350
 * Package: PACKAGE_NAME
 */
public class SqliteConnectorTest {
    SqliteConnector sqliteConnector;

    @Test
    public void constructTest() {
        sqliteConnector = new SqliteConnector();
        sqliteConnector.createTableifNotExists("pinSeven");
    }

    @Test
    public void insertTest() {
        sqliteConnector = new SqliteConnector();
        sqliteConnector.insertValues("pinSeven", 500, 600);
    }

    @Test
    public void getByIdTest() {
        sqliteConnector = new SqliteConnector();
        Assertions.assertNotNull(sqliteConnector.getValuesById("pinSeven", 1));
    }

    @Test
    public void dropTest() {
        sqliteConnector = new SqliteConnector();
        sqliteConnector.dropTable("pinSeven");
    }
}
