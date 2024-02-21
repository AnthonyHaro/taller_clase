import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Actualizar {
    public JPanel ActD;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton ACTUALIZARButton;
    private JButton REGRESARButton;

    public Actualizar() {
        ACTUALIZARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frames = (JFrame) SwingUtilities.getWindowAncestor(REGRESARButton);
                frames.dispose();
                JFrame frame = new JFrame("MENU");
                frame.setContentPane(new Clase_modelo().Jpanel1);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(580, 480);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
