import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Actualizar {
    public JPanel ActD;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCedula;
    private JTextField diretxt;
    private JTextField edadtext;
    private JButton ACTUALIZARButton;
    private JButton REGRESARButton;
    private JTextField teletxt;

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
