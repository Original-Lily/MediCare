import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    @Test
    public void connectToDatabase(){
        // Test connecting to the database
        Database db = Database.getInstance();
        assertNotNull(db, "Database is not connected!");
    }

    @Test
    public void testDoctorCreated(){
        // Test doctor creation
        Database db = Database.getInstance();
        Doctor doctor = new Doctor("First Name", "Last Name", "abc@gmail.com");
        assertNotNull(db.saveDoctor(doctor));
    }

    @Test
    public void testAddressCreated(){
        // Test address creation
        Database db = Database.getInstance();
        Address address = new Address("12 Canterbury Street", "Test", "CT", "12345");
        assertNotNull(db.saveAddress(address));
    }

    @Test
    public void testPatientCreated(){
        // Test patient creation
        Database db = Database.getInstance();
        Patient patient = new Patient("First Name", "Last Name", 20, "Male");
        assertNotNull(db.savePatient(patient));
    }
}
