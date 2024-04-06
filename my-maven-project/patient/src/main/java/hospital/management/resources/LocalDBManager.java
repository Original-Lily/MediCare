package hospital.management.resources;
//package hospital.management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LocalDBManager {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	public static void main(String[] args) {
		LocalDBManager db = new LocalDBManager();
		db.testConnection();
	}

	private void testConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/COMP5590?user=james&password=sqlpassword");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from patient");
			while (resultSet.next())
				System.out.println(resultSet.getString("NHSno") + " - " + resultSet.getString("PatientFirstName")
						+ " - " + resultSet.getString("PatientLastName"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
