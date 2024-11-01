package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class ManagerConfig extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerConfig() {
        setTitle("Configuración del Gestor de Plantillas");
        setSize(600, 450); // Aumenta el tamaño de la ventana para la nueva opción
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración del panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(230, 240, 255)); // Color de fondo personalizado
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Aumenta el espacio entre componentes
        
        // Encabezado
        JLabel headerLabel = new JLabel("Configuración del Gestor", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(60, 63, 65));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(headerLabel, gbc);

        // Etiqueta para la selección de tema
        JLabel themeLabel = new JLabel("Seleccionar Tema:");
        themeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(themeLabel, gbc);

        // JComboBox para seleccionar tema
        String[] themes = {"Claro", "Oscuro"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        themeComboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        themeComboBox.setBorder(new LineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        panel.add(themeComboBox, gbc);

        // Etiqueta para habilitar notificaciones
        JLabel notificationsLabel = new JLabel("Habilitar Notificaciones:");
        notificationsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(notificationsLabel, gbc);

        // Checkbox para habilitar notificaciones
        JCheckBox notificationsCheckBox = new JCheckBox("Activar Notificaciones");
        notificationsCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        notificationsCheckBox.setBackground(new Color(230, 240, 255));
        gbc.gridx = 1;
        panel.add(notificationsCheckBox, gbc);

        // Etiqueta para la frecuencia de actualización
        JLabel updateFrequencyLabel = new JLabel("Frecuencia de Actualización:");
        updateFrequencyLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(updateFrequencyLabel, gbc);

        // JComboBox para seleccionar frecuencia de actualización
        String[] frequencies = {"Cada 5 minutos", "Cada 15 minutos", "Cada 30 minutos", "Cada hora"};
        JComboBox<String> frequencyComboBox = new JComboBox<>(frequencies);
        frequencyComboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        frequencyComboBox.setBorder(new LineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        panel.add(frequencyComboBox, gbc);

        // Nueva opción: Selección de tamaño de fuente
        JLabel fontSizeLabel = new JLabel("Tamaño de Fuente:");
        fontSizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(fontSizeLabel, gbc);

        // JComboBox para seleccionar tamaño de fuente
        String[] fontSizes = {"Pequeño", "Mediano", "Grande"};
        JComboBox<String> fontSizeComboBox = new JComboBox<>(fontSizes);
        fontSizeComboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        fontSizeComboBox.setBorder(new LineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        panel.add(fontSizeComboBox, gbc);
        
        // Panel para los botones "Atrás" y "Guardar Cambios"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(230, 240, 255)); // Mismo color de fondo que el panel principal

        // Botón Atrás
        JButton btnAtras = new JButton("Atrás");
        styleButton(btnAtras);
        btnAtras.addActionListener(e -> {
            dispose();
            ManagerPerfil per = new ManagerPerfil();
            per.setVisible(true);
        });
        buttonPanel.add(btnAtras); // Agregar botón Atrás al panel de botones

        // Botón para guardar cambios
        JButton saveButton = new JButton("Guardar Cambios");
        styleButton(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTheme = (String) themeComboBox.getSelectedItem();
                boolean notificationsEnabled = notificationsCheckBox.isSelected();
                String selectedFrequency = (String) frequencyComboBox.getSelectedItem();
                String selectedFontSize = (String) fontSizeComboBox.getSelectedItem();

                // Aquí puedes implementar la lógica para aplicar el tamaño de fuente
                int fontSize = switch (selectedFontSize) {
                    case "Pequeño" -> 12;
                    case "Mediano" -> 16;
                    case "Grande" -> 20;
                    default -> 16;
                };
                
                // Aplicar el tamaño de fuente al panel (ejemplo)
                panel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));

                String message = String.format("Configuración Guardada:\nTema: %s\nNotificaciones: %s\nFrecuencia de Actualización: %s\nTamaño de Fuente: %s",
                        selectedTheme, notificationsEnabled ? "Activadas" : "Desactivadas", selectedFrequency, selectedFontSize);

                JOptionPane.showMessageDialog(panel, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Cierra la ventana de configuración
                
                // Vuelta a la ventana del perfil
                ManagerPerfil perfil = new ManagerPerfil();
                perfil.setVisible(true);
            }
        });
        buttonPanel.add(saveButton); // Agregar botón Guardar Cambios al panel de botones

        // Añadir el panel de botones al layout principal
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        // Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("src/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        // Añadir el panel al JFrame
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
    
    // Método para estilizar botones
    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerConfig configFrame = new ManagerConfig();
            configFrame.setVisible(true);
        });
    }
}
