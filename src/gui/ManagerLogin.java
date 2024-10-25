package gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerLogin extends JFrame {

    private static final long serialVersionUID = 1L;

	// Constructor de la ventana
    public ManagerLogin() {
        // Configuración de la ventana
        setTitle("Fantasy Manager - Iniciar sesión");
        setSize(450, 200); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false); 

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Márgenes

        // Colores
        panel.setBackground(new Color(240, 240, 240)); 

        // Componentes de la ventana
        JLabel userLabel = new JLabel("Usuario o Email:");
        JTextField userText = new JTextField(20);
        userText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); 

        JLabel passwordLabel = new JLabel("Contraseña:");
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); 

        JButton loginButton = new JButton("Iniciar sesión");
        JButton registerButton = new JButton("Crear cuenta");

        // Estilo de los botones
        loginButton.setBackground(new Color(100, 150, 255));
        loginButton.setForeground(Color.BLACK);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); 
        registerButton.setBackground(new Color(100, 150, 255));
        registerButton.setForeground(Color.BLACK);
        registerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Añadiendo componentes al panel
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(registerButton);

        // Añadir el panel a la ventana
        add(panel);

        // Acciones de los botones
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                // Lógica de inicio de sesión
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
                } else {
                    // Aquí es donde haremos la verificación de la base de datos
                    JOptionPane.showMessageDialog(null, "Iniciando sesión con el usuario: " + username);
                    dispose();
                    ManagerWelcome v = new ManagerWelcome();
                    v.setVisible(true);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para el registro de un nuevo usuario
                JOptionPane.showMessageDialog(null, "Redirigiendo al registro de nuevo usuario...");
                // Aquí abriremos otra ventana o un formulario para crear una cuenta nueva.
                dispose();
                ManagerRegister v = new ManagerRegister();
                v.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        	ManagerLogin frame = new ManagerLogin();
            frame.setVisible(true);
        });
    }
} 