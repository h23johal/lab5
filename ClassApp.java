import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClassApp {
    private JPanel panel1;
    private JTextField userName;
    private JTextField email;
    private JTextField firstName;
    private JTextField termin;
    private JTextField lastName;
    private JTextArea outPut;
    private JButton save;
    private JButton showAll;


    public ClassApp() {
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        showAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outPut.setText(firstName.getText());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClassApp");
        frame.setContentPane(new ClassApp().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}