package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class ManagerPerfil extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerPerfil() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 400);
        setTitle("MI PERFIL");
        setLocationRelativeTo(null);
        setResizable(false);

        String username = UserData.getUsername();

        // Panel principal para organizar los componentes
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Etiqueta de título con estilo
        JLabel labelTitulo = new JLabel("MI PERFIL", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(60, 63, 65)); // Fondo gris oscuro
        labelTitulo.setForeground(Color.WHITE); // Texto blanco
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(labelTitulo, BorderLayout.NORTH);

        // Panel central para los campos de información
        JPanel panelCentro = new JPanel(new GridLayout(3, 4, 10, 10));
        panelCentro.setBackground(new Color(255, 255, 255));
        panelCentro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Cargar datos del usuario
        String[] userData = cargarDatosUsuario(username);
        if (userData == null) {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Etiquetas y datos del usuario
        JLabel labelNombre = createLabel("NOMBRE:");
        JLabel fieldNombre = createDataLabel(userData[0]);

        JLabel labelEmail = createLabel("EMAIL:");
        JLabel fieldEmail = createDataLabel(userData[2]);

        JLabel labelNumero = createLabel("NÚMERO:");
        JLabel fieldNumero = createDataLabel(userData[3]);

        JLabel labelAddress = createLabel("DIRECCIÓN:");
        JLabel fieldAddress = createDataLabel(userData[4]);

        JLabel labelPostal = createLabel("CÓDIGO POSTAL:");
        JLabel fieldPostal = createDataLabel(userData[5]);

        JLabel labelEquipo = createLabel("EQUIPO:");
        JLabel fieldEquipo = createDataLabel(userData[6]);

        // Agregar componentes al panel central
        panelCentro.add(labelNombre);
        panelCentro.add(fieldNombre);
        panelCentro.add(labelAddress);
        panelCentro.add(fieldAddress);

        panelCentro.add(labelEmail);
        panelCentro.add(fieldEmail);
        panelCentro.add(labelPostal);
        panelCentro.add(fieldPostal);

        panelCentro.add(labelNumero);
        panelCentro.add(fieldNumero);
        panelCentro.add(labelEquipo);
        panelCentro.add(fieldEquipo);

        mainPanel.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior para los botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(240, 240, 240));

        // Panel para el botón de configuración en la esquina inferior izquierda
        JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzquierda.setBackground(new Color(240, 240, 240));
        JButton btnConfiguracion = createIconButton("/imagenes/avatar-de-usuario.png");
        btnConfiguracion.addActionListener(e -> {
            dispose();
            new ManagerConfig().setVisible(true);
        });
        panelIzquierda.add(btnConfiguracion);

        // Panel para el botón de notificaciones en la esquina inferior derecha
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerecha.setBackground(new Color(240, 240, 240));
        JButton btnNotificaciones = createIconButton("/imagenes/icono_notif_transparent.png");
        btnNotificaciones.addActionListener(e -> {
            dispose();
            new ManagerNotif().setVisible(true);
        });
        panelDerecha.add(btnNotificaciones);

        // Panel central para los botones de acción
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(new Color(240, 240, 240));
        JButton btnAtras = new JButton("Atrás");
        styleButton(btnAtras);
        btnAtras.addActionListener(e -> {
            dispose();
            new ManagerWelcome().setVisible(true);
        });

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        styleButton(btnCerrarSesion);
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new ManagerLogin().setVisible(true);
        });

        JButton btnCambiarContraseña = new JButton("Cambiar Contraseña");
        styleButton(btnCambiarContraseña);
        btnCambiarContraseña.addActionListener(e -> {
            String nuevaContraseña = JOptionPane.showInputDialog(this, "Introduce la nueva contraseña:");
            if (nuevaContraseña == null || nuevaContraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String contraseñaActual = obtenerContraseñaActual(username);
            if (nuevaContraseña.equals(contraseñaActual)) {
                JOptionPane.showMessageDialog(this, "La nueva contraseña no puede ser la misma que la actual.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (actualizarContraseña(username, nuevaContraseña)) {
                    JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al cambiar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelCentral.add(btnAtras);
        panelCentral.add(btnCambiarContraseña);
        panelCentral.add(btnCerrarSesion);

        // Agregar los paneles izquierdo, central y derecho al panel inferior
        panelInferior.add(panelIzquierda, BorderLayout.WEST);
        panelInferior.add(panelCentral, BorderLayout.CENTER);
        panelInferior.add(panelDerecha, BorderLayout.EAST);

        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);

        // Configuración de cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                new ManagerWelcome().setVisible(true);
            }
        });
    }

    // Crear una etiqueta para campos de datos
    private JLabel createDataLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    // Crear una etiqueta estilizada para nombres de campos
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(100, 100, 100));
        return label;
    }

    // Estilizar botones
    private void styleButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 63, 65));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
    }

    // Crear botón con icono
    private JButton createIconButton(String iconPath) {
        JButton button = new JButton();
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(img));
        button.setPreferredSize(new Dimension(50, 50));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    private String[] cargarDatosUsuario(String username) {
        String filePath = "usuarios.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos.length >= 6 && datos[0].equals(username)) {
                    return datos;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de usuarios", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private String obtenerContraseñaActual(String username) {
        String filePath = "usuarios.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos[0].equals(username)) {
                    return datos[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer la contraseña actual.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // Función para actualizar la contraseña en el archivo usuarios.csv
    private boolean actualizarContraseña(String username, String nuevaContraseña) {
        String filePath = "usuarios.csv";
        File archivoOriginal = new File(filePath);
        File archivoTemporal = new File("usuarios_temp.csv");
        boolean actualizado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivoOriginal));
             PrintWriter pw = new PrintWriter(new FileWriter(archivoTemporal))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos[0].equals(username)) {
                    datos[1] = nuevaContraseña; // Actualizar la contraseña
                    actualizado = true;
                }
                pw.println(String.join(";", datos));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Reemplazar el archivo original con el temporal si la contraseña se actualizó
        if (actualizado) {
            if (archivoOriginal.delete() && archivoTemporal.renameTo(archivoOriginal)) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el archivo de usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            archivoTemporal.delete();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerPerfil frame = new ManagerPerfil();
            frame.setVisible(true);
        });
    }
}
