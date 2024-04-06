import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewBookings extends JFrame {
    private JTable bookingsTable;
    private JScrollPane scrollPane;
    private JButton backButton;
    private JButton newBookingButton;
    private String nhsNo;
    private String firstName;
    private JComboBox<String> doctorComboBox;

    public ViewBookings(String nhsNo, String firstName) {
        this.nhsNo = nhsNo;
        this.firstName = firstName;
        setTitle("Booking Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table model and add columns
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Booking ID");
        model.addColumn("Booking Reason");
        model.addColumn("Booking Time");
        model.addColumn("Doctor");

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                    "MediCare", "my-secret-pw");

            // Prepare SQL statement
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT BookingID, BookingReason, BookingTime, DoctorID FROM Booking WHERE NHSno = ?");
            statement.setString(1, nhsNo);
            ResultSet resultSet = statement.executeQuery();

            // Add rows to the table model
            while (resultSet.next()) {
                int bookingID = resultSet.getInt("BookingID");
                String bookingReason = resultSet.getString("BookingReason");
                String bookingTime = resultSet.getString("BookingTime");
                String doctorName = getDoctorName(connection, resultSet.getInt("DoctorID"));
                model.addRow(new Object[] { bookingID, bookingReason, bookingTime, doctorName });
            }

            // Close connections
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Create table and scroll pane
        bookingsTable = new JTable(model);
        bookingsTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Adjust column width
        scrollPane = new JScrollPane(bookingsTable);
        BufferedImage backgroundImage = null;

        // Load background image
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("BackgroundORIGINAL.png");
            backgroundImage = ImageIO.read(inputStream);

            // Set background image as content pane
            JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
            backgroundLabel.setBounds(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
            setContentPane(backgroundLabel);
            getContentPane().setLayout(null);

            // Add title label
            JLabel titleLabel = new JLabel("Your Bookings:");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font to Arial Bold, size 18
            titleLabel.setForeground(Color.WHITE); // Set text color to white
            titleLabel.setBounds(20, 20, 200, 30);
            getContentPane().add(titleLabel);

            // Add scroll pane for the table
            scrollPane.setBounds(50, 70, 500, 200); // Adjust scroll pane size
            getContentPane().add(scrollPane);

            // Add back button
            backButton = new JButton("Back");
            backButton.setBounds(50, 290, 100, 30);
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new HomePage(nhsNo, firstName);
                }
            });
            getContentPane().add(backButton);

            // Add new booking button
            newBookingButton = new JButton("New Booking");
            newBookingButton.setBounds(160, 290, 150, 30);
            newBookingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new NewBooking(nhsNo, firstName);
                    dispose();
                }
            });
            getContentPane().add(newBookingButton);

            // Add edit button
            JButton editButton = new JButton("Edit");
            editButton.setBounds(320, 290, 100, 30);
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int bookingID = (int) bookingsTable.getValueAt(selectedRow, 0);
                        String bookingReason = (String) bookingsTable.getValueAt(selectedRow, 1);
                        String bookingTime = (String) bookingsTable.getValueAt(selectedRow, 2);
                        String doctorName = (String) bookingsTable.getValueAt(selectedRow, 3);
                        showEditDialog(bookingID, bookingReason, bookingTime, doctorName);
                    } else {
                        JOptionPane.showMessageDialog(ViewBookings.this, "Please select a booking to edit.");
                    }
                }
            });
            getContentPane().add(editButton);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set frame size and visibility
        setSize(backgroundImage.getWidth(), backgroundImage.getHeight());
        setVisible(true);
    }

    private String getDoctorName(Connection connection, int doctorID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT CONCAT(DoctorFirstName, ' ', DoctorSurname) AS DoctorName FROM Doctor WHERE DoctorID = ?");
        statement.setInt(1, doctorID);
        ResultSet resultSet = statement.executeQuery();
        String doctorName = "";
        if (resultSet.next()) {
            doctorName = resultSet.getString("DoctorName");
        }
        resultSet.close();
        statement.close();
        return doctorName;
    }

    private void showEditDialog(int bookingID, String bookingReason, String bookingTime, String doctorName) {
        // Prompt confirmation dialog
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you wish to edit this Booking?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Load doctors from the database
            String[] doctors = loadDoctorsFromDatabase();

            // Create a combo box for doctors
            doctorComboBox = new JComboBox<>(doctors);
            doctorComboBox.setSelectedItem(doctorName);

            // Create text fields for reason and time
            JTextField reasonField = new JTextField(bookingReason);
            JTextField timeField = new JTextField(bookingTime);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Booking Reason:"));
            panel.add(reasonField);
            panel.add(new JLabel("Booking Time:"));
            panel.add(timeField);
            panel.add(new JLabel("Doctor:"));
            panel.add(doctorComboBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Edit Booking",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                // Update database with edited details
                String newBookingReason = reasonField.getText();
                String newBookingTime = timeField.getText();
                String selectedDoctorName = (String) doctorComboBox.getSelectedItem();
                updateBooking(bookingID, newBookingReason, newBookingTime, selectedDoctorName);
            }
        }
    }

    private String[] loadDoctorsFromDatabase() {
        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                    "MediCare", "my-secret-pw");

            // Prepare SQL statement
            PreparedStatement statement = connection
                    .prepareStatement("SELECT CONCAT(DoctorFirstName, ' ', DoctorSurname) AS DoctorName FROM Doctor");
            ResultSet resultSet = statement.executeQuery();

            // Create a dynamic list to store doctor names
            List<String> doctorsList = new ArrayList<>();

            // Populate the list with doctor names
            while (resultSet.next()) {
                doctorsList.add(resultSet.getString("DoctorName"));
            }

            // Convert the list to an array
            String[] doctors = doctorsList.toArray(new String[0]);

            // Close connections
            resultSet.close();
            statement.close();
            connection.close();

            return doctors;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new String[] {};
        }
    }

    private void updateBooking(int bookingID, String newBookingReason, String newBookingTime,
            String selectedDoctorName) {
        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                    "MediCare", "my-secret-pw");

            // Prepare SQL statement to update Booking table
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Booking SET BookingReason = ?, BookingTime = ?, DoctorID = (SELECT DoctorID FROM Doctor WHERE CONCAT(DoctorFirstName, ' ', DoctorSurname) = ?) WHERE BookingID = ?");
            statement.setString(1, newBookingReason);
            statement.setString(2, newBookingTime);
            statement.setString(3, selectedDoctorName);
            statement.setInt(4, bookingID);
            int rowsUpdated = statement.executeUpdate();

            // Show appropriate message based on update result
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Booking updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update booking.");
            }

            // Close connections
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            String nhsNo = args[0];
            String firstName = ""; // Replace this with the actual value of firstName
            new ViewBookings(nhsNo, firstName);
        } else {
            System.out.println("NHS number not provided.");
        }
    }
}
