package gui;

import javax.swing.*;
import java.awt.*;
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

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(230, 240, 250)); // Fondo claro y agradable

        // Título con estilo
        JLabel labelTitulo = new JLabel("MI PERFIL", JLabel.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(33, 150, 243)); // Azul brillante
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(labelTitulo, BorderLayout.NORTH);

        // Panel para los datos del usuario
        JPanel panelCentro = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Cargar datos del usuario
        String[] userData = cargarDatosUsuario(username);
        if (userData == null) {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Datos de usuario en recuadros centrados
        panelCentro.add(createDataPanel("NOMBRE", userData[0]));
        panelCentro.add(createDataPanel("DIRECCIÓN", userData[4]));
        panelCentro.add(createDataPanel("EMAIL", userData[2]));
        panelCentro.add(createDataPanel("CÓDIGO POSTAL", userData[5]));
        panelCentro.add(createDataPanel("NÚMERO", userData[3]));
        panelCentro.add(createDataPanel("EQUIPO", userData[6]));

        mainPanel.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior para los botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(230, 240, 250));

        // Panel izquierdo para botón de configuración
        JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzquierda.setBackground(new Color(230, 240, 250));
        JButton btnConfiguracion = createIconButton("/imagenes/avatar-de-usuario.png");
        btnConfiguracion.addActionListener(e -> {
            dispose();
            new ManagerConfig("UsurioEjemplo").setVisible(true);
        });
        panelIzquierda.add(btnConfiguracion);

        // Panel central para botones de acción
        JPanel panelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelCentral.setBackground(new Color(230, 240, 250));

        JButton btnAtras = new JButton("Atrás");
        styleButton(btnAtras);
        btnAtras.addActionListener(e -> {
            dispose();
            new ManagerWelcome().setVisible(true);
        });


        panelCentral.add(btnAtras);


        // Panel derecho para botón de notificaciones
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerecha.setBackground(new Color(230, 240, 250));
        JButton btnNotificaciones = createIconButton("/imagenes/icono_notif_transparent.png");
        btnNotificaciones.addActionListener(e -> {
            dispose();
            new ManagerNotif().setVisible(true);
        });
        panelDerecha.add(btnNotificaciones);

        // Añadir paneles izquierdo, central y derecho al panel inferior
        panelInferior.add(panelIzquierda, BorderLayout.WEST);
        panelInferior.add(panelCentral, BorderLayout.CENTER);
        panelInferior.add(panelDerecha, BorderLayout.EAST);

        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                new ManagerWelcome().setVisible(true);
            }
        });
    }

    // Método para crear panel de datos centrado con borde
    private JPanel createDataPanel(String label, String data) {
        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.setBackground(Color.WHITE);
        dataPanel.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true));

        JLabel lblTitle = new JLabel(label, JLabel.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(new Color(63, 81, 181)); // Azul vibrante

        JLabel lblData = new JLabel(data, JLabel.CENTER);
        lblData.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblData.setForeground(new Color(85, 85, 85));

        dataPanel.add(lblTitle, BorderLayout.NORTH);
        dataPanel.add(lblData, BorderLayout.CENTER);
        return dataPanel;
    }

    // Método para estilizar botones
    private void styleButton(JButton button) {
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(63, 81, 181)); // Azul vibrante para botones
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

    // Función para cambiar contraseña
    private void cambiarContraseña(String username) {
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
                    datos[1] = nuevaContraseña;
                    actualizado = true;
                }
                pw.println(String.join(";", datos));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

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


