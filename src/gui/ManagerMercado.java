package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ManagerMercado extends JFrame{
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	
	private ArrayList<Jugador> resultado;
	
	public ManagerMercado() {
		
		
		//Ajustes de la ventana
			
		setSize(1100,750);
		setTitle("Mercado");
		setResizable(false);
		setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
		
		//Paneles 
		   	
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new GridBagLayout());
		
		//JTextField
		
		textField = new JTextField();
		textField.setFont(new Font("Courier", Font.BOLD, 24));
		textField.setPreferredSize(new java.awt.Dimension(600, 50));
		
		//Botones
		JButton btnBuscar = new JButton("Buscar");
		JButton btnFiltro = new JButton("Filtros");
		
		
		
		//Configuracion panel superior
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL; // Rellenar horizontalmente
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 0; // Fila 0
        gbc.weightx = 1.0; // Peso para que ocupe el espacio
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado
        panelSuperior.add(textField, gbc); // Añadir el JTextField al panel
        
         // Configurar GridBagConstraints para los botones
        gbc.fill = GridBagConstraints.NONE; // Sin relleno
        gbc.gridx = 1; // Columna 1
        gbc.weightx = 0; // Sin peso
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado
        panelSuperior.add(btnFiltro, gbc); // Añadir el botón Filtros

        gbc.gridx = 2; // Columna 2
        panelSuperior.add(btnBuscar, gbc); // Añadir el botón Buscar
		
		//Añadimos el panel Superior a la ventana
		
		add(panelSuperior, BorderLayout.NORTH);
		
		//Panel de resultados
		
		JPanel panelResultados = new JPanel();
		add(new JScrollPane(panelResultados), BorderLayout.CENTER);
		
		
		
		
		
		
		//Eventos
		
		
           textField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				buscarJugador(textField.getText());
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				buscarJugador(textField.getText());
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				buscarJugador(textField.getText());
				
			}
		});
           
           btnBuscar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				buscarJugador(textField.getText());
				
			}
		});
		
		
		
		btnFiltro.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				mostrarFiltros();
				
			}
		});
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Abrir la ventana de ManagerLogin cuando se cierra esta ventana
            	dispose();
                ManagerWelcome wel = new ManagerWelcome();
                wel.setVisible(true);
            }
        });

	}
	
	//Metodos
	
	
	//Metodo que se conecta a la base de datos y busca jugadores
	private void buscarJugador(String textoBuscar) {
        resultado = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:futbol_fantasy.db");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jugadores WHERE LOWER(nombre) LIKE ?")) {
            
            stmt.setString(1, "%" + textoBuscar.toLowerCase() + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String equipo = rs.getString("equipo");
                String posicion = rs.getString("posicion");
                int edad = rs.getInt("edad");
                resultado.add(new Jugador(nombre, equipo, posicion,edad));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mostrarResultados();
    }
	
	//Metodo que muestra los resultados de la busqueda
	private void mostrarResultados() {
        JPanel panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));

        if (resultado.isEmpty()) {
            panelResultados.add(new JLabel("No se encontraron jugadores."));
        } else {
            for (Jugador jugador : resultado) {
                panelResultados.add(new JLabel(jugador.toString()));
            }
        }

        getContentPane().add(new JScrollPane(panelResultados), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
	
	//Metodo que muestra las opciones de busqueda
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
	                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jugadores WHERE (equipo LIKE ?) AND (posicion LIKE ?)")) {
	                
	                stmt.setString(1, "%" + equipoFiltro + "%");
	                stmt.setString(2, "%" + posicionFiltro + "%");

	                ResultSet rs = stmt.executeQuery();
	                while (rs.next()) {
	                    String nombre = rs.getString("nombre");
	                    String equipo = rs.getString("equipo");
	                    String posicion = rs.getString("posicion");
	                    int edad = rs.getInt("edad");
	                    resultado.add(new Jugador(nombre, equipo, posicion,edad));
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
