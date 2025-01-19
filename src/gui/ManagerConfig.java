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

        Tema.addListener(this);

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

        headerLabel = new JLabel("Configuración del Gestor", JLabel.CENTER);
        headerLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(headerLabel, gbc);

        JLabel themeLabel = new JLabel("Seleccionar Tema:");
        themeLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(themeLabel, gbc);

        themeComboBox = new JComboBox<>(new String[]{"Claro", "Oscuro"});
        themeComboBox.setFont(new Font("Verdana", Font.PLAIN, 16));
        themeComboBox.setSelectedItem(Tema.getTemaActual() == Tema.Theme.CLARO ? "Claro" : "Oscuro");
        themeComboBox.addActionListener(e -> {
            String selectedTheme = (String) themeComboBox.getSelectedItem();
            Tema.setTema("Claro".equals(selectedTheme) ? Tema.Theme.CLARO : Tema.Theme.OSCURO);
        });
        gbc.gridx = 1;
        mainPanel.add(themeComboBox, gbc);

        // Botones principales
        addLabelWithButton("Cambiar Contraseña:", "Cambiar Contraseña", gbc, 0, 2, e -> cambiarContraseña(username));
        addLabelWithButton("Cerrar Sesión:", "Cerrar Sesión", gbc, 0, 3, e -> {
            dispose();
            new ManagerLogin().setVisible(true);
        });

        // Botón Atrás y Guardar Cambios en la parte inferior
        addBottomButton("Atrás", gbc, 0, 4, new Color(64, 64, 64), e -> {
            dispose();
            ManagerPerfil perfil = new ManagerPerfil();
            perfil.setVisible(true);
        });

        addBottomButton("Guardar Cambios", gbc, 1, 4, new Color(128, 128, 128), e -> {
            JOptionPane.showMessageDialog(mainPanel, "Configuración guardada correctamente.");
        });

        setWindowIcon("resources/imagenes/logo.png");

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

    private void addLabelWithButton(String labelText, String buttonText, GridBagConstraints gbc, int x, int y, ActionListener action) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Verdana", Font.PLAIN, 16));
        gbc.gridx = x;
        gbc.gridy = y;
        mainPanel.add(label, gbc);

        JButton button = new JButton(buttonText);
        button.setFont(new Font("Verdana", Font.PLAIN, 14));
        button.setBackground(new Color(153, 204, 255));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.addActionListener(action);
        gbc.gridx = x + 1;
        mainPanel.add(button, gbc);
    }

    private void addBottomButton(String text, GridBagConstraints gbc, int x, int y, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Verdana", Font.BOLD, 14));
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.addActionListener(action);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(button, gbc);
    }

    private void setWindowIcon(String iconPath) {
        try {
            Image icon = ImageIO.read(new File(iconPath));
            setIconImage(icon);
        } catch (IOException e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
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
