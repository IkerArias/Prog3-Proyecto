package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

public class ManagerRegister extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerRegister() {
        setTitle("Fantasy Manager - Crear Cuenta");
        setSize(600, 600);
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

        // Etiqueta y campo para la fecha de nacimiento
        JLabel birthDateLabel = new JLabel("Fecha de Nacimiento:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(birthDateLabel, gbc);

        // Configuración del selector de fecha de nacimiento con JSpinner
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date()); // Establece la fecha actual
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(dateSpinner, gbc);

        // Etiqueta y campo para el número de teléfono
        JLabel phoneLabel = new JLabel("Número de Teléfono:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(phoneField, gbc);

        // Etiqueta y campo para la dirección
        JLabel addressLabel = new JLabel("Dirección:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(addressLabel, gbc);

        JTextField addressField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(addressField, gbc);

        // Etiqueta y campo para el código postal
        JLabel postalCodeLabel = new JLabel("Código Postal:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(postalCodeLabel, gbc);

        JTextField postalCodeField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(postalCodeField, gbc);

        // Etiqueta y campo para la contraseña
        JLabel passLabel = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(passField, gbc);

        // Etiqueta y campo para confirmar la contraseña
        JLabel confirmPassLabel = new JLabel("Confirmar Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(confirmPassLabel, gbc);

        JPasswordField confirmPassField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(confirmPassField, gbc);

        // Etiqueta y JComboBox para el equipo favorito
        JLabel teamLabel = new JLabel("Equipo Favorito:");
        gbc.gridx = 0;
        gbc.gridy = 8;
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
        gbc.gridy = 8;
        panel.add(teamComboBox, gbc);

        // Botón de Crear Cuenta
        JButton createAccountButton = new JButton("Crear Cuenta");
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        panel.add(createAccountButton, gbc);

        // Acción para el botón de crear cuenta
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();
                String postalCode = postalCodeField.getText();
                String password = new String(passField.getPassword());
                String confirmPassword = new String(confirmPassField.getPassword());
                String selectedTeam = (String) teamComboBox.getSelectedItem();

                // Obtener la fecha de nacimiento desde el JSpinner
                Date selectedDate = (Date) dateSpinner.getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int age = Calendar.getInstance().get(Calendar.YEAR) - year;

                // Validación de campos
                if (username.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                    address.isEmpty() || postalCode.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                    selectedTeam.equals("Seleccione un equipo")) {
                    JOptionPane.showMessageDialog(panel, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(panel, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(panel, "Por favor, ingrese un correo electrónico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (age < 18) {
                        showConsentDialog();
                    } else {
                        JOptionPane.showMessageDialog(panel, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        ManagerLogin v = new ManagerLogin();
                        v.setVisible(true); // Cierra la ventana principal y vuelve a la ventana de login
                    }
                }
            }
        });

        // Añadir el panel al JFrame
        add(panel);
    }

    // Método para validar el correo electrónico
    private boolean isValidEmail(String email) {
        return email.contains("@") && (email.endsWith(".com") || email.endsWith(".es"));
    }

    // Método para mostrar la ventana de consentimiento
    private void showConsentDialog() {
        JDialog consentDialog = new JDialog(this, "Consentimiento del Tutor Legal", true);
        consentDialog.setSize(400, 300);
        consentDialog.setLayout(new BorderLayout());

        // Texto de términos y condiciones
        JTextArea termsArea = new JTextArea("Términos y Condiciones:\n\n"
                + "1. El usuario debe ser responsable de su cuenta.\n"
                + "2. No se permite el uso de lenguaje ofensivo.\n"
                + "3. El usuario debe cumplir con las normas establecidas.\n\n"
                + "Al marcar la casilla, el tutor legal acepta los términos anteriores.");
        termsArea.setLineWrap(true);
        termsArea.setWrapStyleWord(true);
        termsArea.setEditable(false);
        termsArea.setBackground(consentDialog.getBackground());
        termsArea.setMargin(new Insets(10, 10, 10, 10));
        termsArea.setFocusable(false); // Evitar la barra de desplazamiento
        consentDialog.add(termsArea, BorderLayout.CENTER);

        // Panel para el checkbox y botón de aceptar
        JPanel consentPanel = new JPanel();
        consentPanel.setLayout(new BoxLayout(consentPanel, BoxLayout.Y_AXIS));

        // Casilla de verificación para el consentimiento
        JCheckBox consentCheckBox = new JCheckBox("Confirmo que soy el tutor legal y acepto los términos.");
        consentPanel.add(consentCheckBox); // Añadir checkbox al panel

        // Botón para aceptar (más pequeño y centrado)
        JButton acceptButton = new JButton("Aceptar");
        acceptButton.setPreferredSize(new Dimension(100, 30)); // Ajusta el tamaño del botón
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (consentCheckBox.isSelected()) {
                    JOptionPane.showMessageDialog(consentDialog, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    consentDialog.dispose(); // Cerrar el diálogo de consentimiento
                    dispose();
                    ManagerLogin v = new ManagerLogin();
                    v.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(consentDialog, "Debe aceptar los términos para continuar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Añadir checkbox y botón al panel de consentimiento
        consentPanel.add(acceptButton);
        consentDialog.add(consentPanel, BorderLayout.SOUTH);
        
        consentDialog.setLocationRelativeTo(this);
        consentDialog.setVisible(true); // Muestra el diálogo
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerRegister frame = new ManagerRegister();
            frame.setVisible(true);
        });
    }
}
