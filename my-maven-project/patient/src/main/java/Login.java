import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Login extends JFrame implements ActionListener { // Inherits JFrame so there is no need to create an
                                                              // instance of JFrame explicitly
    /*
    * Initialise input fields:
    * 
    * JTextField "nhsNoField" as NHS number input
    * JPasswordField "passwordField" as password input
    * JButton "clickMeButton" as a button to login and check inputs
    * JButton "newUserButton" as a button to enter the new user screen
    * 
    */
    JTextField nhsNoField;
    JPasswordField passwordField;
    JButton ExistingUsr;
    JButton newUserButton;
    JLabel errorLabel;

    // Initialize background image
    private BufferedImage backgroundImage;

    Login() {
        // For debugging purposes, as we are reading a file, catch any errors to be
        // displayed in the console should they occur
        // Load background image
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("BackgroundLOGIN.png");
            backgroundImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        setContentPane(backgroundLabel);
        getContentPane().setLayout(null);

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * set up NHS number input field
         */
        JLabel nhsNoLabel = new JLabel("NHS no.");
        nhsNoLabel.setBounds(50, 50, 100, 30);
        nhsNoLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Make bold and 1 size larger
        nhsNoLabel.setForeground(Color.WHITE); // Set text color to white
        add(nhsNoLabel);
        nhsNoField = new JTextField();
        nhsNoField.setBounds(150, 50, 200, 30);
        add(nhsNoField);

        /*
         * set up password input field
         * Note: By utilizing "passwordLabel" this makes the input field hidden for
         * further privacy
         */
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(50, 100, 100, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Make bold and 1 size larger
        passwordLabel.setForeground(Color.WHITE); // Set text color to white
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 30);
        add(passwordField);

        /*
         * set up login button
         */
        ExistingUsr = new JButton("Login");
        ExistingUsr.setBounds(50, 150, 120, 40);
        ExistingUsr.addActionListener(this);
        add(ExistingUsr);

        /*
         * set up new user button
         */
        newUserButton = new JButton("New user");
        newUserButton.setBounds(200, 150, 120, 40);
        newUserButton.addActionListener(this);
        add(newUserButton);

        /*
         * error label for incorrect login attempt
         */
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(50, 200, 300, 30);
        add(errorLabel);

        setSize(backgroundImage.getWidth(), backgroundImage.getHeight()); // Set size of login frame to the size of the
        setLayout(null);                                          // background image, use getter methods for readability
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ExistingUsr) {

            /*
             * Login button clicked:
             * For debugging purposes, display the entered NHS no. and password in console
             * 
             * @JA758 should implement logging system access features her
             * Open the next frame
             */

            String nhsNo = nhsNoField.getText();
            String password = new String(passwordField.getPassword());
            String hashedPassword = hashPassword(password);

            try {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                        "MediCare", "my-secret-pw");
                PreparedStatement statement = connection
                        .prepareStatement("SELECT PatientFirstName FROM Patient WHERE NHSno = ? AND Password = ?");
                statement.setString(1, nhsNo);
                statement.setString(2, hashedPassword);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String firstName = resultSet.getString("PatientFirstName");
                    errorLabel.setText("");
                    HomePage homepage = new HomePage(nhsNo, firstName); // Pass both NHS number and patient's first name
                    homepage.setLabel("Login Successful! Welcome, " + firstName); // Set label directly
                    dispose();
                } else {
                    errorLabel.setText("Invalid NHS number or password. Please try again.");
                }                

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == newUserButton) {
            /*
             * New user button clicked:
             * For debugging purposes, display the entered NHS no. and password in console
             */
            //System.out.println(NHSno, firstName);
            new NewUser();
            dispose();
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
