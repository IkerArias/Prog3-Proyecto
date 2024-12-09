

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import domain.Jugador;


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
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerWelcome wel = new ManagerWelcome();
                wel.setVisible(true);
            }
        });
        

     // Paneles
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBackground(new Color(235, 235, 235)); 
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        

        // JTextField de búsqueda
        textField = new JTextField();
        textField.setFont(new Font("Courier", Font.BOLD, 24));
        textField.setPreferredSize(new Dimension(600, 50));
        textField.setBackground(new Color(245, 245, 245)); 
        textField.setForeground(new Color(50, 50, 50)); 
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2)); 
        textField.setCaretColor(Color.GRAY);

        // Botones
        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 16));
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setBackground(new Color(255, 239, 108));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnBuscar.setPreferredSize(new Dimension(120, 50));
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setOpaque(true);
        
        Color softColor = new Color(0, 51, 102); 
        
        btnFiltro = new JButton("Filtrar");
        btnFiltro.setFont(new Font("Arial", Font.BOLD, 16));
        btnFiltro.setForeground(Color.WHITE);
        btnFiltro.setBackground(softColor); 
        btnFiltro.setFocusPainted(false);
        btnFiltro.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnFiltro.setPreferredSize(new Dimension(120, 50));
        btnFiltro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFiltro.setOpaque(true);
        
        btnAñadirJugador = new JButton("Añadir Jugador a la plantilla");
        btnAñadirJugador.setFont(new Font("Arial", Font.BOLD, 16));
        btnAñadirJugador.setForeground(Color.WHITE);
        btnAñadirJugador.setBackground(softColor); 
        btnAñadirJugador.setFocusPainted(false);
        btnAñadirJugador.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnAñadirJugador.setPreferredSize(new Dimension(220, 50)); 
        btnAñadirJugador.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAñadirJugador.setOpaque(true);
        
        btnAtras = new JButton("Atras");
        btnAtras.setFont(new Font("Arial", Font.BOLD, 16));
        btnAtras.setForeground(Color.WHITE);
        btnAtras.setBackground(softColor); 
        btnAtras.setFocusPainted(false);
        btnAtras.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnAtras.setPreferredSize(new Dimension(120, 50));
        btnAtras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAtras.setOpaque(true);
        
        add(btnBuscar);
        add(btnFiltro);
        add(btnAñadirJugador);
        add(btnAtras);  
       

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
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInferior.add(btnAtras);
        panelInferior.add(btnAñadirJugador);
        add(panelInferior,BorderLayout.SOUTH);
        
        

      //Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("resources/imagenes/logo.png"));
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
                int goles = rs.getInt("goles");
                int asistencias = rs.getInt("asistencias");
                int regates = rs.getInt("regates");
                int tarjetas_amarillas = rs.getInt("tarjetas_amarillas");
                int tarjetas_rojas = rs.getInt("tarjetas_rojas");
                int puntos = rs.getInt("puntos");
                

                // Agregar el jugador a la lista con los puntos calculados
                resultado.add(new Jugador(nombre, equipoNombre, posicion, pais, valor, puntos, goles, asistencias, regates, tarjetas_amarillas, tarjetas_rojas));

               
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
        
        comboEquipo.setFont(new Font("Arial", Font.PLAIN, 14));
        comboPosicion.setFont(new Font("Arial", Font.PLAIN, 14));
        comboEquipo.setBackground(new Color(255, 255, 255)); 
        comboPosicion.setBackground(new Color(255, 255, 255)); 
        comboEquipo.setForeground(Color.DARK_GRAY); 
        comboPosicion.setForeground(Color.DARK_GRAY);

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
                datos[i][10] = jugador.getTarjetas_rojas();
            }

            
            JTable tabla = new JTable(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            
               
            JScrollPane scrollPane = new JScrollPane(tabla);
            tabla.getTableHeader().setDefaultRenderer(headerRenderer);
        	tabla.setDefaultRenderer(Object.class, cellRenderer);
            tabla.getTableHeader().setPreferredSize(new Dimension(tabla.getPreferredSize().width, 50));
            
            //Se define la altura de las filas de la tabla de comics
            tabla.setRowHeight(30);
            
    		//Se deshabilita la reordenación de columnas
            tabla.getTableHeader().setReorderingAllowed(false);
    		//Se deshabilita el redimensionado de las columna
            tabla.getTableHeader().setResizingAllowed(false);
    		//Se definen criterios de ordenación por defecto para cada columna
            
            
            
            tabla.setAutoCreateRowSorter(true);
            
            tabla.setAutoCreateRowSorter(true);

         // Configuración del TableRowSorter
         RowSorter<? extends TableModel> sorter = tabla.getRowSorter();
         if (sorter instanceof TableRowSorter) {
             TableRowSorter<?> tableRowSorter = (TableRowSorter<?>) sorter;
             
             // Configurar comparadores personalizados
             tableRowSorter.setComparator(4, (o1, o2) -> { // Comparador para valor
                 Double valor1 = (Double) o1;
                 Double valor2 = (Double) o2;
                 return valor1.compareTo(valor2);
             });
             tableRowSorter.setComparator(5, (o1, o2) -> { // Comparador para puntos
                 Integer puntos1 = (Integer) o1;
                 Integer puntos2 = (Integer) o2;
                 return puntos1.compareTo(puntos2);
             });
             tableRowSorter.setComparator(8, (o1, o2) -> { // Comparador para regates
                 Integer regates1 = (Integer) o1;
                 Integer regates2 = (Integer) o2;
                 return regates1.compareTo(regates2);
             });

             // Ordenar inicialmente por la columna de puntos en orden descendente
             List<RowSorter.SortKey> sortKeys = new ArrayList<>();
             sortKeys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING)); // Columna 5 (Puntos)
             tableRowSorter.setSortKeys(sortKeys);
         }


            
            
            
            tabla.getColumnModel().getColumn(0).setPreferredWidth(200);  // Columna del nombre más ancha
        	tabla.getColumnModel().getColumn(1).setPreferredWidth(100);  // Columna del equipo
        	tabla.getColumnModel().getColumn(2).setPreferredWidth(170);  // Columna de la posición
        	tabla.getColumnModel().getColumn(4).setPreferredWidth(100);  // Columna del valor
        	tabla.getColumnModel().getColumn(5).setPreferredWidth(80);   // Columna de los puntos
        	tabla.getColumnModel().getColumn(6).setPreferredWidth(80);   // Columna de los goles
        	tabla.getColumnModel().getColumn(7).setPreferredWidth(100);  // Columna de las asistencias
        	tabla.getColumnModel().getColumn(8).setPreferredWidth(100);  // Columna de los regates
        	tabla.getColumnModel().getColumn(9).setPreferredWidth(120);  // Columna de las tarjetas amarillas
        	tabla.getColumnModel().getColumn(10).setPreferredWidth(120); // Columna de las tarjetas rojas
        	
        	
        	
        	
            
            
            panelResultados.setLayout(new BorderLayout());
            panelResultados.add(scrollPane, BorderLayout.CENTER);
        }

        revalidate(); 
        repaint();
    }
    
    
    
    TableCellRenderer headerRenderer = (table, value, isSelected, hasFocus, row, column) -> {
        JLabel result = new JLabel(value != null ? value.toString() : "");

        // Renderizar el encabezado de la columna
        result.setFont(new Font("Arial", Font.BOLD, 14));  // Establecer la fuente en negrita para los títulos de las columnas
        result.setHorizontalAlignment(SwingConstants.CENTER); // Alinear el texto al centro
        
        //Establecer el toolTipText
        if(value!= null) {
        	 result.setToolTipText(value.toString());
        }

        if (column == 9) {  // Columna de tarjetas amarillas
            result.setText(""); // No texto, solo la imagen
            result.setHorizontalAlignment(JLabel.CENTER);
            ImageIcon yellowIcon = new ImageIcon("resources/imagenes/amarilla.png");
            Image yellowImage = yellowIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            result.setIcon(new ImageIcon(yellowImage));
        } else if (column == 10) {  // Columna de tarjetas rojas
            result.setText(""); // No texto, solo la imagen
            result.setHorizontalAlignment(JLabel.CENTER);
            ImageIcon redIcon = new ImageIcon("resources/imagenes/roja.png");
            Image redImage = redIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            result.setIcon(new ImageIcon(redImage));
        }
        
        result.setBackground(new Color(0, 51, 102));
        result.setForeground(Color.WHITE);
        result.setOpaque(true);
        return result;
    };

    TableCellRenderer cellRenderer = (table, value, isSelected, hasFocus, row, column) -> {
        JLabel result = new JLabel(value != null ? value.toString() : "");
        
        // Verificar si es la columna de equipo
        if (column == 1) {
            String equipoNombre = (String) value;
            String imagePath = "resources/laliga/" + equipoNombre + ".png";
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            result.setIcon(new ImageIcon(image));
            result.setHorizontalAlignment(SwingConstants.CENTER);
            result.setText(""); 
            result.setToolTipText(equipoNombre);
        }
        
        
        //Establecer el toolTipText
        if(value!= null) {
       	 result.setToolTipText(value.toString());
       }

        // Establecer el estilo para las celdas
        if (column == 0) {
            result.setFont(new Font("Arial", Font.BOLD, 16));  // Nombre en negrita
            result.setHorizontalAlignment(SwingConstants.LEFT);  // Alinear el texto a la izquierda
        } else if (column == 4) {
            result.setFont(new Font("Arial", Font.BOLD, 16));  // Valor en negrita
            result.setHorizontalAlignment(SwingConstants.RIGHT);  // Alinear el valor a la derecha
        } else {
            result.setFont(new Font("Arial", Font.PLAIN, 14));  // Otras celdas con fuente normal
            result.setHorizontalAlignment(SwingConstants.CENTER);  // Alinear al centro
        }

        // Definir color de fondo alternado
        if (row % 2 == 0) {  // Fila par
            result.setBackground(new Color(240, 248, 255));  // Color claro
        } else {  // Fila impar
            result.setBackground(new Color(255, 255, 255));  // Color blanco
        }

        // Color de fondo para la selección
        if (isSelected) {
            result.setBackground(new Color(173, 216, 230));  // Color de fondo al seleccionar
        }
        
        if (column == 4) { // Si es la columna de valor
            double valor = (double) value;
            if (valor > 80) {  // Si el valor es mayor a 100 millones
                result.setFont(new Font("Arial", Font.BOLD, 16));  // Poner en negrita
                result.setForeground(Color.RED);  // Y cambiar el color a rojo
            }
            result.setHorizontalAlignment(SwingConstants.CENTER);
        }

        result.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        result.setOpaque(true);  // Necesario para que el color de fondo se vea
        return result;
    };



  

    


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
