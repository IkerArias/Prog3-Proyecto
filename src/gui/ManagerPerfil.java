package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ManagerPerfil extends JFrame {


	private static final long serialVersionUID = 1L;

	public ManagerPerfil() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(300, 400); // Ajuste del tamaño de la ventana
        setTitle("Perfil");
        setLocationRelativeTo(null);
        setResizable(false);

        // Etiqueta del perfil
        JLabel labelTitulo = new JLabel("Perfil de Usuario", JLabel.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));

        // Crear un panel para los botones en la parte inferior
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BorderLayout());

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
        panelInferior.add(btnConfiguracion, BorderLayout.WEST); // Alinear a la izquierda
        panelInferior.add(btnNotificaciones, BorderLayout.EAST); // Alinear a la derecha

        // Configurar la acción de cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerWelcome wel = new ManagerWelcome();
                wel.setVisible(true);
            }
        });

        // Añadir componentes al JFrame
        setLayout(new BorderLayout());
        add(labelTitulo, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.SOUTH); // Añadir el panel inferior en la parte inferior
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerPerfil frame = new ManagerPerfil();
            frame.setVisible(true);
        });
    }
}
