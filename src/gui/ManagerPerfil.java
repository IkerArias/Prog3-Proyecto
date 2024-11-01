package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

        // Panel central para los campos de información con fondo suave
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new GridLayout(3, 4, 10, 10));
        panelCentro.setBackground(new Color(255, 255, 255)); // Fondo blanco
        panelCentro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
      //Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("src/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

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
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout());
        panelInferior.setBackground(new Color(240, 240, 240));

        // Botón de Configuración con icono
        JButton btnConfiguracion = createIconButton("/imagenes/avatar-de-usuario.png");

        btnConfiguracion.addActionListener(e -> {
            dispose();
            ManagerConfig conf = new ManagerConfig();
            conf.setVisible(true);
        });

        // Botón Atrás
        JButton btnAtras = new JButton("Atrás");
        styleButton(btnAtras);

        btnAtras.addActionListener(e -> {
            dispose();
            ManagerWelcome wel = new ManagerWelcome();
            wel.setVisible(true);
        });

        // Botón Cerrar Sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        styleButton(btnCerrarSesion);

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            ManagerLogin login = new ManagerLogin();
            login.setVisible(true);
        });

        // Botón de Notificaciones con icono
        JButton btnNotificaciones = createIconButton("/imagenes/icono_notif_transparent.png");

        btnNotificaciones.addActionListener(e -> {
            dispose();
            ManagerNotif notif = new ManagerNotif();
            notif.setVisible(true);
        });

        // Añadir botones al panel inferior
        panelInferior.add(btnConfiguracion);
        panelInferior.add(btnAtras);
        panelInferior.add(btnCerrarSesion);
        panelInferior.add(btnNotificaciones);

        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);

        // Configuración de cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerWelcome wel = new ManagerWelcome();
                wel.setVisible(true);
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
        String filePath = "usuarios.csv"; // Ruta del archivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos.length >= 6 && datos[0].equals(username)) { // Comparar por username
                    return datos;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de usuarios", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; // Usuario no encontrado
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerPerfil frame = new ManagerPerfil();
            frame.setVisible(true);
        });
    }
}
