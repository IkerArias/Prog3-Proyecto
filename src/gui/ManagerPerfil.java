package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ManagerPerfil extends JFrame {


	private static final long serialVersionUID = 1L;

	public ManagerPerfil() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 400);
        setTitle("MI PERFIL");
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal para organizar los componentes
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Etiqueta de título
        JLabel labelTitulo = new JLabel("MI PERFIL", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(labelTitulo, BorderLayout.NORTH);
        
        // Panel central para los campos de información
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new GridLayout(3, 4, 10, 10));
        
        // Cargar datos del usuario desde el archivo CSV
        /*String[] userData = cargarDatosUsuario(username);
        if (userData == null) {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }*/

        // Etiquetas y datos del usuario
        JLabel labelNombre = new JLabel("Nombre:");        
        JLabel labelEmail = new JLabel("Email:");        
        JLabel labelNumero = new JLabel("Número:");        
        JLabel labelAddress = new JLabel("Dirección:");        
        JLabel labelPostal = new JLabel("Código Postal:");
        JLabel labelEquipo = new JLabel("Equipo:");
        
        /*JLabel fieldNombre = new JLabel(userData[0]);
        JLabel fieldEmail = new JLabel(userData[2]);
        JLabel fieldNumero = new JLabel(userData[3]);
        JLabel fieldAddress = new JLabel(userData[4]);
        JLabel fieldPostal = new JLabel(userData[5]);
        JLabel fieldEquipo = new JLabel(userData[6]);*/
        
        JLabel fieldNombre = new JLabel("nombre");
        JLabel fieldEmail = new JLabel("email");
        JLabel fieldNumero = new JLabel("número");
        JLabel fieldAddress = new JLabel("dirección");
        JLabel fieldPostal = new JLabel("código postal");
        JLabel fieldEquipo = new JLabel("equipo");
        
        // Agregar componentes al panel central
        panelCentro.add(labelNombre);
        panelCentro.add(fieldNombre);
        panelCentro.add(labelAddress);
        panelCentro.add(fieldAddress);

        panelCentro.add(labelEmail);
        panelCentro.add(fieldEmail);
        panelCentro.add(labelPostal);
        panelCentro.add(fieldPostal);

        panelCentro.add(labelNumero);
        panelCentro.add(fieldNumero);
        panelCentro.add(labelEquipo);
        panelCentro.add(fieldEquipo);

        mainPanel.add(panelCentro, BorderLayout.CENTER);
        
        // Panel inferior para los botones
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout());

        // Botón de Configuración (izquierda)
        JButton btnConfiguracion = new JButton();
        ImageIcon icono2 = new ImageIcon(getClass().getResource("/imagenes/avatar-de-usuario.png"));
        Image imagen2 = icono2.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon iconoConf = new ImageIcon(imagen2);
        btnConfiguracion.setIcon(iconoConf);
        btnConfiguracion.setPreferredSize(new Dimension(50, 50));

        // Quitar el borde y el fondo del botón de configuración
        btnConfiguracion.setFocusPainted(false);
        btnConfiguracion.setContentAreaFilled(false);
        btnConfiguracion.setBorderPainted(false);

        // Acción para el botón de Configuración
        btnConfiguracion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManagerConfig conf = new ManagerConfig();
                conf.setVisible(true);
            }
        });
        
        // Botón Atrás
        JButton btnAtras = new JButton("Atrás");
        btnAtras.addActionListener(e -> {
            dispose();
            ManagerWelcome wel = new ManagerWelcome();
            wel.setVisible(true);
        });

        // Botón Cerrar Sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            ManagerLogin login = new ManagerLogin(); // Abre la ventana de login
            login.setVisible(true);
        });

        // Botón de Notificaciones (derecha)
        JButton btnNotificaciones = new JButton();
        // Cargar el icono de notificación
        ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/icono_notif_transparent.png"));
        Image imagen = icono.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon iconoRed = new ImageIcon(imagen);
        btnNotificaciones.setIcon(iconoRed);
        btnNotificaciones.setPreferredSize(new Dimension(50, 50));

        // Quitar el borde y el fondo del botón de notificaciones
        btnNotificaciones.setFocusPainted(false);
        btnNotificaciones.setContentAreaFilled(false);
        btnNotificaciones.setBorderPainted(false);

        // Acción para el botón de Notificaciones
        btnNotificaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManagerNotif notif = new ManagerNotif();
                notif.setVisible(true);
            }
        });

        // Añadir los botones al panel inferior
        panelInferior.add(btnConfiguracion); 
        panelInferior.add(btnAtras);
        panelInferior.add(btnCerrarSesion);
        panelInferior.add(btnNotificaciones); 

        mainPanel.add(panelInferior, BorderLayout.SOUTH);
        
        // Añadir el panel principal al JFrame
        add(mainPanel);
        
        
        // Configurar la acción de cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerWelcome wel = new ManagerWelcome();
                wel.setVisible(true);
            }
        });
    }
	
	private String[] cargarDatosUsuario(String username) {
        String filePath = "usuarios.csv"; // Ruta del archivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos.length >= 6 && datos[0].equals(username)) { // Comparar por username
                    return datos;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de usuarios", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; // Usuario no encontrado
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerPerfil frame = new ManagerPerfil();
            frame.setVisible(true);
        });
    }
}
