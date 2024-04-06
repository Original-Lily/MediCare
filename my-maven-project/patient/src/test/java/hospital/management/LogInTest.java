import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LogInTest {

    @Test
    public void connectToDatabase(){
        // Test connecting to the database
        Database db = Database.getInstance();
        assertNotNull(db, "Database is not connected!");
    }   
    
    @Test
    public void testValidLogin(){
        Database db = Database.getInstance();
        Login testLogin = new Login();
        
        testLogin.nhsNoField.setText("999000999");
        testLogin.passwordField.setText("pass123");
        testLogin.ExistingUsr.doClick();
        assertNotNull("Valid NHS Number or password!");
        assertEquals("999000999", "That is correct");
    }
    
    @Test
    public void testInValidLogin(){
        Database db = Database.getInstance();
        Login testLogin = new Login();
        
        testLogin.nhsNoField.setText("123456789");
        testLogin.passwordField.setText("pass");
        testLogin.ExistingUsr.doClick();
        assertNotNull("Invalide NHS Number or password! Please try again");
    }
}

