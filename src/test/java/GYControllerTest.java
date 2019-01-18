import de.htw.samuelerb.lightsensor.GYController;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by samuelerb on 27.12.18.
 * Matr_nr: s0556350
 * Package: PACKAGE_NAME
 */
public class GYControllerTest {
    private static GYController gyController;

    @BeforeAll
    static void initAll() {
        gyController = new GYController();
    }

    @AfterAll
    static void tearDownAll() {
    }

    @BeforeEach
    void init() {
    }

    @Test
    void parseBytesTest() {
//        int lowByte = 0xFF & bytes[1];  high[1] | low[0]
//        int highByte = 0xFF & bytes[0];

        byte[] bytes = {(byte) 0x00, (byte) 0x40};
        int actual = gyController.parseUINTBigEndian(bytes);
        int expected = 16_384;
        assertEquals(expected, actual);
    }

    @AfterEach
    void tearDown() {
    }

}
