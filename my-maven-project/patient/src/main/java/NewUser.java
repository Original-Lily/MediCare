import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

public class NewUser extends JFrame implements ActionListener {

    // Initialize input fields, button, and label
    JTextField nhsNoField;
    JTextField firstNameField;
    JTextField surnameField;
    JTextField emailField;
    JTextField phoneNoField;
    JTextField addressField;
    JPasswordField passwordField;
    JButton submitButton;
    JButton backButton; // Added back button
    JLabel statusLabel;

    // Initialize background image
    private BufferedImage backgroundImage;

    NewUser() {
        setTitle("New User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * Load background image
         */
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("BackgroundORIGINAL.png");
            backgroundImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        setContentPane(backgroundLabel);
        getContentPane().setLayout(new GridLayout(9, 2));

        /*
         * NHS number input field
         */
        JLabel nhsNoLabel = new JLabel("NHS Number:");
        nhsNoLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Make bold and 2 sizes larger
        nhsNoLabel.setForeground(Color.WHITE); // Set text color to white
        add(nhsNoLabel);
        nhsNoField = new JTextField();
        add(nhsNoField);

        /*
         * First name input field
         */
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Make bold and 2 sizes larger
        firstNameLabel.setForeground(Color.WHITE); // Set text color to white
        add(firstNameLabel);
        firstNameField = new JTextField();
        add(firstNameField);

        /*
         * Surname input field
         */
        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Make bold and 2 sizes larger
        surnameLabel.setForeground(Color.WHITE); // Set text color to white
        add(surnameLabel);
        surnameField = new JTextField();
        add(surnameField);

        /*
         * Email input field
         */
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Make bold and 2 sizes larger
        emailLabel.setForeground(Color.WHITE); // Set text color to white
        add(emailLabel);
        emailField = new JTextField();
        add(emailField);

        /*
         * Phone number input field
         */
        JLabel phoneNoLabel = new JLabel("Phone Number:");
        phoneNoLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Make bold and 2 sizes larger
        phoneNoLabel.setForeground(Color.WHITE); // Set text color to white
        add(phoneNoLabel);
        phoneNoField = new JTextField();
        add(phoneNoField);

        /*
         * Address input field
         */
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Make bold and 2 sizes larger
        addressLabel.setForeground(Color.WHITE); // Set text color to white
        add(addressLabel);
        addressField = new JTextField();
        add(addressField);

        /*
         * Password input field
         */
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Make bold and 2 sizes larger
        passwordLabel.setForeground(Color.WHITE); // Set text color to white
        add(passwordLabel);
        passwordField = new JPasswordField();
        add(passwordField);

        /*
         * Submit and back buttons
         */
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        add(submitButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton); // Add the back button

        /*
         * Status label for feedback
         */
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        add(statusLabel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String nhsNo = nhsNoField.getText();
            String firstName = firstNameField.getText();
            String surname = surnameField.getText();
            String email = emailField.getText();
            String phoneNo = phoneNoField.getText();
            String address = addressField.getText();
            String password = new String(passwordField.getPassword());

            // Check if any field is empty
            if (nhsNo.isEmpty() || firstName.isEmpty() || surname.isEmpty() || email.isEmpty() || phoneNo.isEmpty()
                    || address.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please fill in all fields.");
                return;
            }

            // Database connectivity
            try {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                        "MediCare", "my-secret-pw");
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO Patient (NHSno, PatientFirstName, PatientSurname, PatientEmail, PatientPhoneNo, Address, Password) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, nhsNo);
                statement.setString(2, firstName);
                statement.setString(3, surname);
                statement.setString(4, email);
                statement.setString(5, phoneNo);
                statement.setString(6, address);

                // Hash the password before storing in the database
                String hashedPassword = hashPassword(password);
                statement.setString(7, hashedPassword);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    statusLabel.setForeground(Color.GREEN);
                    statusLabel.setText("User added successfully.");

                    // Delay before redirection
                    delayBeforeRedirect();
                } else {
                    statusLabel.setText("Failed to add user.");
                }

                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                statusLabel.setText("Error: Failed to add user.");
            }
        } else if (e.getSource() == backButton) {
            // Handle back button action
            new Login();
            dispose();
        }
    }

    private void delayBeforeRedirect() {
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 2 seconds delay
                SwingUtilities.invokeLater(() -> {
                    new Login(); // Redirect to login page
                    dispose();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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
        SwingUtilities.invokeLater(NewUser::new);
    }
}
