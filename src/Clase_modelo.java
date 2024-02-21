import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class Clase_modelo extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtId;
    private JButton btnMostrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    public JPanel Jpanel1;
    private JButton subir_fotoButton;
    private JButton buscarImagenButton;
    private JLabel JLabel_Imagen;
    private JTextField txtCedula;
    private JTextField diretxt;
    private JTextField teletxt;
    private JTextField edadtext;

    private static final String URL = "jdbc:mysql://uwbtoxzn5u0iisji:IYihO7vjhCbhmAcYPN5I@bvditkfe61woksb136yw-mysql.services.clever-cloud.com:3306/bvditkfe61woksb136yw";
    private static final String USUARIO = "uwbtoxzn5u0iisji";
    private static final String CONTRASENA = "IYihO7vjhCbhmAcYPN5I";

    public Clase_modelo() {
        super("CRUD de Usuarios");
        setContentPane(Jpanel1);

        subir_fotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        // Leer la imagen como un arreglo de bytes
                        FileInputStream fis = new FileInputStream(selectedFile);
                        byte[] imageData = fis.readAllBytes();
                        fis.close();
                        ImageIcon imageIcon = new ImageIcon(imageData);
                        // Mostrar la imagen en un JLabel o en otro componente
                        JLabel labelImagen = new JLabel(imageIcon);
                        int option = JOptionPane.showConfirmDialog(null,labelImagen ,"¿Desea guardar esta imagen?",  JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            // Guardar la imagen en la base de datos
                            guardarImagen(imageData);
                            JOptionPane.showMessageDialog(null, "Su datos y la Imagen  se Guardò correctamente en la base de datos" );
                            txtCedula.setText(" ");
                            txtNombre.setText(" ");
                            txtApellido.setText(" ");
                            diretxt.setText(" ");
                            teletxt.setText(" ");
                            edadtext.setText(" ");
                        } else {
                            JOptionPane.showMessageDialog(null, "Imagen no guardada");
                        }
                    } catch (IOException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al subir la imagen: " + ex.getMessage());
                    }
                }
            }
        });



        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInformacion_tabla();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frames = (JFrame) SwingUtilities.getWindowAncestor(btnActualizar);
                frames.dispose();
                JFrame frame = new JFrame("Informacion Personas");
                frame.setContentPane(new Actualizar().ActD);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarRegistro();
            }
        });
        buscarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener el ID del usuario desde algún componente de tu interfaz de usuario, como un campo de texto
                int idUsuario = Integer.parseInt(txtId.getText());

                // Llamar al método mostrarImagenPorId con el ID del usuario
                mostrarImagenPorId();
            }
        });
    }

    public void iniciar() {
        setLocationRelativeTo(null);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Connection establecerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    private void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



   private void mostrarInformacion_tabla() {
        try (Connection conexion = establecerConexion();
             PreparedStatement statement = conexion.prepareStatement("SELECT cedula, nombre, apellido, imagen , Dirección , Telefono , Edad ,fecha FROM usuarios");
             ResultSet resultSet = statement.executeQuery()) {

            // Crear el modelo de tabla con los nombres de las columnas
            DefaultTableModel tableModel = new DefaultTableModel(
                    new String[]{"Cedula", "Nombre", "Apellido", "Imagen", "Dirección", "Telefono", "Edad", "fecha"}, 0);

            while (resultSet.next()) {
                int id = resultSet.getInt("cedula");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                byte[] imageData = resultSet.getBytes("imagen");
                String Direccion = resultSet.getString("Dirección");
                int telefono= resultSet.getInt("Telefono");
                // Verificar si imageData es nulo
                ImageIcon imageIcon = null;
                if (imageData != null) {
                    // Crear un ImageIcon a partir de los datos de la imagen
                    imageIcon = new ImageIcon(imageData);
                    // Escalar la imagen para mostrarla en la tabla
                    Image scaledImage = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(scaledImage);
                }

                // Agregar fila al modelo de tabla
                tableModel.addRow(new Object[]{id, nombre, apellido, imageIcon});
            }

            // Crear la tabla con el modelo de datos
            JTable tabla = new JTable(tableModel);

            // Crear el panel que contendrá la tabla
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

            // Crear el marco que contendrá el panel con la tabla
            JFrame frameTabla = new JFrame("Datos en Tabla");
            frameTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo la ventana de la tabla
            frameTabla.getContentPane().add(panelTabla);
            frameTabla.setSize(600, 400);
            frameTabla.setLocationRelativeTo(this);
            frameTabla.setVisible(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener información de la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void mostrarImagenPorId() {
        // Obtener el ID del usuario desde el campo de texto txtId
        int idUsuario = Integer.parseInt(txtId.getText());

        try (Connection conexion = establecerConexion();
             PreparedStatement statement = conexion.prepareStatement("SELECT imagen FROM usuarios WHERE id = ?");
        ) {

            statement.setInt(1, idUsuario);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                byte[] imageData = resultSet.getBytes("imagen");
                if (imageData != null) {
                    // Crear un ImageIcon a partir de los datos de la imagen
                    ImageIcon imageIcon = new ImageIcon(imageData);

                    // Escalar la imagen al tamaño de un carnet (5.5cm x 3.5cm)
                    Image image = imageIcon.getImage();
                    Image scaledImage = image.getScaledInstance(165, 150, Image.SCALE_SMOOTH);
                    ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

                    // Establecer el ImageIcon escalado en el JLabel
                    JLabel_Imagen.setIcon(scaledImageIcon);
                } else {
                    JOptionPane.showMessageDialog(this, "El usuario no tiene una imagen asociada.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con el ID proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener la imagen de la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarDatos() {
        try (Connection conexion = establecerConexion();
             PreparedStatement statement = conexion.prepareStatement("UPDATE usuarios SET cedula = ?, nombre = ?, apellido = ? WHERE id = ?")) {

            // Obtener los valores de los campos desde los JTextField
            String cedula = txtCedula.getText();
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            int id = Integer.parseInt(txtId.getText()); // Suponiendo que txtId es un JTextField donde se ingresa el ID del usuario a actualizar

            // Establecer los valores de los parámetros en el PreparedStatement
            statement.setString(1, cedula);
            statement.setString(2, nombre);
            statement.setString(3, apellido);
            statement.setInt(4, id);

            // Ejecutar la actualización
            int filasActualizadas = statement.executeUpdate();

            // Verificar si se realizaron actualizaciones y mostrar un mensaje correspondiente
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el usuario con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar datos en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarImagen(byte[] imageData) throws SQLException {
        Connection conexion = null;
        PreparedStatement statement = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            statement = conexion.prepareStatement("INSERT INTO usuarios (cedula,nombre, apellido, imagen) VALUES (?,?, ?, ?)");
            statement.setString(1, txtCedula.getText());
            statement.setString(2, txtNombre.getText());
            statement.setString(3, txtApellido.getText());
            statement.setBytes(4, imageData);

            statement.executeUpdate();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        }
    }




    private void eliminarRegistro() {
        try (Connection conexion = establecerConexion();
             PreparedStatement statement = conexion.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {

            // Suponiendo que txtId es un JTextField donde se ingresa el ID del usuario a eliminar
            int id = Integer.parseInt(txtId.getText());

            statement.setInt(1, id);

            int filasEliminadas = statement.executeUpdate();

            if (filasEliminadas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el usuario con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar registro en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
