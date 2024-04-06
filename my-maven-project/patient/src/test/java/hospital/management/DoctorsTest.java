import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DoctorsTest {
    
    Doctor doctor = new Doctor("First Name", "Last Name", "abc@gmail.com");

    //Variables for connecting to the database
    private static final String DB_URL = "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management";
    private static final String DB_USER = "MediCare";
    private static final String DB_PASSWORD = "my-secret-pw";

    @Test
    void connectToDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            assertNotNull(connection, "Database is not connected!");
            assertTrue(connection.isValid(5), "Database connection is not valid!");
        } catch (SQLException e) {
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }
    
    @Test
    public void testRetrieveDoctorFirstNameFromDatabase() {
        // Test retrieving a doctor from the database
        Database db = Database.getInstance();
        assertNotNull(doctor, "Doctor not found!");
        assertEquals("First Name", doctor.getFirstName(), "Incorrect first name for retrieved doctor!");
    }
    
    @Test
    public void testRetrieveDoctorLastNameFromDatabase() {
        // Test retrieving a doctor from the database
        Database db = Database.getInstance();
        assertNotNull(doctor, "Doctor not found!");
        assertEquals("Last Name", doctor.getLastName(), "Incorrect Last Name for retrieved doctor!");
    }
    
    @Test
    public void testRetrieveDoctorEmailFromDatabase() {
        // Test retrieving a doctor from the database
        Database db = Database.getInstance();
        assertNotNull(doctor, "Doctor not found!");
        assertEquals("abc@gmail.com", doctor.getEmail(), "Incorrect Email for retrieved doctor!");
    }
}

