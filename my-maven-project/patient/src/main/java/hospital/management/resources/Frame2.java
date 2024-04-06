package hospital.management.resources;
import javax.swing.*;
public class Frame2 extends JFrame{
    JFrame f;
    JLabel L;
    static String label;
    Frame2(){
        L = new JLabel(label); //The text in the label is the same as the field label
        L.setBounds(50, 150, 200, 30);

        add(L);
        setSize(400, 400);
        setLayout(null);
        setVisible(true);
    }
}
