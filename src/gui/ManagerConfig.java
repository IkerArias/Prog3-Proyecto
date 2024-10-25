package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerConfig extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerConfig() {
        setTitle("Configuración del Gestor de Plantillas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración del panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre componentes

        // Etiqueta para la selección de tema
        JLabel themeLabel = new JLabel("Seleccionar Tema:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(themeLabel, gbc);

        // JComboBox para seleccionar tema
        String[] themes = {"Claro", "Oscuro"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(themeComboBox, gbc);

        // Etiqueta para habilitar notificaciones
        JLabel notificationsLabel = new JLabel("Habilitar Notificaciones:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(notificationsLabel, gbc);

        // Checkbox para habilitar notificaciones
        JCheckBox notificationsCheckBox = new JCheckBox("Activar Notificaciones");
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(notificationsCheckBox, gbc);

        // Etiqueta para la frecuencia de actualización
        JLabel updateFrequencyLabel = new JLabel("Frecuencia de Actualización:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(updateFrequencyLabel, gbc);

        // JComboBox para seleccionar frecuencia de actualización
        String[] frequencies = {"Cada 5 minutos", "Cada 15 minutos", "Cada 30 minutos", "Cada hora"};
        JComboBox<String> frequencyComboBox = new JComboBox<>(frequencies);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(frequencyComboBox, gbc);

        // Botón para guardar cambios
        JButton saveButton = new JButton("Guardar Cambios");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        // Acción para el botón de guardar cambios
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTheme = (String) themeComboBox.getSelectedItem();
                boolean notificationsEnabled = notificationsCheckBox.isSelected();
                String selectedFrequency = (String) frequencyComboBox.getSelectedItem();

                // Aquí puedes implementar la lógica para guardar la configuración
                String message = String.format("Configuración Guardada:\nTema: %s\nNotificaciones: %s\nFrecuencia de Actualización: %s",
                        selectedTheme, notificationsEnabled ? "Activadas" : "Desactivadas", selectedFrequency);
                
                JOptionPane.showMessageDialog(panel, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Cierra la ventana de configuración
            }
        });

        // Añadir el panel al JFrame
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerConfig configFrame = new ManagerConfig();
            configFrame.setVisible(true);
        });
    }
}
