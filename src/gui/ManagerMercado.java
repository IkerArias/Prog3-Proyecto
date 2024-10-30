package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ManagerMercado extends JFrame{
	private static final long serialVersionUID = 1L;
	
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
		
		JTextField textField = new JTextField();
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
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        	ManagerMercado frame = new ManagerMercado();
            frame.setVisible(true);
        });
    }

	
}
