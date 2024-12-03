package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ManagerConfig extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerConfig(String username) {  // Pasamos el nombre de usuario como parámetro al constructor
        setTitle("Configuración del Gestor de Plantillas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con gradiente de fondo
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(196, 238, 255);
                Color color2 = Color.WHITE;
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Encabezado con fuente estilizada
        JLabel headerLabel = new JLabel("Configuración del Gestor", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(60, 63, 65));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(headerLabel, gbc);

        // Etiqueta y JComboBox para el tema
        JLabel themeLabel = new JLabel("Seleccionar Tema:");
        themeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        themeLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(themeLabel, gbc);

        String[] themes = {"Claro", "Oscuro"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        styleComboBox(themeComboBox);
        gbc.gridx = 1;
        panel.add(themeComboBox, gbc);

        // Etiqueta y JCheckBox para notificaciones
        JLabel notificationsLabel = new JLabel("Habilitar Notificaciones:");
        notificationsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        notificationsLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(notificationsLabel, gbc);

        JCheckBox notificationsCheckBox = new JCheckBox("Activar Notificaciones");
        styleCheckBox(notificationsCheckBox);
        gbc.gridx = 1;
        panel.add(notificationsCheckBox, gbc);

        // Etiqueta y JComboBox para la frecuencia de actualización
        JLabel updateFrequencyLabel = new JLabel("Frecuencia de Actualización:");
        updateFrequencyLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        updateFrequencyLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(updateFrequencyLabel, gbc);

        String[] frequencies = {"Cada 5 minutos", "Cada 15 minutos", "Cada 30 minutos", "Cada hora"};
        JComboBox<String> frequencyComboBox = new JComboBox<>(frequencies);
        styleComboBox(frequencyComboBox);
        gbc.gridx = 1;
        panel.add(frequencyComboBox, gbc);

        // Etiqueta y JComboBox para tamaño de fuente
        JLabel fontSizeLabel = new JLabel("Tamaño de Fuente:");
        fontSizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        fontSizeLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(fontSizeLabel, gbc);

        String[] fontSizes = {"Pequeño", "Mediano", "Grande"};
        JComboBox<String> fontSizeComboBox = new JComboBox<>(fontSizes);
        styleComboBox(fontSizeComboBox);
        gbc.gridx = 1;
        panel.add(fontSizeComboBox, gbc);

        // Panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        // Botón "Atrás"
        JButton btnAtras = new JButton("Atrás");
        styleButton(btnAtras);
        btnAtras.addActionListener(e -> {
            dispose();
            ManagerPerfil per = new ManagerPerfil();
            per.setVisible(true);
        });
        buttonPanel.add(btnAtras);

        // Botón "Guardar Cambios"
        JButton saveButton = new JButton("Guardar Cambios");
        styleButton(saveButton);
        saveButton.addActionListener(e -> {
            String selectedTheme = (String) themeComboBox.getSelectedItem();
            boolean notificationsEnabled = notificationsCheckBox.isSelected();
            String selectedFrequency = (String) frequencyComboBox.getSelectedItem();
            String selectedFontSize = (String) fontSizeComboBox.getSelectedItem();

            // Ajuste del tema
            if ("Oscuro".equals(selectedTheme)) {
                panel.setBackground(new Color(45, 45, 45));
                headerLabel.setForeground(Color.WHITE);
                themeLabel.setForeground(Color.WHITE);
                notificationsLabel.setForeground(Color.WHITE);
                updateFrequencyLabel.setForeground(Color.WHITE);
                fontSizeLabel.setForeground(Color.WHITE);
            } else {
                panel.setBackground(new Color(230, 240, 255));
                headerLabel.setForeground(new Color(60, 63, 65));
                themeLabel.setForeground(new Color(60, 63, 65));
                notificationsLabel.setForeground(new Color(60, 63, 65));
                updateFrequencyLabel.setForeground(new Color(60, 63, 65));
                fontSizeLabel.setForeground(new Color(60, 63, 65));
            }

            String message = String.format("Configuración Guardada:\nTema: %s\nNotificaciones: %s\nFrecuencia de Actualización: %s\nTamaño de Fuente: %s",
                    selectedTheme, notificationsEnabled ? "Activadas" : "Desactivadas", selectedFrequency, selectedFontSize);

            JOptionPane.showMessageDialog(panel, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            ManagerPerfil perfil = new ManagerPerfil();
            perfil.setVisible(true);
        });
        buttonPanel.add(saveButton);

        // Botón "Cambiar Contraseña"
        JButton btnCambiarContraseña = new JButton("Cambiar Contraseña");
        styleButton(btnCambiarContraseña);
        btnCambiarContraseña.addActionListener(e -> cambiarContraseña(username));  // Usa el nombre de usuario pasado al constructor
        buttonPanel.add(btnCambiarContraseña);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        styleButton(btnCerrarSesion);
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new ManagerLogin().setVisible(true);
        });
        
        buttonPanel.add(btnCambiarContraseña);

        try {
            Image icono = ImageIO.read(new File("resources/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerPerfil perf = new ManagerPerfil();
                perf.setVisible(true);
            }
        });
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 63, 65));
            }
        });
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        checkBox.setBackground(new Color(230, 240, 255));
    }

    private void cambiarContraseña(String username) {
        String nuevaContraseña = JOptionPane.showInputDialog(this, "Introduce la nueva contraseña:");
        if (nuevaContraseña != null && !nuevaContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Contraseña cambiada para el usuario " + username);
            // Aquí puedes agregar lógica adicional para cambiar la contraseña en la base de datos o archivo de configuración
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerConfig config = new ManagerConfig("usuario");
            config.setVisible(true);
        });
    }
}
