import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class BackgroundText extends JFrame {

    public BackgroundText() {
        setTitle("Doctor Details Page");
        setSize(1920, 1080);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a custom JPanel for background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw the background image
                try {
                    BufferedImage backgroundImage = ImageIO.read(new File("Background.png"));
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Set layout to null for manual positioning
        backgroundPanel.setLayout(null);

        // Add the background panel to the frame
        setContentPane(backgroundPanel);
        
        String Doctor = Database("SELECT ('Dr ' + DoctorFirstName + ' ' + DoctorSurname) AS DrName FROM Doctor");
        String Date = Database("SELECT DoctorSpecialities FROM Doctor");
        String Email = = Database("SELECT DoctorEmail FROM Doctor");

        // Create a JLabel with text
        JLabel textLabel = new JLabel(Doctor);
        textLabel.setFont(new Font("Arial", Font.BOLD, 48));
        textLabel.setForeground(Color.WHITE); // Set text color
        textLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        textLabel.setBounds(200, 10, 200, 50); // Initial position and size
        backgroundPanel.add(textLabel);// Add the label to the background panel
        // Move the label to the new position
        int newX = 500; // New x-coordinate
        int newY = 70; // New y-coordinate
        textLabel.setLocation(newX, newY);
        
        
        JLabel datetextLabel = new JLabel(Date);
        datetextLabel.setFont(new Font("Arial", Font.BOLD, 48));
        datetextLabel.setForeground(Color.WHITE); // Set text color
        datetextLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        datetextLabel.setBounds(200, 10, 200, 50); // Initial position and size
        backgroundPanel.add(datetextLabel);// Add the label to the background panel
        // Move the label to the new position
        int datenewX = 800; // New x-coordinate
        int datenewY = 230; // New y-coordinate
        datetextLabel.setLocation(datenewX, datenewY);
        
        JLabel EmailtextLabel = new JLabel(Email);
        EmailtextLabel.setFont(new Font("Arial", Font.BOLD, 48));
        EmailtextLabel.setForeground(Color.WHITE); // Set text color
        EmailtextLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        EmailtextLabel.setBounds(200, 10, 200, 50); // Initial position and size
        backgroundPanel.add(EmailtextLabel);// Add the label to the background panel
        // Move the label to the new position
        int EmailnewX = 800; // New x-coordinate
        int EmailnewY = 505; // New y-coordinate
        EmailtextLabel.setLocation(EmailnewX, EmailnewY);
        
        setVisible(true);
    }
    
    private String Database(String query){
        
        try (Connection dbConnection = DriverManager.getConnection("jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management", "MediCare", "my-secret-pw");
             PreparedStatement dbStatement = dbConnection.prepareStatement(query);
             ResultSet resultSet = dbStatement.executeQuery()) {
             if (resultSet.next()) {
                return resultSet.getString(1);
             } 
        }

        return ""; // Return an empty string if data wasn't retrived from the database
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BackgroundText());
    }
}