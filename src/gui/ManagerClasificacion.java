package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class ManagerClasificacion extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    public ManagerClasificacion() {
        setSize(400, 400);  // Ajusta el tamaño de la ventana
        setTitle("Clasificación");
        setResizable(false);
        setLocationRelativeTo(null);
        
        String username = UserData.getUsername();  // Obtén el nombre de usuario que ha iniciado sesión
        List<Usuario> usuarios = obtenerUsuarios();  // Obtén la lista de usuarios
        
        // Ordenar los usuarios por los puntos de forma descendente
        Collections.sort(usuarios, (u1, u2) -> Integer.compare(u2.getPuntos(), u1.getPuntos()));
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(230, 240, 250)); // Fondo claro y agradable
        
        // Título de la ventana
        JLabel labelTitulo = new JLabel("Clasificación", JLabel.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(33, 150, 243)); // Azul brillante
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(labelTitulo, BorderLayout.NORTH);
        
        // Crear la tabla para mostrar usuarios y puntos
        String[] columnNames = {"Usuario", "Puntos"};
        Object[][] data = new Object[usuarios.size()][2];
        
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            data[i][0] = usuario.getUsername();
            data[i][1] = usuario.getPuntos();
        }
        
        // Crear la tabla con los datos de usuarios y puntos
        JTable tablaClasificacion = new JTable(data, columnNames);
        tablaClasificacion.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tablaClasificacion.setRowHeight(30);
        
        // Subrayar al usuario actual
        tablaClasificacion.setDefaultRenderer(Object.class, (table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString(), JLabel.CENTER);
            if (column == 0 && value.equals(username)) {
                // Subrayar al usuario actual
                label.setFont(new Font("SansSerif", Font.BOLD, 16));
                label.setForeground(new Color(33, 150, 243));  // Color del usuario actual
                label.setText("<html><u>" + value + "</u></html>");  // Subrayado
            } else {
                label.setFont(new Font("SansSerif", Font.PLAIN, 16));
                label.setForeground(Color.BLACK);
            }
            return label;
        });
        
        // Añadir la tabla al panel
        JScrollPane scrollPane = new JScrollPane(tablaClasificacion);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel para el botón de volver atrás
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAtras = new JButton("Atrás");
        btnAtras.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnAtras.addActionListener(e -> {
            dispose();
            new ManagerWelcome().setVisible(true);  // Regresar a la ventana de bienvenida
        });
        panelInferior.add(btnAtras);
        mainPanel.add(panelInferior, BorderLayout.SOUTH);
        
        // Agregar el panel principal a la ventana
        add(mainPanel);
        
        // Manejar el cierre de la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                new ManagerWelcome().setVisible(true);
            }
        });
    }
    
    // Método para obtener todos los usuarios desde el archivo usuarios.csv
    private List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String filePath = "usuarios.csv";
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos.length >= 2) {
                    String username = datos[0];  // El primer campo es el nombre de usuario
                    int puntos = 0;  // Inicialmente todos los puntos son 0
                    try {
                        puntos = Integer.parseInt(datos[1]);  // Si hay puntos, usarlos
                    } catch (NumberFormatException e) {
                        // Si no hay puntos válidos, usar 0
                    }
                    usuarios.add(new Usuario(username, puntos));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de usuarios", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return usuarios;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerClasificacion frame = new ManagerClasificacion();
            frame.setVisible(true);
        });
    }
    
    // Clase interna para representar un usuario y sus puntos
    static class Usuario {
        private String username;
        private int puntos;
        
        public Usuario(String username, int puntos) {
            this.username = username;
            this.puntos = puntos;
        }
        
        public String getUsername() {
            return username;
        }
        
        public int getPuntos() {
            return puntos;
        }
    }
}
