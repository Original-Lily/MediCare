import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class ViewDoctors extends JFrame {
    private JTable doctorsTable;
    private JScrollPane scrollPane;
    private JButton backButton;
    private JComboBox<String> surgeryComboBox;
    private JTextField searchField;
    private String nhsNo;
    private String firstName;

    public ViewDoctors(String nhsNo, String firstName) {
        this.nhsNo = nhsNo;
        this.firstName = firstName;
        setTitle("Doctor Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create table model and add columns
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Doctor ID");
        model.addColumn("First Name");
        model.addColumn("Surname");
        model.addColumn("Email");
        model.addColumn("Surgery");
        model.addColumn("Specialties");

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                    "MediCare", "my-secret-pw");

            // Prepare SQL statement
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Doctor");
            ResultSet resultSet = statement.executeQuery();

            // Add rows to the table model
            while (resultSet.next()) {
                int doctorID = resultSet.getInt("DoctorID");
                String DfirstName = resultSet.getString("DoctorFirstName");
                String surname = resultSet.getString("DoctorSurname");
                String email = resultSet.getString("DoctorEmail");
                int surgeryID = resultSet.getInt("SurgeryID");
                String specialties = resultSet.getString("DoctorSpecialties");

                // Get surgery name based on surgery ID
                String surgery = getSurgeryName(connection, surgeryID);

                model.addRow(new Object[]{doctorID, DfirstName, surname, email, surgery, specialties});
            }

            // Close connections
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Create table and scroll pane
        doctorsTable = new JTable(model);
        scrollPane = new JScrollPane(doctorsTable);
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

            // Add title label
            JLabel titleLabel = new JLabel("Doctor Information");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font to Arial Bold, size 18
            titleLabel.setForeground(Color.WHITE); // Set text color to white
            titleLabel.setBounds(20, 30, 200, 30);
            getContentPane().add(titleLabel);

            // Add scroll pane for the table
            int tableWidth = (int) (backgroundImage.getWidth() * 0.8); // 80% of frame width
            int tableHeight = 300;
            scrollPane.setBounds((backgroundImage.getWidth() - tableWidth) / 2, 150, tableWidth, tableHeight);
            getContentPane().add(scrollPane);

            // Add surgery combo box
            JLabel surgeryLabel = new JLabel("Filter by Surgery:");
            surgeryLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font to Arial Bold, size 16
            surgeryLabel.setForeground(Color.WHITE); // Set text color to white
            surgeryLabel.setBounds(50, 100, 150, 30);
            getContentPane().add(surgeryLabel);
            surgeryComboBox = new JComboBox<>();
            surgeryComboBox.addItem("All");
            populateSurgeryComboBox();
            surgeryComboBox.setBounds(200, 100, 150, 30);
            surgeryComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filterDoctors();
                }
            });
            getContentPane().add(surgeryComboBox);

            // Add search field
            JLabel searchLabel = new JLabel("Search:");
            searchLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font to Arial Bold, size 16
            searchLabel.setForeground(Color.WHITE); // Set text color to white
            searchLabel.setBounds(400, 100, 100, 30);
            getContentPane().add(searchLabel);
            searchField = new JTextField();
            searchField.setBounds(500, 100, 200, 30);
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filterDoctors();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterDoctors();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterDoctors();
                }
            });
            getContentPane().add(searchField);

            // Add back button
            backButton = new JButton("Back");
            backButton.setBounds(50, 500, 100, 30);
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new HomePage(nhsNo, firstName);
                }
            });
            getContentPane().add(backButton);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set frame size and visibility
        setSize(backgroundImage.getWidth(), backgroundImage.getHeight() + 50); // Increase height by 50 pixels
        setVisible(true);
    }

    private String getSurgeryName(Connection connection, int surgeryID) throws SQLException {
        String surgeryName = "";
        PreparedStatement surgeryStatement = connection.prepareStatement("SELECT SurgeryName FROM Surgery WHERE SurgeryID = ?");
        surgeryStatement.setInt(1, surgeryID);
        ResultSet surgeryResult = surgeryStatement.executeQuery();
        if (surgeryResult.next()) {
            surgeryName = surgeryResult.getString("SurgeryName");
        }
        surgeryResult.close();
        surgeryStatement.close();
        return surgeryName;
    }

    private void populateSurgeryComboBox() {
        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management",
                    "MediCare", "my-secret-pw");

            // Prepare SQL statement
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Surgery");
            ResultSet resultSet = statement.executeQuery();

            // Add surgeries to the combo box
            while (resultSet.next()) {
                String surgeryName = resultSet.getString("SurgeryName");
                surgeryComboBox.addItem(surgeryName);
            }

            // Close connections
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void filterDoctors() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) doctorsTable.getModel());
        doctorsTable.setRowSorter(sorter);

        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();
        String surgery = (String) surgeryComboBox.getSelectedItem();
        String search = searchField.getText();

        if (!surgery.equals("All")) {
            filters.add(RowFilter.regexFilter("(?i)" + surgery, 4));
        }
        if (!search.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + search));
        }

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    public static void main(String[] args) {
        String nhsNo = ""; // Replace with the actual value
        String firstName = ""; // Replace with the actual value
        SwingUtilities.invokeLater(() -> new ViewDoctors(nhsNo, firstName)); // Placeholder for NHS number
    }
}
