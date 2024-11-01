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

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarJugador(textField.getText());
            }
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

    // Método para mostrar el diálogo de filtros
    private void mostrarFiltros() {
        JTextField equipoField = new JTextField();
        JTextField posicionField = new JTextField();

        Object[] message = {
            "Equipo:", equipoField,
            "Posición:", posicionField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Filtrar Jugadores", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String equipoFiltro = equipoField.getText().trim().toLowerCase();
            String posicionFiltro = posicionField.getText().trim().toLowerCase();
            resultado = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:futbol_fantasy.db");
                 PreparedStatement stmt = conn.prepareStatement(
                     "SELECT jugadores.nombre, equipos.nombre_equipo, jugadores.posicion, jugadores.edad " +
                     "FROM jugadores " +
                     "JOIN equipos ON jugadores.equipo_id = equipos.equipo_id " +
                     "WHERE LOWER(equipos.nombre_equipo) LIKE ? AND LOWER(jugadores.posicion) LIKE ?")) {
                
                stmt.setString(1, "%" + equipoFiltro + "%");
                stmt.setString(2, "%" + posicionFiltro + "%");

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                	String nombre = rs.getString("nombre");
                    int equipo = rs.getInt("equipo");
                    String posicion = rs.getString("posicion");
                    String pais = rs.getString("pais");
                    Double valor = rs.getDouble("valor");
                    resultado.add(new Jugador(nombre, equipo, posicion, pais,valor));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            mostrarResultados();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerMercado frame = new ManagerMercado();
            frame.setVisible(true);
        });
    }
}
