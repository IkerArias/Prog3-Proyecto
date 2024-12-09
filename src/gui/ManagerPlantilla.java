package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.nio.channels.FileChannel;

import domain.Jugador;
import domain.UserData;

public class ManagerPlantilla extends JFrame {

    private static final long serialVersionUID = 1L;
    
	private ArrayList<Jugador> resultado;
	private double presupuestoInicial = 1000.0; // Presupuesto inicial
    private double presupuesto = presupuestoInicial;
    private JLabel lblPresupuesto;
    private JLabel lblFormacion;
    private JPanel pnlPlantilla; // Panel central para la formación
    private JComboBox<String> formationSelector; // Selector de formación
    private JButton[] playerButtons; // Botones reutilizables para los jugadores
    private JProgressBar progressBar; // Barra de progreso
    private JLabel lblJornada;
    private JComboBox<String> comboJornada;
    

    public ManagerPlantilla() {
        // Configuración básica
        setSize(700, 700);
        setTitle("Gestión de Plantilla");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(196, 238, 255)); 
        
        
        
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
            Image icono = ImageIO.read(new File("resources/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        setLayout(new BorderLayout(10, 10)); 

        // Panel superior (Presupuesto y Selector de formación)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        JPanel panelFormacion = new JPanel();
        topPanel.setOpaque(false);

        lblPresupuesto = new JLabel("Presupuesto: " + presupuesto + " M");
        lblPresupuesto.setFont(new Font("Arial", Font.BOLD, 16));
        lblPresupuesto.setForeground(new Color(70, 130, 180)); 
        
        lblFormacion = new JLabel("Formacion: ");
        lblFormacion.setFont(new Font("Arial", Font.BOLD, 16));
        lblFormacion.setForeground(new Color(70, 130, 180)); 

        formationSelector = new JComboBox<>(new String[] { 
            "4-3-3", 
            "5-3-2",
            "4-4-2",
            "4-5-1",
            "3-4-3"
        });
        formationSelector.addActionListener(this::cambiarFormacion);
        formationSelector.setFont(new Font("Arial", Font.PLAIN, 14));
        
        
        lblJornada = new JLabel("Jornada");
        lblJornada.setFont(new Font("Arial", Font.BOLD, 16));
        lblJornada.setForeground(new Color(70, 130, 180));

        comboJornada = new JComboBox<>(new String[] { 
            "1", "2", "3", "4", "5"
        });
        comboJornada.setFont(new Font("Arial", Font.PLAIN, 14));
        comboJornada.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
		        reiniciarPresupuesto();
				
			}
		});

        
        
        
        
        
        JButton btnGuardarPlantilla = new JButton("Guardar Plantilla");
        //btnGuardarPlantilla.addActionListener(this::guardarPlantilla);
        btnGuardarPlantilla.addActionListener(e -> guardarPlantilla());
        

        topPanel.add(lblPresupuesto);
        panelFormacion.add(lblFormacion);
        panelFormacion.add(formationSelector);
        panelFormacion.setBackground(new Color(196, 238, 255));
        topPanel.add(panelFormacion);
        topPanel.add(lblJornada);
        topPanel.add(comboJornada);
        topPanel.add(btnGuardarPlantilla);
 
        
        

        add(topPanel, BorderLayout.NORTH);
        

        // Panel central (Formación del equipo)
        pnlPlantilla = new JPanel();
        pnlPlantilla.setOpaque(false);
        pnlPlantilla.setBorder(new EmptyBorder(15, 15, 15, 15)); 
        add(pnlPlantilla, BorderLayout.CENTER);

        // Crear botones de jugadores
        playerButtons = new JButton[11];
        for (int i = 0; i < 11; i++) {
            playerButtons[i] = crearBotonJugador(null);
        }

     // Configurar formación inicial
        configurarFormacion("4-3-3");

        // Barra de progreso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(300, 25));
        progressBar.setVisible(false); // Se muestra solo cuando está trabajando
        JPanel progressPanel = new JPanel();
        progressPanel.setOpaque(false);
        progressPanel.add(progressBar);
        add(progressPanel, BorderLayout.SOUTH);
    }

    private JButton crearBotonJugador(String texto) {
        JButton button = new JButton(texto != null ? texto : "");
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(new Color(224, 255, 255)); 
        button.setForeground(new Color(0, 102, 102)); 
        
        return button;
    }

    private void configurarFormacion(String formacion) {
        pnlPlantilla.removeAll();

        if ("4-4-2".equals(formacion)) {
            pnlPlantilla.setLayout(new GridLayout(4, 5));

            // Primera fila: portero
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[0], "Portero"); 
            pnlPlantilla.add(playerButtons[0]);// Solo porteros
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));

            // Segunda fila: defensas
            SeleccionarJugadorPorPosicion(playerButtons[1], "Lateral izquierdo"); 
            pnlPlantilla.add(playerButtons[1]);// Lateral izquierdo
            SeleccionarJugadorPorPosicion(playerButtons[2], "Defensa central");
            pnlPlantilla.add(playerButtons[2]); // Defensa central
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[3], "Defensa central"); 
            pnlPlantilla.add(playerButtons[3]);// Defensa central
            SeleccionarJugadorPorPosicion(playerButtons[4], "Lateral izquierdo"); 
            pnlPlantilla.add(playerButtons[4]);// Lateral derecho

            // Tercera fila: mediocampistas
            SeleccionarJugadorPorPosicion(playerButtons[5], "Mediocentro"); 
            pnlPlantilla.add(playerButtons[5]);// Mediocentro
            SeleccionarJugadorPorPosicion(playerButtons[6], "Mediocentro"); 
            pnlPlantilla.add(playerButtons[6]);// Mediocentro
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[7], "Mediocentro"); 
            pnlPlantilla.add(playerButtons[7]);// Mediocentro
            SeleccionarJugadorPorPosicion(playerButtons[8], "Mediocentro"); 
            pnlPlantilla.add(playerButtons[8]);//Mediocentro

            // Cuarta fila: delanteros
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[9], "Delantero centro"); 
            pnlPlantilla.add(playerButtons[9]);// Delantero centro
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[10], "Delantero centro"); 
            pnlPlantilla.add(playerButtons[10]);// Delantero centro
            pnlPlantilla.add(new JLabel(""));
        } else if ("4-3-3".equals(formacion)) {
            pnlPlantilla.setLayout(new GridLayout(4, 5));
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[0], "Portero");
            pnlPlantilla.add(playerButtons[0]); // Portero
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));
            
            SeleccionarJugadorPorPosicion(playerButtons[1], "Lateral izquierdo"); 
            pnlPlantilla.add(playerButtons[1]); // Lateral izquierdo
            SeleccionarJugadorPorPosicion(playerButtons[2], "Defensa central"); 
            pnlPlantilla.add(playerButtons[2]); // Defensa Central
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[3], "Defensa central");
            pnlPlantilla.add(playerButtons[3]); // Defensa Central
            SeleccionarJugadorPorPosicion(playerButtons[4], "Lateral derecho"); 
            pnlPlantilla.add(playerButtons[4]); // Lateral Derecho
            

            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[5], "Mediocentro"); 
            pnlPlantilla.add(playerButtons[5]); // Mediocentro
            SeleccionarJugadorPorPosicion(playerButtons[6], "Mediocentro ofensivo"); 
            pnlPlantilla.add(playerButtons[6]); // Mediocentro ofensivo
            SeleccionarJugadorPorPosicion(playerButtons[7], "Mediocentro"); 
            pnlPlantilla.add(playerButtons[7]); // Mediocentro
            pnlPlantilla.add(new JLabel(""));
            
            SeleccionarJugadorPorPosicion(playerButtons[8], "Extremo izquierdo"); 
            pnlPlantilla.add(playerButtons[8]); // Extremo izquierdo
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[9], "Delantero centro"); 
            pnlPlantilla.add(playerButtons[9]); // Delantero Centro
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[10], "Extremo derecho"); 
            pnlPlantilla.add(playerButtons[10]); // Extremo derecho

        } else if ("5-3-2".equals(formacion)) {
            pnlPlantilla.setLayout(new GridLayout(4, 5));
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[0], "Portero");
            pnlPlantilla.add(playerButtons[0]); // Portero
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));

            SeleccionarJugadorPorPosicion(playerButtons[1], "Lateral izquierdo");
            pnlPlantilla.add(playerButtons[1]); // Lateral izquierdo
            SeleccionarJugadorPorPosicion(playerButtons[2], "Defensa central");
            pnlPlantilla.add(playerButtons[2]); // Defensa Central
            SeleccionarJugadorPorPosicion(playerButtons[3], "Defensa central");
            pnlPlantilla.add(playerButtons[3]); // Defensa Central
            SeleccionarJugadorPorPosicion(playerButtons[4], "Defensa central");
            pnlPlantilla.add(playerButtons[4]); // Defensa Central
            SeleccionarJugadorPorPosicion(playerButtons[5], "Lateral derecho");
            pnlPlantilla.add(playerButtons[5]); // Lateral Derecho
            

            pnlPlantilla.add(new JLabel("")); 
            SeleccionarJugadorPorPosicion(playerButtons[6], "Mediocentro");
            pnlPlantilla.add(playerButtons[6]); // Mediocentro
            SeleccionarJugadorPorPosicion(playerButtons[7], "Mediocentro ofensivo");
            pnlPlantilla.add(playerButtons[7]); // Mediocentro ofensivo
            SeleccionarJugadorPorPosicion(playerButtons[8], "Mediocentro");
            pnlPlantilla.add(playerButtons[8]); // Mediocentro
            pnlPlantilla.add(new JLabel(""));

            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[9], "Delantero centro");
            pnlPlantilla.add(playerButtons[9]); // Delantero Centro
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[10], "Delantero centro");
            pnlPlantilla.add(playerButtons[10]); // Delantero Centro
            pnlPlantilla.add(new JLabel(""));
            
        } else if ("4-5-1".equals(formacion)) {
        	pnlPlantilla.setLayout(new GridLayout(4, 5));
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[0], "Portero");
            pnlPlantilla.add(playerButtons[0]); // Portero
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));

            SeleccionarJugadorPorPosicion(playerButtons[1], "Lateral izquierdo");
            pnlPlantilla.add(playerButtons[1]); // Lateral izquierdo
            SeleccionarJugadorPorPosicion(playerButtons[2], "Defensa central");
            pnlPlantilla.add(playerButtons[2]); // Defensa Central
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[3], "Defensa central");
            pnlPlantilla.add(playerButtons[3]); // Defensa Central
            SeleccionarJugadorPorPosicion(playerButtons[4], "Lateral derecho");
            pnlPlantilla.add(playerButtons[4]); // Lateral Derecho
            

            SeleccionarJugadorPorPosicion(playerButtons[5], "Extremo izquierdo");
            pnlPlantilla.add(playerButtons[5]); // Extremo izquierdo
            SeleccionarJugadorPorPosicion(playerButtons[6], "Mediocentro");
            pnlPlantilla.add(playerButtons[6]); // Mediocentro 
            SeleccionarJugadorPorPosicion(playerButtons[7], "Mediocentro ofensivo");
            pnlPlantilla.add(playerButtons[7]); // Mediocentro ofensivo 
            SeleccionarJugadorPorPosicion(playerButtons[8], "Mediocentro");
            pnlPlantilla.add(playerButtons[8]); // Mediocentro
            SeleccionarJugadorPorPosicion(playerButtons[9], "Extremo derecho");
            pnlPlantilla.add(playerButtons[9]); // Extremo derecho

            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[10], "Delantero centro");
            pnlPlantilla.add(playerButtons[10]); // Delantero Centro
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));
            
        } else if ("3-4-3".equals(formacion)) {
            pnlPlantilla.setLayout(new GridLayout(4, 5));
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[0], "Portero");
            pnlPlantilla.add(playerButtons[0]); // Portero
            pnlPlantilla.add(new JLabel(""));
            pnlPlantilla.add(new JLabel(""));

            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[1], "Defensa central");
            pnlPlantilla.add(playerButtons[1]); // Defensa Central
            SeleccionarJugadorPorPosicion(playerButtons[2], "Defensa central");
            pnlPlantilla.add(playerButtons[2]); // Defensa Central
            SeleccionarJugadorPorPosicion(playerButtons[3], "Defensa central");
            pnlPlantilla.add(playerButtons[3]); // Defensa Central
            pnlPlantilla.add(new JLabel(""));
            

            SeleccionarJugadorPorPosicion(playerButtons[4], "Mediocentro");
            pnlPlantilla.add(playerButtons[4]); //Mediocentro
            SeleccionarJugadorPorPosicion(playerButtons[5], "Mediocentro");
            pnlPlantilla.add(playerButtons[5]); // Mediocentro 
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[6], "Mediocentro");
            pnlPlantilla.add(playerButtons[6]); // Mediocentro
            SeleccionarJugadorPorPosicion(playerButtons[7], "Mediocentro");
            pnlPlantilla.add(playerButtons[7]); // Mediocentro

            SeleccionarJugadorPorPosicion(playerButtons[8], "Extremo izquierdo");
            pnlPlantilla.add(playerButtons[8]); // Extremo izquierdo
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[9], "Delantero centro");
            pnlPlantilla.add(playerButtons[9]); // Delantero Centro
            pnlPlantilla.add(new JLabel(""));
            SeleccionarJugadorPorPosicion(playerButtons[10], "Extremo derecho");
            pnlPlantilla.add(playerButtons[10]); // Extremo derecho
            
         
            for (int i = 11; i < playerButtons.length; i++) {
                SeleccionarJugadorPorPosicion(playerButtons[i], "Banquillo");
                pnlPlantilla.add(playerButtons[i]);
            }
        }
 
        pnlPlantilla.revalidate();
        pnlPlantilla.repaint();
    }
   

    private void cambiarFormacion(ActionEvent e) {
    	reiniciarBotonesFormacion();
        reiniciarPresupuesto();
        // Mostrar barra de progreso
        progressBar.setVisible(true);

        // Crear un SwingWorker para cambiar la formación
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                String seleccion = (String) formationSelector.getSelectedItem();
                Thread.sleep(100); // Simula un breve retraso para mostrar la barra de progreso

                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(30); // Simula un pequeño retraso
                    publish(i); // Actualiza el progreso
                }

                Thread.sleep(1500); // Espera de 1,5 segundos (simulación)
                configurarFormacion(seleccion);
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                // Actualiza el progreso de la barra
                progressBar.setValue(chunks.get(chunks.size() - 1));
            }

            @Override
            protected void done() {
                progressBar.setVisible(false); // Ocultar la barra después de completar
            }
        };
        worker.execute();
    }
    private void reiniciarPresupuesto() {
        presupuesto = presupuestoInicial;
        actualizarPresupuesto();
    }
    
    private void reiniciarBotonesFormacion() {
        for (JButton boton : playerButtons) {
        	boton.setText(""); 
        	boton.setEnabled(true); 
        }
    }


    private void SeleccionarJugadorPorPosicion(JButton button, String posicion) {
        button.setText(posicion); // Establece el texto predeterminado como el nombre de la posición
        button.addActionListener(e -> {
            buscarJugador(posicion); // Filtrar jugadores por rol específico
            if (!resultado.isEmpty()) {
                JList<String> playerList = new JList<>(
                    resultado.stream()
                             .map(j -> j.getNombre() + " - $" + j.getValor())
                             .toArray(String[]::new)
                );
                int option = JOptionPane.showConfirmDialog(this, 
                    new JScrollPane(playerList), 
                    "Selecciona un jugador (" + posicion + ")", 
                    JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    int selectedIndex = playerList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        Jugador selectedPlayer = resultado.get(selectedIndex);

                        if (presupuesto >= selectedPlayer.getValor()) {
                            presupuesto -= selectedPlayer.getValor();
                            button.setText(selectedPlayer.getNombre());
                            actualizarPresupuesto();
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Presupuesto insuficiente para este jugador.", 
                                "Advertencia", 
                                JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se encontraron jugadores para la posición: " + posicion, 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    private void guardarPlantilla() {
        String jornadaSeleccionada = (String) comboJornada.getSelectedItem();
        if (jornadaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona una jornada para guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreUsuario = UserData.getUsername(); 

        // Obtener la lista de jugadores seleccionados
        ArrayList<String> jugadoresSeleccionados = new ArrayList<>();
        for (JButton boton : playerButtons) {
            if (boton.getText() != null && !boton.getText().isEmpty()) {
                jugadoresSeleccionados.add(boton.getText());
            }
        }

        // Guardar la plantilla en el archivo CSV
        guardarPlantillaEnCSV(nombreUsuario, jornadaSeleccionada, jugadoresSeleccionados);

        JOptionPane.showMessageDialog(this, "Plantilla guardada para la jornada: " + jornadaSeleccionada, "Guardado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void guardarPlantillaEnCSV(String nombreUsuario, String jornada, ArrayList<String> jugadores) {
        String filePath = "resources/data/guardarPlantillas.csv"; 

        try (FileWriter fw = new FileWriter(filePath, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {

            // Comprobar si el archivo está vacío para añadir el encabezado
            if (new File(filePath).length() == 0) {
                bw.write("usuario,jornada,jugador1,jugador2,jugador3,jugador4,jugador5,jugador6,jugador7,jugador8,jugador9,jugador10,jugador11");
                bw.newLine();
            }

            // Crear una copia de la lista de jugadores y unirlos en una cadena
            StringBuilder jugadoresString = new StringBuilder();
            for (String jugador : new ArrayList<>(jugadores)) {
                jugadoresString.append(jugador).append(",");
            }
            // Eliminar la última coma si es necesario
            if (jugadoresString.length() > 0) {
                jugadoresString.deleteCharAt(jugadoresString.length() - 1);
            }

            // Construir la línea completa
            String linea = nombreUsuario + "," + jornada + "," + jugadoresString;
            bw.write(linea);
            bw.newLine();

        } catch (IOException e) {
            System.err.println("Error al guardar la plantilla: " + e.getMessage());
        }
    }
    

    // Método auxiliar para buscar la línea del usuario en el archivo CSV
    private String buscarLineaUsuario(String filePath, String nombreUsuario) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[0].equals(nombreUsuario)) {
                    return linea;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return null;
    }

    
    






    private void actualizarPresupuesto() {
        lblPresupuesto.setText("Presupuesto: $" + String.format("%.2f", presupuesto));
    }

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
                resultado.add(new Jugador(nombre, equipoNombre, posicion, pais, valor, puntos, goles, asistencias, regates, tarjetas_amarillas, tarjetas_rojas));
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