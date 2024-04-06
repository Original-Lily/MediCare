package hospital.management.resources;
import java.sql.*;

public class DatabaseTest {
    private static final String URL = "jdbc:mysql://ec2-16-171-174-44.eu-north-1.compute.amazonaws.com:3306/hospital_management";
    private static final String USER = "MediCare";
    private static final String PASSWORD = "my-secret-pw";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT NHSno, PatientFirstName, PatientSurname, PatientEmail FROM Patient;");
            while (resultSet.next()) {
                int nhsNo = resultSet.getInt("NHSno");
                String firstName = resultSet.getString("PatientFirstName");
                String surname = resultSet.getString("PatientSurname");
                String email = resultSet.getString("PatientEmail");
                System.out.println("NHS Number: " + nhsNo + ", Name: " + firstName + " " + surname + ", Email: " + email);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
