package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*The DBTest class is a test class that tests the functionality of the DB class. It contains two test methods: testGetConnection
 and testClose. These methods test the getConnection and close methods of the DB class respectively. */
public class DBTest {

    private DB db;

    /*
     * setup: This method is annotated with @BeforeEach and is executed before each
     * test method. It creates a new instance of the DB class.
     */
    @BeforeEach
    public void setup() {
        // Create a new instance of the DB class before each test
        db = new DB();
    }

    /*
     * cleanup: This method is annotated with @AfterEach and is executed after each
     * test method. It closes the connection to the database.
     */
    @AfterEach
    public void cleanup() {
        try {
            // Close the connection after each test
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * testGetConnection: This method is annotated with @Test and tests the
     * getConnection method of the DB class.
     * It calls the getConnection method and asserts that the returned connection is
     * not null.
     */
    @Test
    public void testGetConnection() {
        try {
            // Call the getConnection method
            Connection connection = db.getConnection();

            // Check that the connection is not null
            assertNotNull(connection);

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    /*
     * testClose: This method is annotated with @Test and tests the close method of
     * the DB class. It creates a new instance of the DB class, calls the
     * getConnection method, asserts that the returned connection is not null,
     * and then calls the close method. It asserts that the connection is closed
     * after calling the close method.
     */
    @Test
    public void testClose() {
        try (DB db = new DB()) {
            Connection connection = db.getConnection();
            assertNotNull(connection);

            db.close();
            assertTrue(connection.isClosed());
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

}