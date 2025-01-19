package gui;


import javax.imageio.ImageIO;
import javax.swing.*;

import domain.UserData;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ManagerLogin extends JFrame {

    private static final long serialVersionUID = 1L;

	// Constructor de la ventana
    public ManagerLogin() {
        // Configuración de la ventana
        setTitle("Fantasy Manager - Iniciar sesión");
        setSize(450, 200); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(true); 

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Márgenes
        panel.setBackground(new Color(196, 238, 255));

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
        
        Color softColor = new Color(0, 51, 102); 
        
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(softColor); 
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        loginButton.setPreferredSize(new Dimension(120, 50));
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.setOpaque(true);
        
        registerButton.setForeground(Color.black);
        registerButton.setBackground(new Color(255, 239, 108)); 
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        registerButton.setPreferredSize(new Dimension(120, 50));
        registerButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registerButton.setOpaque(true);

        // Añadiendo componentes al panel
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(registerButton);

        // Añadir el panel a la ventana
        add(panel);
        
        //Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("resources/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        setVisible(true);
        
        addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}


        	
        	
        });
    

        // Boton inciar sesion: Validar que el usuario esta regustrado en fiuchero csv
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                // Comprobar inicio de sesión
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
                } else {
                    // Verificación de usuario
                    if (validateLogin(username, password)) {
                    	UserData.setUsername(username);
                        JOptionPane.showMessageDialog(null, "Bienvenido, " + username + "!");
                        dispose();
                        ManagerWelcome v = new ManagerWelcome();
                        v.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Inténtalo de nuevo.");
                    }
                }
            }
        });
        
        //Key listenner para el apartado de la contraseña
        passwordText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	String username = userText.getText();
                    String password = new String(passwordText.getPassword());
                    // Comprobar inicio de sesión
                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
                    } else {
                        // Verificación de usuario
                        if (validateLogin(username, password)) {
                        	UserData.setUsername(username);
                            JOptionPane.showMessageDialog(null, "Bienvenido, " + username + "!");
                            dispose();
                            ManagerWelcome v = new ManagerWelcome();
                            v.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Inténtalo de nuevo.");
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        });

        //Action listenner para registrar los datos 
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
    
    
    // Metodo para validar el login: comprueba que el ususario y la contraseña son correctos
    private boolean validateLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("resources/data/usuarios.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data[0].equals(username) && data[1].equals(password)) {
                    return true; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        	ManagerLogin frame = new ManagerLogin();
            frame.setVisible(true);
        });
    }
} 