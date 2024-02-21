import javax.swing.*;

public class Main {
        static JFrame ventana = new JFrame("Gestion de Proyectos");

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Clase_modelo ventana=new Clase_modelo();
            ventana.iniciar();
        });
    }
}