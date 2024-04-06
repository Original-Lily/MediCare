package hospital.management.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*; //Useful GUI package for later
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Frame1 extends JFrame implements ActionListener { //Inherits JFrame so there is no need to create an instance of JFrame explicitly

    /*
     * Initialise input fields:
     * 
     * JTextField "nhsNoField" as NHS number input
     * JPasswordField "passwordField" as password input
     * JButton "clickMeButton" as a button to login and check inputs
     * JButton "newUserButton" as a button to enter the new user screen
     * 
     */

    JTextField nhsNoField; 
    JPasswordField passwordField; 
    JButton ExistingUsr; 
    JButton newUserButton;

    //Initialise background image
    private BufferedImage backgroundImage;

    Frame1(){

        //For debugging purposes, as we are reading a file, catch any errors to be displayed in the console should they occur

        //C:\Users\buddi\OneDrive\Desktop\Computer Science Revision\Year 2\Software Development\GroupProj\software-development-assignment-1\my-maven-project\patient\src\main\resources

        try {
            //Load selected background Image
            backgroundImage = ImageIO.read(new File("../../.." + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "BackgroundLOGIN.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setBounds(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
        setContentPane(backgroundLabel);
        getContentPane().setLayout(null);

        /*
         * set up NHS number input field
         */
        JLabel nhsNoLabel = new JLabel("NHS no."); 
        nhsNoLabel.setBounds(50, 50, 100, 30);
        add(nhsNoLabel);
        nhsNoField = new JTextField();
        nhsNoField.setBounds(150, 50, 200, 30);
        add(nhsNoField);

        /*
         * set up password input field  
         * Note: By utilizing "passwordLabel" this makes the input field hidden for further privacy 
        */
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(50, 100, 100, 30);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 30);
        add(passwordField);

        /*
         * set up login button
         */
        ExistingUsr = new JButton("Login");
        ExistingUsr.setBounds(50, 150, 120, 40);
        ExistingUsr.addActionListener(this);
        add(ExistingUsr);

        /*
         * set up new user button
         */
        newUserButton = new JButton("New user");
        newUserButton.setBounds(200, 150, 120, 40);
        newUserButton.addActionListener(this);
        add(newUserButton);

        setSize(backgroundImage.getWidth(), backgroundImage.getHeight()); //Set size of login frame to the size of the background image, use getter methods for readability
        setLayout(null); //Sets the layout of the frame (It's currently set to null)
        setVisible(true); //Sets the frame to be visible
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nhsNo = nhsNoField.getText();
        String password = new String(passwordField.getPassword());
        if (e.getSource() == ExistingUsr) {

            /*
             * Login button clicked:
             * For debugging purposes, display the entered NHS no. and password in console
             * @JA758 should implement logging system access features her
             * Open the next frame
             */

            System.out.println("Existing User:");
            System.out.println("NHS no.: " + nhsNo);
            System.out.println("Password: " + password);
            
            Frame2.label = nhsNo; //Sets the label field in Frame2 class to the variable s1
            new Frame2(); //Creates an instance of the Frame2 class

        } else if (e.getSource() == newUserButton) {

            /*
             * New user button clicked:
             * For debugging purposes, display the entered NHS no. and password in console
             * @GEHP should include new frame for new user registration here
             */

            System.out.println("New User:");
            System.out.println("NHS no.: " + nhsNo);
            System.out.println("Password: " + password);
            
        }
    }

    public static void main(String[] args) {
        new Frame1(); //Calls the Frame method / object
    }
}