import javax.imageio.ImageIO;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class NewBooking extends JFrame implements ActionListener {
    private BufferedImage backgroundImage;
    private JTextField nhsNoField;
    private JTextField reasonField;
    private JComboBox<Doctor> doctorBox;
    private JDateChooser dateField;
    private JSpinner timeField;
    private JLabel statusLabel;
    private String nhsNo;
    private String firstName;

    public NewBooking(String nhsNo, String firstName) {
        this.nhsNo = nhsNo;
        this.firstName = firstName;
        setTitle("New Booking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Get background image
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("BackgroundORIGINAL.png");
            backgroundImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Define components
        JLabel backgroundLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(backgroundLabel);
        getContentPane().setLayout(new GridLayout(12, 2));

        // Define font and color for labels and input fields
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Color whiteColor = Color.WHITE;

        // NHS number field
        JLabel nhsNoLabel = new JLabel("NHS number:");
        nhsNoLabel.setFont(labelFont);
        nhsNoLabel.setForeground(whiteColor);
        nhsNoField = new JTextField(nhsNo);
        nhsNoField.setFont(labelFont);
        nhsNoField.setEditable(false); // Make NHS number non-editable
        add(nhsNoLabel);
        add(nhsNoField);

        // Booking reason field
        JLabel reasonLabel = new JLabel("Booking Reason:");
        reasonLabel.setFont(labelFont);
        reasonLabel.setForeground(whiteColor);
        reasonField = new JTextField();
        reasonField.setFont(labelFont);
        add(reasonLabel);
        add(reasonField);

        // Doctor selection dropdown
        JLabel doctorLabel = new JLabel("Doctor:");
        doctorLabel.setFont(labelFont);
        doctorLabel.setForeground(whiteColor);
        doctorBox = new JComboBox<>();
        doctorBox.setFont(labelFont);
        add(doctorLabel);
        add(doctorBox);

        // Date selection field
        JLabel dateLabel = new JLabel("Booking Date:");
        dateLabel.setFont(labelFont);
        dateLabel.setForeground(whiteColor);
        dateField = new JDateChooser();
        dateField.setFont(labelFont);
        add(dateLabel);
        add(dateField);

        // Time selection field
        JLabel timeLabel = new JLabel("Booking Time:");
        timeLabel.setFont(labelFont);
        timeLabel.setForeground(whiteColor);
        timeField = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeField, "HH:mm:ss");
        timeField.setEditor(timeEditor);
        timeField.setFont(labelFont);
        add(timeLabel);
        add(timeField);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        submitButton.setFont(labelFont);
        add(submitButton);

        // Status label for displaying messages
        statusLabel = new JLabel("");
        statusLabel.setFont(labelFont);
        statusLabel.setForeground(Color.RED);
        add(statusLabel);

        // Back and submit buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ViewBookings(nhsNo, firstName);
            }
        });
        backButton.setFont(labelFont);
        buttonPanel.add(backButton);

        // Add button panel
        buttonPanel.add(submitButton);
        add(buttonPanel);

        // Fetch and populate doctors
        fetchDoctors();
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get values from input fields
        String bookingReason = reasonField.getText();
        Doctor selectedDoctor = (Doctor) doctorBox.getSelectedItem();
        int doctorID = selectedDoctor.getId();
        String bookingDate = "";
        if (dateField.getDate() != null) {
            bookingDate = new SimpleDateFormat("yyyy-MM-dd").format(dateField.getDate());
        }
        String bookingTime = ((JSpinner.DefaultEditor)timeField.getEditor()).getTextField().getText();

        // Check if all fields are filled
        if (bookingReason.isEmpty() || bookingDate.isEmpty() || bookingTime.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                    "MediCare", "my-secret-pw");

            // Prepare SQL statement to insert booking data
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO Booking (NHSno, DoctorID, BookingReason, BookingTime) VALUES (?,?,?,?)");
            statement.setString(1, nhsNo);
            statement.setInt(2, doctorID);
            statement.setString(3, bookingReason);
            statement.setString(4, bookingDate + " " + bookingTime);
            int rowsInserted = statement.executeUpdate();

            // Check if the booking is successfully created
            if (rowsInserted > 0) {
                statusLabel.setForeground(Color.GREEN);
                statusLabel.setText("Booking created successfully.");
            } else {
                statusLabel.setText("Failed to create booking. Please try again later.");
            }
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setText("Error: Failed to create booking: " + ex.getMessage());
        }
    }

    private void fetchDoctors() {
        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                    "MediCare", "my-secret-pw");

            // Prepare SQL statement to fetch doctors
            PreparedStatement statement = connection.prepareStatement("SELECT DoctorID, CONCAT(DoctorFirstName, ' ', DoctorSurname) AS DoctorName FROM Doctor");
            ResultSet resultSet = statement.executeQuery();

            // Populate the list of doctors in the dropdown box
            while (resultSet.next()) {
                int doctorID = resultSet.getInt("DoctorID");
                String doctorName = resultSet.getString("DoctorName");
                doctorBox.addItem(new Doctor(doctorID, doctorName));
            }

            // Close connections
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String nhsNo = ""; // Provide the NHS number here
        String firstName = ""; // Provide the first name here
        new NewBooking(nhsNo, firstName);
    }
}

class Doctor {
    private int id;
    private String name;

    public Doctor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
