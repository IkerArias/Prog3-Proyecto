package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import basicas.Jugador;

public class ManagerPlantilla extends JFrame {

    private ArrayList<Jugador> resultado;
    private double presupuesto = 200.0;  // Presupuesto inicial
    private JLabel lblPresupuesto;

    public ManagerPlantilla() {
        setSize(700,700);
        setTitle("Plantilla");
        setResizable(false);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerWelcome wel = new ManagerWelcome();
                wel.setVisible(true);
            }
        });
        
        // Cambiar icono de la ventana
        try {
            Image icono = ImageIO.read(new File("src/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        setLayout(new BorderLayout());
        
        JPanel presupuestoPanel = new JPanel();
        lblPresupuesto = new JLabel("Presupuesto: " + presupuesto + "M");
        presupuestoPanel.add(lblPresupuesto);
        add(presupuestoPanel, BorderLayout.NORTH);

        JPanel pnlPlantilla = new JPanel();
        pnlPlantilla.setLayout(new GridLayout(4, 5));
        add(pnlPlantilla, BorderLayout.CENTER);

        // Crear botones para cada posición
        JButton btnPortero = new JButton("Portero");
        JButton btnDefensaCentral1 = new JButton("Defensa Central");
        JButton btnDefensaCentral2 = new JButton("Defensa Central");
        JButton btnLateralDerecho = new JButton("Lateral Derecho");
        JButton btnLateralIzquierdo = new JButton("Lateral Izquierdo");
        JButton btnCentro1 = new JButton("Mediocentro");
        JButton btnCentro2 = new JButton("Mediocentro");
        JButton btnMedioOfensivo = new JButton("Mediocentro Ofensivo");
        JButton btnExtremoDerecho = new JButton("Extremo Derecho");
        JButton btnExtremoIzquierdo = new JButton("Extremo Izquierdo");
        JButton btnDelantero = new JButton("Delantero Centro");

        // Añadir listeners para abrir la lista de jugadores
        SeleccionarJugadorPorPosicion(btnPortero, "Portero");
        SeleccionarJugadorPorPosicion(btnDefensaCentral1, "Defensa Central");
        SeleccionarJugadorPorPosicion(btnDefensaCentral2, "Defensa Central");
        SeleccionarJugadorPorPosicion(btnLateralDerecho, "Lateral Derecho");
        SeleccionarJugadorPorPosicion(btnLateralIzquierdo, "Lateral Izquierdo");
        SeleccionarJugadorPorPosicion(btnCentro1, "Mediocentro");
        SeleccionarJugadorPorPosicion(btnCentro2, "Mediocentro");
        SeleccionarJugadorPorPosicion(btnMedioOfensivo, "Mediocentro Ofensivo");
        SeleccionarJugadorPorPosicion(btnExtremoDerecho, "Extremo Derecho");
        SeleccionarJugadorPorPosicion(btnExtremoIzquierdo, "Extremo Izquierdo");
        SeleccionarJugadorPorPosicion(btnDelantero, "Delantero Centro");

        // Añadir los botones a la formación (grid)
        pnlPlantilla.add(new JLabel(""));
        pnlPlantilla.add(new JLabel(""));
        pnlPlantilla.add(btnPortero);
        pnlPlantilla.add(new JLabel(""));
        pnlPlantilla.add(new JLabel(""));

        pnlPlantilla.add(btnLateralIzquierdo);
        pnlPlantilla.add(btnDefensaCentral1);
        pnlPlantilla.add(new JLabel(""));
        pnlPlantilla.add(btnDefensaCentral2);
        pnlPlantilla.add(btnLateralDerecho);

        pnlPlantilla.add(new JLabel(""));
        pnlPlantilla.add(btnCentro1);
        pnlPlantilla.add(btnMedioOfensivo);
        pnlPlantilla.add(btnCentro2);
        pnlPlantilla.add(new JLabel(""));

        pnlPlantilla.add(btnExtremoIzquierdo);
        pnlPlantilla.add(new JLabel(""));
        pnlPlantilla.add(btnDelantero);
        pnlPlantilla.add(new JLabel(""));
        pnlPlantilla.add(btnExtremoDerecho);

        // Panel para el banquillo
        JPanel benchPanel = new JPanel(new GridLayout(1, 4));
        JButton[] benchButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            benchButtons[i] = new JButton("Banquillo " + (i + 1));
            SeleccionarJugadorPorPosicion(benchButtons[i], null); 
            benchPanel.add(benchButtons[i]);
        }
        add(benchPanel, BorderLayout.SOUTH);
    }
    
    private void SeleccionarJugadorPorPosicion(JButton button, String position) {
        button.addActionListener(e -> {
            buscarJugador(position); 
            if (!resultado.isEmpty()) {
                JList<String> playerList = new JList<>(resultado.stream().map(j -> j.getNombre() + " - $" + j.getValor()).toArray(String[]::new));
                int option = JOptionPane.showConfirmDialog(this, new JScrollPane(playerList), "Selecciona un jugador", JOptionPane.OK_CANCEL_OPTION);
                
                if (option == JOptionPane.OK_OPTION) {
                    int selectedIndex = playerList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        Jugador selectedPlayer = resultado.get(selectedIndex);
                        
                        if (presupuesto >= selectedPlayer.getValor()) {
                            presupuesto -= selectedPlayer.getValor();
                            button.setText(selectedPlayer.getNombre());
                            actualizarPresupuesto();
                        } else {
                            JOptionPane.showMessageDialog(this, "Presupuesto insuficiente para este jugador.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron jugadores para la posición: " + (position == null ? "Cualquiera" : position));
            }
        });
    }

    private void actualizarPresupuesto() {
        lblPresupuesto.setText("Presupuesto: $" + presupuesto);
    }

    //Metodo reciclado de ManagerMercado
    private void buscarJugador(String textoBuscar) {
        resultado = new ArrayList<>();
        String query = "SELECT j.nombre, j.posicion, e.nombre AS equipo_nombre, j.pais, j.valor,j.puntos,j.goles,j.asistencias,j.regates,j.tarjetas_amarillas,j.tarjetas_rojas " +
                       "FROM Jugadores j " +
                       "JOIN Equipos e ON j.equipo_id = e.id ";
        
        if (textoBuscar != null) {
            query += "WHERE LOWER(j.posicion) LIKE ?";
        }
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:futbol_fantasy.db");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (textoBuscar != null) {
                stmt.setString(1, textoBuscar.toLowerCase() + "%");
            }

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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerPlantilla frame = new ManagerPlantilla();
            frame.setVisible(true);
        });
    }
}
