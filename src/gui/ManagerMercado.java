

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import basicas.Jugador;


public class ManagerMercado extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPanel panelResultados;
    private ArrayList<Jugador> resultado;
    private String equipoFiltro = "";
    private String posicionFiltro = "";
    private JButton btnAñadirJugador;
    private JButton btnBuscar;
    private JButton btnFiltro;
    private JButton btnAtras;
    private JPanel panelInferior;
    private ArrayList<String> listaEquipos;
    

    public ManagerMercado() {
        // Configuración de la ventana principal
        setSize(1100, 750);
        setTitle("Mercado");
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        

        // Paneles
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        
        
        
        
        

        // JTextField de búsqueda
        textField = new JTextField();
        textField.setFont(new Font("Courier", Font.BOLD, 24));
        textField.setPreferredSize(new java.awt.Dimension(600, 50));

        // Botones
        btnBuscar = new JButton("Buscar");
        btnFiltro = new JButton("Filtrar");
        btnAñadirJugador = new JButton("Añadir Jugador a la plantilla");
        btnAtras = new JButton("Atras");
        
        configurarBotones(btnBuscar);
        configurarBotones(btnFiltro);
        configurarBotones(btnAñadirJugador);
        configurarBotones(btnAtras);  
       

        // Configuración del panel superior
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panelSuperior.add(textField, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        panelSuperior.add(btnFiltro, gbc);

        gbc.gridx = 2;
        panelSuperior.add(btnBuscar, gbc);
        
        gbc.gridx = 3;
        panelSuperior.add(btnAñadirJugador,gbc);
        
        gbc.gridx = 4;
        panelSuperior.add(btnAtras);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel de resultados
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        add(new JScrollPane(panelResultados), BorderLayout.CENTER);
        
        
        //Panel inferior
        panelInferior = new JPanel(new GridLayout(1,2,10,10));
        panelInferior.add(btnAtras);
        panelInferior.add(btnAñadirJugador);
        add(panelInferior,BorderLayout.SOUTH);
        panelInferior.setBackground(new Color(196, 238, 255));
        
        listaEquipos = new ArrayList<String>();
		listaEquipos.add("Athletic Club De Bilbao");
		listaEquipos.add("FC Barcelona");
		listaEquipos.add("Real Madrid");
		listaEquipos.add("Atlético de Madrid");
		listaEquipos.add("Real Sociedad");
		listaEquipos.add("Real Betis");
		listaEquipos.add("Sevilla FC");
		listaEquipos.add("Valencia CF");
		listaEquipos.add("Villarreal CF");
		listaEquipos.add("Celta de Vigo");
		listaEquipos.add("Getafe CF");
		listaEquipos.add("Rayo Vallecano");
		listaEquipos.add("Athletic Club De Bilbao");
		listaEquipos.add("Athletic Club De Bilbao");
		listaEquipos.add("Athletic Club De Bilbao");
		listaEquipos.add("Athletic Club De Bilbao");
		listaEquipos.add("Athletic Club De Bilbao");
		listaEquipos.add("Athletic Club De Bilbao");
		
        
        
      //Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("src/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        // Eventos
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarJugador(textField.getText());
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarJugador(textField.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarJugador(textField.getText());
            }
        });

        btnBuscar.addActionListener(e -> {
            String textoBusqueda = textField.getText(); // Obtener el texto del JTextField
            buscarJugadorConFiltros(textoBusqueda);     // Llamar al nuevo método con filtros
        });


        btnFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFiltros();
            }
        });
        
         btnAtras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				ManagerWelcome w = new ManagerWelcome();
				w.setVisible(true);
				
				
				
			}});
        
    
    
    }
    
    

 

    // Método para buscar jugadores con filtros aplicados
    private void buscarJugador(String textoBuscar) {
        resultado = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:futbol_fantasy.db");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT j.nombre, j.posicion, e.nombre AS equipo_nombre, j.pais, j.valor,j.puntos,j.goles,j.asistencias,j.regates,j.tarjetas_amarillas,j.tarjetas_rojas " +
                     "FROM Jugadores j " +
                     "JOIN Equipos e ON j.equipo_id = e.id " +
                     "WHERE LOWER(j.nombre) LIKE ?")) {

            stmt.setString(1, textoBuscar.toLowerCase() + "%"); 

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String equipoNombre = rs.getString("equipo_nombre");
                String posicion = rs.getString("posicion");
                String pais = rs.getString("pais");
                double valor = rs.getDouble("valor");
                int puntos = rs.getInt("puntos");
                int goles = rs.getInt("goles");
                int asistencias = rs.getInt("asistencias");
                int regates = rs.getInt("regates");
                int tarjetas_amarillas = rs.getInt("tarjetas_amarillas");
                int tarjetas_rojas = rs.getInt("tarjetas_rojas");
                
                resultado.add(new Jugador(nombre, equipoNombre, posicion, pais, valor,puntos,goles,asistencias,regates,tarjetas_amarillas,tarjetas_rojas));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mostrarResultados();
    }

    
 // Método para buscar jugadores con filtros aplicados
    private void buscarJugadorConFiltros(String nombreJugador) {
        resultado = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT j.nombre, j.posicion, e.nombre AS equipo_nombre, j.pais, j.valor,j.puntos,j.goles,j.asistencias,j.regates,j.tarjetas_amarillas,j.tarjetas_rojas  " +
            "FROM Jugadores j " +
            "JOIN Equipos e ON j.equipo_id = e.id " +
            "WHERE 1=1"
        );

        if (nombreJugador != null && !nombreJugador.isEmpty()) {
            query.append(" AND LOWER(j.nombre) LIKE ?");
        }
        if (equipoFiltro != null && !equipoFiltro.isEmpty() && !equipoFiltro.equals("-1")) {
            query.append(" AND e.id = ?");
        }
        if (posicionFiltro != null && !posicionFiltro.isEmpty() && !posicionFiltro.equals("Seleccione una posicion:")) {
            query.append(" AND j.posicion = ?");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:futbol_fantasy.db");
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            if (nombreJugador != null && !nombreJugador.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + nombreJugador.toLowerCase() + "%");
            }
            if (equipoFiltro != null && !equipoFiltro.isEmpty() && !equipoFiltro.equals("-1")) {
                pstmt.setInt(paramIndex++, Integer.parseInt(equipoFiltro));
            }
            if (posicionFiltro != null && !posicionFiltro.isEmpty() && !posicionFiltro.equals("Seleccione una posicion:")) {
                pstmt.setString(paramIndex++, posicionFiltro);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	  String nombre = rs.getString("nombre");
                  String equipoNombre = rs.getString("equipo_nombre");
                  String posicion = rs.getString("posicion");
                  String pais = rs.getString("pais");
                  double valor = rs.getDouble("valor");
                  int puntos = rs.getInt("puntos");
                  int goles = rs.getInt("goles");
                  int asistencias = rs.getInt("asistencias");
                  int regates = rs.getInt("regates");
                  int tarjetas_amarillas = rs.getInt("tarjetas_amarillas");
                  int tarjetas_rojas = rs.getInt("tarjetas_rojas");
                  resultado.add(new Jugador(nombre, equipoNombre, posicion, pais, valor,puntos,goles,asistencias,regates,tarjetas_amarillas,tarjetas_rojas));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mostrarResultados();
    }


    
 // Método para mostrar el diálogo de filtros, cargando dinámicamente los equipos desde la BD
    private void mostrarFiltros() {
        JComboBox<ComboItem> comboEquipo = new JComboBox<>();
        JComboBox<String> comboPosicion = new JComboBox<>();

        // Cargar equipos desde la base de datos y añadirlos al comboBox
        comboEquipo.addItem(new ComboItem("Seleccione un equipo:", -1));

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:futbol_fantasy.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nombre FROM Equipos")) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombreEquipo = rs.getString("nombre");

                // Aquí se agrega el nombre del equipo al JComboBox, y podemos almacenar el ID en el "Item"
                comboEquipo.addItem(new ComboItem(nombreEquipo, id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Opciones estáticas de posición
        comboPosicion.addItem("Seleccione una posicion:");
        comboPosicion.addItem("Portero");
        comboPosicion.addItem("Defensa central");
        comboPosicion.addItem("Lateral derecho");
        comboPosicion.addItem("Lateral izquierdo");
        comboPosicion.addItem("Mediocentro ofensivo");
        comboPosicion.addItem("Extremo izquierdo");
        comboPosicion.addItem("Extremo derecho");
        comboPosicion.addItem("Delantero centro");

        // Mostrar un cuadro de diálogo con los JComboBox de filtros
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Equipo:"), gbc);
        gbc.gridx = 1;
        panel.add(comboEquipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Posición:"), gbc);
        gbc.gridx = 1;
        panel.add(comboPosicion, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel, "Seleccione los filtros", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Guardar la selección de equipo y posición
            ComboItem selectedEquipo = (ComboItem) comboEquipo.getSelectedItem();
            String selectedPosicion = (String) comboPosicion.getSelectedItem();
            
            // Guardar los filtros
            equipoFiltro = selectedEquipo != null && selectedEquipo.getId() != -1 ? String.valueOf(selectedEquipo.getId()) : "";
            posicionFiltro = selectedPosicion != null && !selectedPosicion.equals("Seleccione una posicion:") ? selectedPosicion : "";

            buscarJugador(textField.getText()); 
        }
    }
    
    private void configurarBotones(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        
        boton.setForeground(Color.black);
        boton.setFocusPainted(false);
        
        boton.setBackground(Color.LIGHT_GRAY);
        boton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
    }
    
    private void mostrarResultados() {
        panelResultados.removeAll(); 

        if (resultado.isEmpty()) {
            panelResultados.add(new JLabel("No se encontraron jugadores."));
        } else {
            
            String[] columnas = {"Nombre", "Equipo", "Posición", "País", "Valor", "Puntos", "Goles", "Asistencias", "Regates", "Tarjetas Amarillas", "Tarjetas Rojas"};
            Object[][] datos = new Object[resultado.size()][columnas.length];  
            
            
            
            for (int i = 0; i < resultado.size(); i++) {
                Jugador jugador = resultado.get(i);
                datos[i][0] = jugador.getNombre();
                datos[i][1] = jugador.getEquipoNombre();
                datos[i][2] = jugador.getPosicion();
                datos[i][3] = jugador.getPais();
                datos[i][4] = jugador.getValor();
                datos[i][5] = jugador.getPuntos();
                datos[i][6] = jugador.getGoles();
                datos[i][7] = jugador.getAsistencias();
                datos[i][8] = jugador.getRegates();
                datos[i][9] = jugador.getTarjetas_amarillas();
                datos[i][10] = jugador.getTarjetas_amarillas();
            }

            
            JTable tabla = new JTable(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            tabla.getTableHeader().setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setIconTextGap(5);
                label.setText(value.toString());
              
                
                if (value != null) {
                    label.setToolTipText(value.toString());
                } else {
                    label.setToolTipText(null);
                }

                
                ImageIcon icon = null;
                switch (column) {
                    case 4: 
                        icon = cambiarTamañoImagen("src/imagenes/euro.jpg", 20, 20);
                        break;
                    case 6: 
                        icon = cambiarTamañoImagen("src/imagenes/gol.jfif", 20, 20);
                        break;
                    case 7: 
                        icon = cambiarTamañoImagen("src/imagenes/asis.png", 20, 20);
                        break;
                    case 9: 
                        icon = cambiarTamañoImagen("src/imagenes/amarilla.jfif", 20, 20);
                        break;
                    case 10: 
                        icon = cambiarTamañoImagen("src/imagenes/roja.jfif", 20, 20);
                        break;
                }

                if (icon != null) {
                    label.setIcon(icon);
                }

                label.setHorizontalTextPosition(SwingConstants.RIGHT);
                label.setVerticalTextPosition(SwingConstants.CENTER);

                label.setOpaque(true);
                label.setBackground(Color.LIGHT_GRAY);
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                return label;
            });

             
                
             
                
        		
        		Map<String, ImageIcon> mapaEquipos = new HashMap<>();

        		
        		for (String equipo : listaEquipos) {
        		    try {
        		        ImageIcon icon = new ImageIcon("src/imagenesEscudos.laliga/" + equipo + ".png");
        		        mapaEquipos.put(equipo, icon);
        		    } catch (Exception e) {
        		        System.err.println("No se encontró imagen para el equipo: " + equipo);
        		    }
        		}

        		// Renderizado personalizado para las celdas de la tabla
        		TableCellRenderer cellRenderer = (table, value, isSelected, hasFocus, row, column) -> {
        		    JLabel label = new JLabel();
        		    label.setHorizontalAlignment(SwingConstants.CENTER);
        		    if (value != null) {
                        label.setToolTipText(value.toString());
                    }
        		    		   
        		            		   
        		    if (column == 1) { 
        		        String equipo = (String) value;
        		        label.setText(equipo);
        		        ImageIcon icono = mapaEquipos.get(equipo);
        		        if (icono != null) {
        		            label.setIcon(icono);
        		        }
        		    } else {
        		       
        		        label.setText(value != null ? value.toString() : "");
        		    }

        		    
        		    label.setOpaque(true);
        		    
        		    label.setForeground(Color.BLACK);

        		    return label;
        		};

        		// Asignar el renderizado personalizado a todas las celdas de la tabla
        		tabla.setDefaultRenderer(Object.class, cellRenderer);

                

                
                JScrollPane scrollPane = new JScrollPane(tabla);
            

            
         

            
            panelResultados.setLayout(new BorderLayout());
            panelResultados.add(scrollPane, BorderLayout.CENTER);
        }

        revalidate(); 
        repaint();
    }
    
    private ImageIcon cambiarTamañoImagen(String ruta, int ancho, int alto) {
        try {
            Image imagenOriginal = ImageIO.read(new File(ruta));
            Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenEscalada);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + ruta);
            return null;
        }
    }





    // Clase interna ComboItem para almacenar nombres y IDs en el JComboBox
    private class ComboItem {
        private String name;
        private int id;

        public ComboItem(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerMercado frame = new ManagerMercado();
            frame.setVisible(true);
        });
    }
}
