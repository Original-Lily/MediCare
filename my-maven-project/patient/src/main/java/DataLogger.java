import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;

public class DataLogger {
    private String file;

    // constructor
    public DataLogger() {
        this.file = "logs.txt";

    }

    /*
     * Method for logging accounts signing in
     * writes a message that the printToLogs method will save
     * 
     * @param: account the account that signs in
     */
    public void logSignIn(String account) {
        String message = "Account '" + account + "' Signed in.";
        printToLogs(message);
    }

    /*
     * Method for logging patients changing doctor
     * writes a message that the printToLogs method will save
     * 
     * @param: account the account that requested a change
     */
    public void logDocChange(String account) {
        String message = "Account '" + account + "' has requested a new doctor.";
        printToLogs(message);
    }

    /*
     * Method for logging patients signing up
     * writes a message that the printToLogs method will save
     * 
     * @param: account the account that signed up
     */
    public void logRegister(String account) {
        String message = "Account '" + account + "' has registered as a patient.";
        printToLogs(message);
    }

    /*
     * Method for logging patients booking an appointment
     * writes a message that the printToLogs method will save
     * 
     * @param: account the account that created the booking
     */
    public void logBooking(String account, String time) {
        String message = "Account '" + account + "' has created a booking for " + time + ".";
        printToLogs(message);
    }

    /*
     * Method for logging patients rescheduling bookings
     * writes a message that the printToLogs method will save
     * 
     * @param: account the account that resceduled the booking
     */
    public void logRescheduledBooking(String account, String time) {
        String message = "Account '" + account + "' has rescheduled a booking for " + time + ".";
        printToLogs(message);
    }

    /*
     * Method for saving messages
     * formats the message and saves it to the file
     * 
     * @param message: the message to be saved
     */
    private void printToLogs(String message) {
        // get and format the current time
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
        String time = ldt.format(dtf);
        // format output
        String output = "[" + time + "]: " + message;
        try {
            FileWriter fWriter = new FileWriter(file);
            fWriter.write(output + "\n");
            fWriter.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}