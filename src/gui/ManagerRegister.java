package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class ManagerRegister extends JFrame {
	private BufferedImage imagenCargada;
	
	 

    private static final long serialVersionUID = 1L;

    public ManagerRegister() {
    	
    	//Configuracion inicial de la ventana con componentes basicos
        setTitle("Fantasy Manager - Crear Cuenta");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración del panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(196, 238, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Espacio entre componentes
        
     // Título de la ventana
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Estilo del título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        panel.add(titleLabel, gbc);

        // Ajustar el GridBagConstraints para el resto de componentes
        gbc.gridwidth = 1; // Restablecer a una columna

        // Etiqueta y campo para el nombre de usuario
        JLabel userLabel = new JLabel("Nombre de Usuario:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Arial", Font.BOLD, 14));
        userField.setPreferredSize(new Dimension(100,25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(userField, gbc);

        // Etiqueta y campo para el correo electrónico
        JLabel emailLabel = new JLabel("Correo Electrónico:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2; // Cambiar a nueva fila
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.BOLD, 14));
        emailField.setPreferredSize(new Dimension(100,25));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(emailField, gbc);

        // Etiqueta y campo para la fecha de nacimiento
        JLabel birthDateLabel = new JLabel("Fecha de Nacimiento:");
        birthDateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        gbc.gridx = 0;
        gbc.gridy = 3; // Cambiar a nueva fila
        panel.add(birthDateLabel, gbc);

        // Configuración del selector de fecha de nacimiento con JSpinner
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setFont(new Font("Arial", Font.BOLD, 14));
        dateEditor.setPreferredSize(new Dimension(90, 25));
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new Date()); // Establece la fecha actual
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(dateSpinner, gbc);

        // Etiqueta y campo para el número de teléfono
        JLabel phoneLabel = new JLabel("Número de Teléfono:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));;
        gbc.gridx = 0;
        gbc.gridy = 4; // Cambiar a nueva fila
        panel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(20);
        phoneField.setFont(new Font("Arial", Font.BOLD, 14));
        phoneField.setPreferredSize(new Dimension(100,25));
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(phoneField, gbc);

        // Etiqueta y campo para la dirección
        JLabel addressLabel = new JLabel("Dirección:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 5; // Cambiar a nueva fila
        panel.add(addressLabel, gbc);

        JTextField addressField = new JTextField(20);
        addressField.setFont(new Font("Arial", Font.BOLD, 14));
        addressField.setPreferredSize(new Dimension(100,25));
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(addressField, gbc);

        // Etiqueta y campo para el código postal
        JLabel postalCodeLabel = new JLabel("Código Postal:");
        postalCodeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 6; // Cambiar a nueva fila
        panel.add(postalCodeLabel, gbc);

        JTextField postalCodeField = new JTextField(20);
        postalCodeField.setFont(new Font("Arial", Font.BOLD, 14));
        postalCodeField.setPreferredSize(new Dimension(100,25));
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(postalCodeField, gbc);
        
     // Etiqueta y campo para la foto de usuario 
        JLabel imagenLabel1 = new JLabel("Foto de usuario:");
        imagenLabel1.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(imagenLabel1, gbc);
        
        Color softColor = new Color(0, 51, 102);

   
        JButton cargarFotoButton = new JButton("Cargar Foto");
        
        cargarFotoButton.setFont(new Font("Arial", Font.BOLD, 14));
        cargarFotoButton.setForeground(Color.WHITE);
        cargarFotoButton.setBackground(softColor); 
        cargarFotoButton.setFocusPainted(false);
        cargarFotoButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cargarFotoButton.setPreferredSize(new Dimension(100, 30));
        cargarFotoButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cargarFotoButton.setOpaque(true);
        
        cargarFotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarFoto();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 7; 
        panel.add(cargarFotoButton, gbc);
       

        // Etiqueta y campo para la contraseña
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 8; // Cambiar a nueva fila
        panel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        gbc.gridy = 8;
        panel.add(passField, gbc);

        // Etiqueta y campo para confirmar la contraseña
        JLabel confirmPassLabel = new JLabel("Confirmar Contraseña:");
        confirmPassLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 9; // Cambiar a nueva fila
        panel.add(confirmPassLabel, gbc);

        JPasswordField confirmPassField = new JPasswordField(20);
        confirmPassField.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        gbc.gridy = 9;
        panel.add(confirmPassField, gbc);

        // Etiqueta y JComboBox para el equipo favorito
        JLabel teamLabel = new JLabel("Equipo Favorito:");
        teamLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 10; // Cambiar a nueva fila
        panel.add(teamLabel, gbc);
        
       

        // Lista de equipos únicos
        String[] equipos = {
            "Seleccione un equipo", "Athletic Club", "Villarreal", "Valencia", "Rayo Vallecano", "Getafe",
            "Real Madrid", "Atlético de Madrid", "Mallorca", "Leganés", "Barcelona", "Alavés",
            "Real Sociedad", "Celta de Vigo", "Granada", "Osasuna", "Real Betis", "Espanyol",
            "Elche", "UD Las Palmas", "Cadiz"
        };
        JComboBox<String> teamComboBox = new JComboBox<>(equipos);
        teamComboBox.setFont(new Font("Arial", Font.BOLD, 14));
        teamComboBox.setForeground(Color.black);
        teamComboBox.setPreferredSize(new Dimension(120,25));
        teamComboBox.setBackground(Color.white); 
        teamComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        gbc.gridy = 10;
        panel.add(teamComboBox, gbc);

     // Botón de Crear Cuenta
     // Botón de Crear Cuenta
        JButton createAccountButton = new JButton("Crear Cuenta");
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 16));
        createAccountButton.setForeground(Color.BLACK);
        createAccountButton.setBackground(new Color(255, 239, 108)); 
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        createAccountButton.setPreferredSize(new Dimension(120, 40)); // Tamaño uniforme

        
        
        gbc.gridx = 1; 
        gbc.gridy = 12; 
        gbc.insets = new Insets(10, 20, 10, 20); 
        panel.add(createAccountButton, gbc);

        
        
      //Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("resources/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Abrir la ventana de ManagerLogin cuando se cierra esta ventana
            	dispose();
                ManagerLogin loginFrame = new ManagerLogin();
                loginFrame.setVisible(true);
            }
        });
        


        // Acción para el botón de crear cuenta
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                String postalCode = postalCodeField.getText().trim();
                String password = new String(passField.getPassword());
                String confirmPassword = new String(confirmPassField.getPassword());
                String selectedTeam = (String) teamComboBox.getSelectedItem();

                if (username.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                    address.isEmpty() || postalCode.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                    selectedTeam.equals("Seleccione un equipo")) {
                    JOptionPane.showMessageDialog(panel, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(panel, "Ingrese un correo electrónico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(panel, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!phone.matches("\\d{10,}")) {
                    JOptionPane.showMessageDialog(panel, "El número de teléfono debe contener al menos 10 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Integer.parseInt(postalCode);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "El código postal debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (imagenCargada == null) {
                    int confirm = JOptionPane.showConfirmDialog(panel, "No se ha cargado una foto. ¿Desea continuar?", "Advertencia", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) return;
                }

                // Lógica para guardar datos y redirigir al usuario
                Date selectedDate = (Date) dateSpinner.getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);
                int year = calendar.get(Calendar.YEAR);
                int age = Calendar.getInstance().get(Calendar.YEAR) - year;

                if (age < 18) {
                    showConsentDialog();
                } else {
                    String fotoUsuario = (imagenCargada != null) ? convertImageToBase64(imagenCargada) : "No Foto";
                    registerUser(username, password, email, phone, address, postalCode, selectedTeam, selectedDate, fotoUsuario);
                    JOptionPane.showMessageDialog(panel, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new ManagerLogin().setVisible(true);
                }
            }
        });
        
        
        
     // Botón de Atrás
        JButton backButton = new JButton("Atrás");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0, 51, 102)); // Azul oscuro
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        backButton.setPreferredSize(new Dimension(120, 40)); // Tamaño uniforme
        backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backButton.setOpaque(true);

        
        gbc.gridx = 0; 
        gbc.gridy = 12; 
        gbc.insets = new Insets(10, 20, 10, 20); 
        panel.add(backButton, gbc);
        
        // Acción para el botón Atrás
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                ManagerLogin loginFrame = new ManagerLogin();
                loginFrame.setVisible(true); // Muestra la ventana de inicio de sesión
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
        
        consentDialog.setSize(600, 220);
        consentDialog.setLayout(new BorderLayout());
        consentDialog.setBackground(new Color(196, 238, 255));

        // Texto de términos y condiciones
        JTextArea termsArea = new JTextArea("Autorización para el uso de la cuenta:\n\n"
                + "1. El tutor legal autoriza al menor a crear y usar una cuenta en esta plataforma.\n"
                + "2. El menor se compromete a utilizar la cuenta de manera responsable y adecuada.\n"
                + "3. No se permitirá el uso de lenguaje ofensivo ni el incumplimiento de las normas de la plataforma.\n\n"
                + "Al marcar la casilla, el tutor legal acepta los términos y condiciones y autoriza al menor a usar la cuenta.");

        termsArea.setLineWrap(true);
        termsArea.setWrapStyleWord(true);
        termsArea.setEditable(false);
        termsArea.setBackground(consentDialog.getBackground());
        termsArea.setMargin(new Insets(10, 10, 10, 10));
        termsArea.setFocusable(false); // Evitar la barra de desplazamiento
        consentDialog.add(termsArea, BorderLayout.CENTER);

        // Panel para el checkbox y botón de aceptar
     // Panel para el checkbox y botón de aceptar
        JPanel consentPanel = new JPanel();
        // Cambiar a FlowLayout con alineación centrada
        consentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        consentPanel.setBackground(new Color(196, 238, 255));

        // Casilla de verificación para el consentimiento
        JCheckBox consentCheckBox = new JCheckBox("Confirmo que soy el tutor legal y acepto los términos.");
        consentCheckBox.setBackground(new Color(196, 238, 255));

        // Añadir checkbox al panel
        consentPanel.add(consentCheckBox);

        // Botón para aceptar (centrado)
        JButton acceptButton = new JButton("Aceptar");
        acceptButton.setFont(new Font("Arial", Font.BOLD, 14));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setBackground(new Color(0, 51, 102));
        acceptButton.setFocusPainted(false);
        acceptButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        acceptButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        acceptButton.setOpaque(true);

        // Ajusta el tamaño del botón
        acceptButton.setPreferredSize(new Dimension(110, 35)); 
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (consentCheckBox.isSelected()) {
                    JOptionPane.showMessageDialog(consentDialog, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    consentDialog.dispose(); // Cerrar el diálogo de consentimiento
                    dispose(); // Cerrar la ventana actual
                    ManagerLogin v = new ManagerLogin();
                    v.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(consentDialog, "Debe aceptar los términos para continuar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Añadir el botón centrado al panel
        consentPanel.add(acceptButton);

        // Añadir el panel de consentimiento al sur del diálogo
        consentDialog.add(consentPanel, BorderLayout.SOUTH);

        
        consentDialog.setLocationRelativeTo(this);
        consentDialog.setVisible(true); // Muestra el diálogo
        

        
        
    }
 // Método para convertir la imagen a Base64
    private String convertImageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            return java.util.Base64.getEncoder().encodeToString(imageBytes); // Devuelve la imagen en Base64
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // Método para registrar al usuario en el archivo
    private void registerUser(String username, String password, String email, String phone, String address, String postalCode, String selectedTeam, Date birthDate,String fotoUsuario) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/data/usuarios.csv", true))) {
            // Guardar datos en el fichero csv
            bw.write(username + ";" + password + ";" + email + ";" + phone + ";" + address + ";" + postalCode + ";" + selectedTeam + ";" + birthDate + (fotoUsuario != null ? fotoUsuario : "No Foto"));
            bw.newLine(); 
            bw.flush();
            System.out.println("Usuario guardado"); //Linea para comprobar por la consola
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Metodo para cargar fotos
    private void cargarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona una foto de perfil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Cargar la imagen en la variable imagenCargada
                imagenCargada = ImageIO.read(selectedFile);
                // La imagen está cargada, pero no se muestra en la interfaz
                System.out.println("Imagen cargada correctamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerRegister frame = new ManagerRegister();
            frame.setVisible(true);
        });
    }
    
    
}
