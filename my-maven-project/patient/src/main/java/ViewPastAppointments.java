import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class ViewPastAppointments extends JFrame {
    private JTable appointmentsTable;
    private JScrollPane scrollPane;
    private JTextArea medicalHistoryTextArea;
    private JScrollPane historyScrollPane;
    private JButton backButton;

    public ViewPastAppointments(String nhsNo, String firstName) {
        setTitle("Past Appointments");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table model and add columns
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Booking ID");
        model.addColumn("Doctor Surname");
        model.addColumn("Doctor Email");
        model.addColumn("Reason");
        model.addColumn("Booking Time");

        // Create medical history text area
        medicalHistoryTextArea = new JTextArea();
        medicalHistoryTextArea.setEditable(false);

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                    "MediCare", "my-secret-pw");

            // Prepare SQL statement for fetching past appointments
            PreparedStatement appointmentStatement = connection.prepareStatement(
                    "SELECT Booking.BookingID, Doctor.DoctorSurname, Doctor.DoctorEmail, Booking.BookingReason, Booking.BookingTime " +
                            "FROM Booking " +
                            "JOIN Doctor ON Booking.DoctorID = Doctor.DoctorID " +
                            "WHERE Booking.NHSno = ?");
            appointmentStatement.setString(1, nhsNo);
            ResultSet appointmentResult = appointmentStatement.executeQuery();

            // Add rows to the table model
            while (appointmentResult.next()) {
                int bookingID = appointmentResult.getInt("BookingID");
                String doctorSurname = appointmentResult.getString("DoctorSurname");
                String doctorEmail = appointmentResult.getString("DoctorEmail");
                String reason = appointmentResult.getString("BookingReason");
                Timestamp bookingTime = appointmentResult.getTimestamp("BookingTime");

                model.addRow(new Object[]{bookingID, doctorSurname, doctorEmail, reason, bookingTime});
            }

            // Close appointments result set and statement
            appointmentResult.close();
            appointmentStatement.close();

            // Prepare SQL statement for fetching medical history
            PreparedStatement historyStatement = connection.prepareStatement("SELECT History FROM Patient WHERE NHSno = ?");
            historyStatement.setString(1, nhsNo);
            ResultSet historyResult = historyStatement.executeQuery();

            // Display medical history
            if (historyResult.next()) {
                String medicalHistory = historyResult.getString("History");
                medicalHistoryTextArea.setText(medicalHistory);
            }

            // Close history result set and statement
            historyResult.close();
            historyStatement.close();

            // Close connection
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Create table and scroll pane
        appointmentsTable = new JTable(model);
        scrollPane = new JScrollPane(appointmentsTable);

        // Create scroll pane for medical history text area
        historyScrollPane = new JScrollPane(medicalHistoryTextArea);

        BufferedImage backgroundImage = null;

        // Load background image
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("BackgroundWIDE.png");
            backgroundImage = ImageIO.read(inputStream);

            // Set background image as content pane
            JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
            backgroundLabel.setBounds(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
            setContentPane(backgroundLabel);
            getContentPane().setLayout(null);

            // Add title label for appointments
            JLabel titleLabel = new JLabel("Your Appointments:");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setBounds(50, 50, 200, 30);
            getContentPane().add(titleLabel);

            // Add scroll pane for the table
            scrollPane.setBounds(50, 100, 800, 300); // Adjust size as needed
            getContentPane().add(scrollPane);

            // Add scroll pane for medical history text area
            historyScrollPane.setBounds(870, 100, 250, 300); // Adjust position and size as needed
            getContentPane().add(historyScrollPane);

            // Add back button
            backButton = new JButton("Back");
            backButton.setBounds(50, 450, 100, 30);
            backButton.addActionListener(e -> {
                dispose();
                new HomePage(nhsNo, firstName);
            });
            getContentPane().add(backButton);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set frame size and visibility
        setSize(backgroundImage.getWidth(), backgroundImage.getHeight());
        setVisible(true);
    }

    public static void main(String[] args) {
        String nhsNo = ""; // Replace with the actual value
        String firstName = ""; // Replace with the actual value
        SwingUtilities.invokeLater(() -> new ViewPastAppointments(nhsNo, firstName));
    }
}
