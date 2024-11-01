package gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class ManagerWelcome extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerWelcome() {
        // Configuración de la ventana principal
        setTitle("Fantasy Manager - Ventana Principal");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        
       
        // Panel principal con un BorderLayout y márgenes
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        panelPrincipal.setBackground(new Color(196, 238, 255));
        
        // Panel superior para el título y logo
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout());
        panelSuperior.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        panelSuperior.setBackground(new Color(196, 238, 255));
        
        
        // Añadir logotipo en el lado izquierdo
        URL imageUrl = getClass().getResource("/imagenes/logo.png");
        if (imageUrl == null) {
            System.err.println("Image not found: /imagenes/logo.png");
        } else {
            ImageIcon icono = new ImageIcon(imageUrl);
            Image image = icono.getImage(); 
            Image resizedImage = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH); 
            ImageIcon resizedIcon = new ImageIcon(resizedImage); 
            
            JLabel labelLogo = new JLabel(resizedIcon); 
            panelSuperior.add(labelLogo, BorderLayout.WEST);
        }
        
        // Añadir logotipo en el lado derecho
        URL image2Url = getClass().getResource("/imagenes/logo2.png");
        if (image2Url == null) {
            System.err.println("Image not found: /imagenes/logo2.png");
        } else {
            ImageIcon icono = new ImageIcon(image2Url);
            Image image = icono.getImage(); 
            Image resizedImage = image.getScaledInstance(70, 70, Image.SCALE_SMOOTH); 
            ImageIcon resizedIcon = new ImageIcon(resizedImage); 
            
            JLabel labelLogo = new JLabel(resizedIcon); 
            panelSuperior.add(labelLogo, BorderLayout.EAST);
        }

 
        
        // Título
        JLabel labelBienvenida = new JLabel("Liga hypertension", SwingConstants.CENTER);
        labelBienvenida.setFont(new Font("Serif", Font.BOLD, 34));
        panelSuperior.add(labelBienvenida, BorderLayout.CENTER);
        
        // Añadir el panel de encabezado al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel para el contenido central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(1, 2, 0, 0)); 
        panelCentral.setBackground(Color.WHITE);
        
        //Panel para las tablas de partidos y clasificacion
        JPanel panelPartidosClasificacion = new JPanel();
        panelPartidosClasificacion.setLayout(new GridLayout(2,1,10,0));
        panelPartidosClasificacion.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel para la tabla de partidos
        JPanel panelPartidos = new JPanel();
        panelPartidos.setBorder(new EmptyBorder(10, 10, 10, 10));  
        JLabel labelPartidos = new JLabel("Partidos", SwingConstants.CENTER);
        labelPartidos.setFont(new Font("Serif", Font.BOLD, 20));
        panelPartidos.add(labelPartidos);
        
        
        // DATOS DE EJEMPLO PARA LA TABLA
        String[] nombreColumnas = {"Equipo Local", "Equipo Visitante", "Fecha", "Hora"};
        Object[][] data = {
        		{"Athletic Club", "Villarreal", "2024-10-25", "16:00"},
        	    {"Valencia", "Rayo Vallecano", "2024-10-26", "18:00"},
        	    {"Getafe", "Real Madrid", "2024-10-26", "20:00"},
        	    {"Atlético de Madrid", "Mallorca", "2024-10-28", "16:00"},
        	    {"Leganés", "Barcelona", "2024-10-28", "18:00"},
        	    {"Alavés", "Real Sociedad", "2024-10-28", "20:00"},
        	    {"Celta de Vigo", "Granada", "2024-10-27", "14:00"},
        	    {"Osasuna", "Real Betis", "2024-10-27", "16:00"},
        	    {"Espanyol", "Elche", "2024-10-27", "18:00"},
        	    {"UD Las Palmas", "Cadiz", "2024-10-29", "20:00"},
        	    
        	    {"Real Madrid", "Celta de Vigo", "2024-11-02", "21:00"},
        	    {"Granada", "Athletic Club", "2024-11-03", "14:00"},
        	    {"Barcelona", "Osasuna", "2024-11-03", "16:00"},
        	    {"Real Betis", "Leganés", "2024-11-03", "18:00"},
        	    {"Mallorca", "Valencia", "2024-11-03", "20:00"},
        	    {"Rayo Vallecano", "Getafe", "2024-11-04", "12:00"},
        	    {"Elche", "Alavés", "2024-11-04", "14:00"},
        	    {"Villarreal", "Atlético de Madrid", "2024-11-04", "16:00"},
        	    {"Real Sociedad", "UD Las Palmas", "2024-11-04", "18:00"},
        	    {"Cadiz", "Espanyol", "2024-11-05", "21:00"}
        	    
        };
        
        // Crear la tabla de partidos no editable
        JTable tablaPartidos = new JTable(data, nombreColumnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
      //Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("src/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        //Renderizado para que las filas pares e impares se vean de diferentes colores 
        tablaPartidos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                c.setForeground(Color.BLACK);
                return c;
            }
        });
        
        
        // Set preferred size to ensure scrolling when necessary
        tablaPartidos.setPreferredScrollableViewportSize(new Dimension(450, 200)); // Adjust dimensions as needed
        tablaPartidos.setFillsViewportHeight(true); // Fill the viewport height

        JScrollPane tableScrollPanePartidos = new JScrollPane(tablaPartidos);
        panelPartidos.add(tableScrollPanePartidos);
        panelPartidos.setBackground(new Color(196, 238, 255));
        
        // Panel para la tabla de clasificación
        JPanel panelClasificacion = new JPanel();
        panelClasificacion.setBorder(new EmptyBorder(10, 10, 10, 10));  
        JLabel labelClasificacion = new JLabel("Clasificación", SwingConstants.CENTER);
        labelClasificacion.setFont(new Font("Serif", Font.BOLD, 20));
        panelClasificacion.add(labelClasificacion);
        panelClasificacion.setBackground(new Color(196, 238, 255));
        
        
        // DATOS DE EJEMPLO PARA LA TABLA DE CLASIFICACIÓN
        String[] nombreColumnasClasificacion = {"Posición", "Equipo", "Puntos", "Goles"};
        Object[][] dataClasificacion = {
        		{"1", "Real Madrid", "28", "32", "12"},
        	    {"2", "Barcelona", "25", "43", "21"},
        	    {"3", "Atlético de Madrid", "23", "21", "17"},
        	    {"4", "Athletic Club", "22", "24", "20"},
        	    {"5", "Villarreal", "20", "23", "23"},
        	    {"6", "Valencia", "19", "28", "28"},
        	    {"7", "Real Betis", "18", "27", "26"},
        	    {"8", "Real Sociedad", "17", "25", "24"},
        	    {"9", "Celta de Vigo", "16", "22", "26"},
        	    {"10", "Espanyol", "15", "20", "30"},
        	    {"11", "Granada", "14", "19", "29"},
        	    {"12", "Osasuna", "13", "17", "25"},
        	    {"13", "Getafe", "12", "18", "26"},
        	    {"14", "Mallorca", "11", "15", "22"},
        	    {"15", "Elche", "10", "16", "31"},
        	    {"16", "Alavés", "9", "14", "30"},
        	    {"17", "Rayo Vallecano", "8", "12", "29"},
        	    {"18", "Cádiz", "7", "10", "34"},
        	    {"19", "UD Las Palmas", "6", "9", "33"},
        	    {"20", "Celta de Vigo", "5", "8", "32"}
        };
        
        // Crear la tabla de clasificación no editable
        JTable tablaClasificacion = new JTable(dataClasificacion, nombreColumnasClasificacion) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        //Renderizado para marcar con diferentes colores a los clasificados a las competiciones europeas
        tablaClasificacion.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row < 4) {
                    c.setBackground(new Color(167, 206, 255));
                } else if (row == 4) {
                    c.setBackground(new Color(255, 198, 167));
                } else if (row == 5) {
                    c.setBackground(new Color(175, 255, 167)); 
                } else if (row > 16) {
                    c.setBackground(new Color(255, 167, 167)); 
                } else if (row == 16) {
                	c.setBackground(new Color(255, 251, 167 ));
                } else {
                    c.setBackground(Color.WHITE);
                }
                c.setForeground(Color.BLACK);
                return c;
            }
        });
        
        tablaClasificacion.setPreferredScrollableViewportSize(new Dimension(450, 200)); 
        tablaClasificacion.setFillsViewportHeight(true);
        
        JScrollPane tableScrollPaneClasificacion = new JScrollPane(tablaClasificacion);
        panelClasificacion.add(tableScrollPaneClasificacion);
        
        panelPartidosClasificacion.add(panelPartidos);
        panelPartidosClasificacion.add(panelClasificacion);
        panelPartidosClasificacion.setBackground(new Color(196, 238, 255));
        
        // Añadir el panel de partidos y clasificación al panel central
        panelCentral.add(panelPartidosClasificacion);
        
        // Panel para noticias
        JPanel panelNoticias = new JPanel();
        panelNoticias.setLayout(new BorderLayout());
        panelNoticias.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        panelNoticias.setBackground(new Color(196, 238, 255));
        
        JLabel labelNoticias = new JLabel("Noticias", SwingConstants.CENTER);
        labelNoticias.setFont(new Font("Serif", Font.BOLD, 20));
        panelNoticias.add(labelNoticias, BorderLayout.NORTH);
        
        // Usar JTextPane para que el texto se ajuste automáticamente al ancho
        JTextPane noticiasArea = new JTextPane();
        noticiasArea.setContentType("text/html"); 
        noticiasArea.setEditable(false);
        noticiasArea.setFocusable(false);
        
        // Crear el contenido de noticias con separadores
        String noticiasHTML = "<html>" +
                "<p><strong>Bellingham es baja durante dos semanas:</strong> Jude Bellingham, la joven estrella del Real Madrid, ha sufrido una lesión en el muslo durante el partido contra el FC Barcelona. El club anunciará más detalles en los próximos días, pero los aficionados ya están preocupados por su ausencia en los partidos clave.</p>" +
                "<hr>" +  
                "<p><strong>Partido atrasado entre Osasuna y Las Palmas:</strong> El partido atrasado entre Osasuna y Las Palmas se disputará este miércoles en el Estadio El Sadar.</p>" +
                "<hr>" +  
                "<p><strong>Destitución del entrenador del Leganés:</strong> El CD Leganés ha decidido destituir a su entrenador tras una serie de resultados decepcionantes que han dejado al equipo en la parte baja de la clasificación.</p>" +
                "<hr>" +  
                "<p><strong>Haaland logra nuevo récord de goles en la Premier League:</strong> Erling Haaland ha alcanzado los 45 goles en una sola temporada, rompiendo el récord anterior. Su rendimiento sigue asombrando a los aficionados y expertos por igual.</p>" +
                "<hr>" +  
                "<p><strong>La FIFA anuncia cambios en las reglas del VAR:</strong> A partir de la próxima temporada, la FIFA implementará modificaciones en el uso del VAR para hacer el proceso más eficiente y reducir los tiempos de espera.</p>" +
                "<hr>" +  
                "<p><strong>Nuevo fichaje del Barcelona:</strong> El FC Barcelona ha confirmado el fichaje de una joven promesa brasileña, que se unirá al equipo en la próxima ventana de transferencias. Los fanáticos esperan que sea el futuro de la delantera del club.</p>" +
                "</html>";
        
        noticiasArea.setText(noticiasHTML);
        JScrollPane noticiasScrollPane = new JScrollPane(noticiasArea);
        panelNoticias.add(noticiasScrollPane, BorderLayout.CENTER);
         
        
        panelCentral.add(panelNoticias);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        
        
        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 7, 10, 0));  
        panelBotones.setBorder(new EmptyBorder(10, 10, 10, 10));  
        panelBotones.setBackground(new Color(196, 238, 255));
        
        // Crear botones
        JButton btnClasificacion = new JButton("Clasificación");
        JButton btnPlantilla = new JButton("Mi Plantilla");
        JButton btnMercado = new JButton("Mercado");
        JButton btnPerfil = new JButton("Perfil");
        
        // Ajustar tamaño y borde de los botones
        disenyiarBotones(btnClasificacion);
        disenyiarBotones(btnPlantilla);
        disenyiarBotones(btnMercado);
        disenyiarBotones(btnPerfil);
        

        // Añadir listeners a los botones
        btnClasificacion.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Accediendo a la clasificación...");
                dispose();
                ManagerClasificacion clasificacion = new ManagerClasificacion();
                clasificacion.setVisible(true);
               
            }
        });

        btnPlantilla.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Accediendo a mi plantilla...");
                dispose();
                ManagerPlantilla plantilla = new ManagerPlantilla();
                plantilla.setVisible(true);
                
            }
        });

        btnMercado.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Accediendo al mercado...");
                dispose();
                ManagerMercado mercado = new ManagerMercado();
                mercado.setVisible(true);
            }
        });

       


        btnPerfil.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Accediendo al perfil...");
                dispose();
                ManagerPerfil perfil = new ManagerPerfil();
                perfil.setVisible(true);
                
            }
        });

        // Añadir botones al panel
        panelBotones.add(btnClasificacion);
        panelBotones.add(btnPlantilla);
        panelBotones.add(btnMercado);
        panelBotones.add(btnPerfil);
      

        // Añadir el panel de botones a la ventana principal
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    // Método para implementar el diseño estándar de los botones
    private void disenyiarBotones(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(120, 40)); 
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); 
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerWelcome welcomeFrame = new ManagerWelcome();
            welcomeFrame.setVisible(true);
        });
    }
}
