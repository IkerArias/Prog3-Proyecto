package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import basicas.Jugador;

public class ManagerMercado extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPanel panelResultados;
    private ArrayList<Jugador> resultado;

    private String equipoFiltro = "";
    private String posicionFiltro = "";

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
        JButton btnBuscar = new JButton("Buscar");
        JButton btnFiltro = new JButton("Filtros");

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

        add(panelSuperior, BorderLayout.NORTH);

        // Panel de resultados
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        add(new JScrollPane(panelResultados), BorderLayout.CENTER);
        
        
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerWelcome wel = new ManagerWelcome();
                wel.setVisible(true);
            }
        });
    }

    // Método para buscar jugadores con filtros aplicados
    private void buscarJugador(String textoBuscar) {
        resultado = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:futbol_fantasy.db");
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT nombre, posicion, equipo_id, pais, valor " +
                         "FROM Jugadores " + // Utiliza el nombre exacto de tu tabla
                         "WHERE LOWER(nombre) LIKE ?")) {

            stmt.setString(1, textoBuscar.toLowerCase() + "%"); // Set the wildcard at the beginning

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int equipoId = rs.getInt("equipo_id");
                String posicion = rs.getString("posicion");
                String pais = rs.getString("pais");
                double valor = rs.getDouble("valor");
                resultado.add(new Jugador(nombre, equipoId, posicion, pais, valor));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mostrarResultados();
    }
    
    private void buscarJugadorConFiltros(String nombreJugador) {
        StringBuilder query = new StringBuilder("SELECT * FROM Jugadores WHERE 1=1");
        
        
        
        // Agregar filtro por equipo si se ha seleccionado uno en el JComboBox
        if (equipoFiltro != null && !equipoFiltro.isEmpty() && !equipoFiltro.equals("-1")) {
            query.append(" AND equipo_id = ?");
        }
        
        // Agregar filtro por posición si se ha seleccionado una en el JComboBox
        if (posicionFiltro != null && !posicionFiltro.isEmpty() && !posicionFiltro.equals("Seleccione una posicion:")) {
            query.append(" AND posicion = ?");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:futbol_fantasy.db");
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            // Establecer el valor del filtro de nombre
            if (nombreJugador != null && !nombreJugador.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + nombreJugador + "%");
            }

            // Establecer el valor del filtro de equipo
            if (equipoFiltro != null && !equipoFiltro.isEmpty() && !equipoFiltro.equals("-1")) {
                pstmt.setInt(paramIndex++, Integer.parseInt(equipoFiltro));
            }

            // Establecer el valor del filtro de posición
            if (posicionFiltro != null && !posicionFiltro.isEmpty() && !posicionFiltro.equals("Seleccione una posicion:")) {
                pstmt.setString(paramIndex++, posicionFiltro);
            }

            ResultSet rs = pstmt.executeQuery();

            // Procesar los resultados de la consulta
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String posicion = rs.getString("posicion");
                int equipoId = rs.getInt("equipo_id");

                System.out.println("Jugador: " + nombre + ", Posición: " + posicion + ", Equipo ID: " + equipoId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // Método que muestra los resultados de la búsqueda en el panel de resultados
    private void mostrarResultados() {
        panelResultados.removeAll();

        if (resultado.isEmpty()) {
            panelResultados.add(new JLabel("No se encontraron jugadores."));
        } else {
            for (Jugador jugador : resultado) {
                panelResultados.add(new JLabel(jugador.toString()));
            }
        }
        revalidate();
        repaint();
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
        comboPosicion.addItem("Defensa");
        comboPosicion.addItem("Medio");
        comboPosicion.addItem("Delantero");

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

            buscarJugador(textField.getText()); // Realizar búsqueda con filtros
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
