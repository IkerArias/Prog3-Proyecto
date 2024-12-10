package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import domain.Tema;
import domain.Tema.CambiarTema;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class ManagerConfig extends JFrame implements CambiarTema {

    private static final long serialVersionUID = 1L;
    private JPanel mainPanel;
    private JLabel headerLabel;
    private JComboBox<String> themeComboBox;

    public ManagerConfig(String username) {
        setTitle("Configuración del Gestor de Plantillas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Registrar la clase como listener de Tema
        Tema.addListener(this);

        // Configurar el panel principal
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                applyTheme();
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Encabezado
        headerLabel = new JLabel("Configuración del Gestor", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(headerLabel, gbc);

        // Selección de tema
        JLabel themeLabel = new JLabel("Seleccionar Tema:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(themeLabel, gbc);

        themeComboBox = new JComboBox<>(new String[]{"Claro", "Oscuro"});
        themeComboBox.setSelectedItem(Tema.getTemaActual() == Tema.Theme.CLARO ? "Claro" : "Oscuro");
        themeComboBox.addActionListener(e -> {
            String selectedTheme = (String) themeComboBox.getSelectedItem();
            if ("Claro".equals(selectedTheme)) {
                Tema.setTema(Tema.Theme.CLARO);
            } else {
                Tema.setTema(Tema.Theme.OSCURO);
            }
        });
        gbc.gridx = 1;
        mainPanel.add(themeComboBox, gbc);

        // Botón Atrás
        JButton btnAtras = new JButton("Atrás");
        btnAtras.addActionListener(e -> {
            dispose();
            ManagerPerfil perfil = new ManagerPerfil();
            perfil.setVisible(true);
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(btnAtras, gbc);

        // Botón Guardar Cambios
        JButton btnGuardarCambios = new JButton("Guardar Cambios");
        btnGuardarCambios.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "Configuración guardada correctamente.");
        });
        gbc.gridx = 1;
        mainPanel.add(btnGuardarCambios, gbc);

        // Botón Cambiar Contraseña
        JButton btnCambiarContraseña = new JButton("Cambiar Contraseña");
        btnCambiarContraseña.addActionListener(e -> cambiarContraseña(username));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(btnCambiarContraseña, gbc);

        // Botón Cerrar Sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new ManagerLogin().setVisible(true);
        });
        gbc.gridx = 1;
        mainPanel.add(btnCerrarSesion, gbc);

        // Configurar el icono de la ventana
        try {
            Image icono = ImageIO.read(new File("resources/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        add(mainPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerPerfil perfil = new ManagerPerfil();
                perfil.setVisible(true);
            }
        });
    }

    private void cambiarContraseña(String username) {
        String nuevaContraseña = JOptionPane.showInputDialog(this, "Introduce la nueva contraseña:");
        if (nuevaContraseña != null && !nuevaContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Contraseña para el usuario " + username + " ha sido actualizada.");
        }
    }

    private void applyTheme() {
        if (Tema.getTemaActual() == Tema.Theme.OSCURO) {
            mainPanel.setBackground(Color.DARK_GRAY);
            headerLabel.setForeground(Color.WHITE);
        } else {
            mainPanel.setBackground(Color.WHITE);
            headerLabel.setForeground(Color.BLACK);
        }
    }

    @Override
    public void onThemeChanged(Tema.Theme theme) {
        applyTheme();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerConfig managerConfig = new ManagerConfig("usuario");
            managerConfig.setVisible(true);
        });
    }
}
