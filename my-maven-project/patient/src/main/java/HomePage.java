import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class HomePage extends JFrame implements ActionListener {
    JLabel welcomeLabel;
    private BufferedImage backgroundImage;

    static String label;
    String nhsNo;
    String firstName;

    HomePage(String nhsNo, String firstName) {
        this.nhsNo = nhsNo; // Assign the NHS number passed from Login
        this.firstName = firstName; // Assign the first name passed from Login
        setTitle("Home Page"); // Set title to "Home Page"
        // For debugging purposes, as we are reading a file, catch any errors to be displayed in the console should they occur
        // Load background image
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("BackgroundORIGINAL.png");
            backgroundImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
        setContentPane(backgroundLabel);
        getContentPane().setLayout(null);

        /*
         * welcome message label
         */
        if (label == null || label.isEmpty()) { // Check if label is null or empty
            label = "Welcome"; // Set default welcome message
        }
        welcomeLabel = new JLabel(label);
        welcomeLabel.setBounds(50, 50, 400, 30);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Make bold and size 16 Arial
        welcomeLabel.setForeground(Color.WHITE);
        add(welcomeLabel);

        /*
         * set up buttons
         */
        JButton bookingButton = new JButton("Booking Information");
        bookingButton.setBounds(50, 100, 200, 30);
        bookingButton.addActionListener(this);
        add(bookingButton);

        JButton doctorButton = new JButton("Doctor Information");
        doctorButton.setBounds(50, 150, 200, 30);
        doctorButton.addActionListener(this);
        add(doctorButton);

        JButton medicalButton = new JButton("Medical History & Prescriptions");
        medicalButton.setBounds(50, 200, 300, 30);
        medicalButton.addActionListener(this);
        add(medicalButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(450, 400, 100, 30);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login(); // Take the user back to the login page
            }
        });
        add(logoutButton);

        setSize(backgroundImage.getWidth(), backgroundImage.getHeight()); // Set size of login frame to the size of the
        setLayout(null);                                          // background image, use getter methods for readability
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Booking Information")) {
            new ViewBookings(nhsNo, firstName);
            dispose();
        } else if (e.getActionCommand().equals("Doctor Information")) {
            new ViewDoctors(nhsNo, firstName);
            dispose();
        } else if (e.getActionCommand().equals("Medical History & Prescriptions")) {
            new ViewPastAppointments(nhsNo, firstName);
            dispose();
        }
    }

    public static void main(String[] args) {
        new HomePage("nhsNo", "firstName"); // Calls the Frame method / object with required arguments
    }

    public void setLabel(String label) {
        welcomeLabel.setText(label);
    }
}
