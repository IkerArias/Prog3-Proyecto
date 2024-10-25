package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Set;

public class ManagerRegister extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerRegister() {
        setTitle("Fantasy Manager - Crear Cuenta");
        setSize(600, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false); 

        // Configuración del panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre componentes

        // Etiqueta y campo para el nombre de usuario
        JLabel userLabel = new JLabel("Nombre de Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userField, gbc);

        // Etiqueta y campo para el correo electrónico
        JLabel emailLabel = new JLabel("Correo Electrónico:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);

        // Etiqueta y campo para la contraseña
        JLabel passLabel = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passField, gbc);

        // Etiqueta y campo para confirmar la contraseña
        JLabel confirmPassLabel = new JLabel("Confirmar Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(confirmPassLabel, gbc);

        JPasswordField confirmPassField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(confirmPassField, gbc);

        // Etiqueta y JComboBox para el equipo favorito
        JLabel teamLabel = new JLabel("Equipo Favorito:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(teamLabel, gbc);

        // Lista de equipos únicos
        String[] equipos = {
            "Seleccione un equipo", "Athletic Club", "Villarreal", "Valencia", "Rayo Vallecano", "Getafe", 
            "Real Madrid", "Atlético de Madrid", "Mallorca", "Leganés", "Barcelona", "Alavés", 
            "Real Sociedad", "Celta de Vigo", "Granada", "Osasuna", "Real Betis", "Espanyol", 
            "Elche", "UD Las Palmas", "Cadiz"
        };
        JComboBox<String> teamComboBox = new JComboBox<>(equipos);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(teamComboBox, gbc);

        // Botón de Crear Cuenta
        JButton createAccountButton = new JButton("Crear Cuenta");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        panel.add(createAccountButton, gbc);

        // Acción para el botón de crear cuenta
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String email = emailField.getText();
                String password = new String(passField.getPassword());
                String confirmPassword = new String(confirmPassField.getPassword());
                String selectedTeam = (String) teamComboBox.getSelectedItem();

                // Validación de campos
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedTeam.equals("Seleccione un equipo")) {
                    JOptionPane.showMessageDialog(panel, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(panel, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Cuenta creada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Aquí puedes añadir la lógica para almacenar o registrar la cuenta
                }
            }
        });

        // Añadir el panel a la ventana
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerRegister frame = new ManagerRegister();
            frame.setVisible(true);
        });
    }
}
