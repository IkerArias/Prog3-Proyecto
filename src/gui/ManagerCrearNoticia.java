package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import domain.UserData;

public class ManagerCrearNoticia extends JFrame {
    private JPanel panelCentro;
    private JPanel panelOeste;
    private JPanel panelSur;
    private JPanel panelPrincipal;

    public ManagerCrearNoticia() {
        // Configuración de la ventana principal
        setTitle("Fantasy Manager - Crear Noticia");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        String username = UserData.getUsername(); 
        System.out.println(username);

        // Logo Ventana
        try {
            Image icono = ImageIO.read(new File("resources/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        // Inicializar los paneles
        panelCentro = new JPanel(new BorderLayout());
        panelSur = new JPanel(new FlowLayout());
        panelPrincipal = new JPanel(new BorderLayout());
        panelOeste = new JPanel(new FlowLayout());
        this.add(panelPrincipal);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);
        panelPrincipal.add(panelOeste, BorderLayout.WEST);

        // Crear componentes
        JComboBox<String> comboJornada = new JComboBox<>();
        for (int i = 1; i <= 38; i++) {
            comboJornada.addItem("Jornada " + i);
        }
        JTextArea area = new JTextArea("Escriba su noticia");
        JButton btnGuardar = new JButton("Guardar noticia");
        JButton btnAtras = new JButton("Atrás");

        // Añadir componentes a sus paneles
        panelCentro.add(area, BorderLayout.CENTER);
        panelOeste.add(comboJornada);
        panelSur.add(btnGuardar);
        panelSur.add(btnAtras);

        // Evento volver a ManagerWelcome
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ManagerWelcome w = new ManagerWelcome();
                w.setVisible(true);
            }
        });

        // Evento guardar noticia con hilo integrado
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jornadaSeleccionada = (String) comboJornada.getSelectedItem();
                String noticia = area.getText().trim();

                int numeroJornada = Integer.parseInt(jornadaSeleccionada.split(" ")[1]);

                if (!noticia.isEmpty() && !noticia.equals("Escriba su noticia")) {
                    new Thread(() -> {
                        try {
                            
                            Thread.sleep(5000);

                            
                            boolean success = guardarNoticia(numeroJornada, username, noticia);

                            
                            if (success) {
                                JOptionPane.showMessageDialog(null, 
                                    "Noticia guardada correctamente.", 
                                    "Éxito", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, 
                                    "Hubo un error al guardar la noticia.", 
                                    "Error", 
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (InterruptedException ex) {
                            System.out.println("El hilo fue interrumpido: " + ex.getMessage());
                        }
                    }).start();
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Debe escribir una noticia válida.", 
                        "Advertencia", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Metodo para guardar la noticia en un archivo CSV
    private boolean guardarNoticia(int numeroJornada, String username, String noticia) {
        String filePath = "resources/data/noticias_liga.csv"; 

        try (FileWriter fw = new FileWriter(filePath, true);
             PrintWriter pw = new PrintWriter(fw)) {

            
            pw.println(numeroJornada + ";" + username + ";" + noticia);
            System.out.println("Noticia guardada correctamente.");
            return true; 
        } catch (IOException ex) {
            System.out.println("Error al guardar la noticia: " + ex.getMessage());
            return false; 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerCrearNoticia noticia = new ManagerCrearNoticia();
            noticia.setVisible(true);
        });
    }
}
