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

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ManagerPlantilla extends JFrame{
	
	
	//
	public ManagerPlantilla() {
		setSize(700,700);
		setTitle("Plantilla");
		setResizable(false);
		setLocationRelativeTo(null);
		
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Abrir la ventana de ManagerLogin cuando se cierra esta ventana
            	dispose();
                ManagerWelcome wel = new ManagerWelcome();
                wel.setVisible(true);
            }
        });
		
		//Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("src/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }
        
        
        
        setLayout(new BorderLayout());
        
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
        JButton btnExtremoDerecho = new JButton("Eextremo Derecho");
        JButton btnExtremoIzquierdo = new JButton("Eextremo Izquierdo");
        JButton btnDelantero = new JButton("Delantero Centro");
        
        // Añadir los botones a la formación (grid)
        //Fila 0: Portero
        pnlPlantilla.add(new JLabel("")); 
        pnlPlantilla.add(new JLabel("")); 
        pnlPlantilla.add(btnPortero);
        pnlPlantilla.add(new JLabel("")); 
        pnlPlantilla.add(new JLabel("")); 
        
        //Fila 1: Defensas
        pnlPlantilla.add(btnLateralIzquierdo);
        pnlPlantilla.add(btnDefensaCentral1);
        pnlPlantilla.add(new JLabel(""));
        pnlPlantilla.add(btnDefensaCentral2);
        pnlPlantilla.add(btnLateralDerecho);
        
        
        // Fila 2: Medios
        pnlPlantilla.add(new JLabel("")); 
        pnlPlantilla.add(btnCentro1); 
        pnlPlantilla.add(btnMedioOfensivo); 
        pnlPlantilla.add(btnCentro2);
        pnlPlantilla.add(new JLabel("")); 
        

        // Fila 3: Delanteros
        pnlPlantilla.add(btnExtremoIzquierdo); 
        pnlPlantilla.add(new JLabel("")); 
        pnlPlantilla.add(btnDelantero); 
        pnlPlantilla.add(new JLabel("")); 
        pnlPlantilla.add(btnExtremoDerecho); 

        
        
        // Crear panel para el banquillo
        JPanel benchPanel = new JPanel(new GridLayout(1, 4)); 
        JButton[] benchButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            benchButtons[i] = new JButton("Banquillo " + (i + 1));
            benchPanel.add(benchButtons[i]);
        }
        add(benchPanel, BorderLayout.SOUTH);
        
        
        // Acción al hacer clic en un botón (Aquí deberías cargar la lista de jugadores según la posición)
        ActionListener addPlayerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                String position = source.getText();
                System.out.println("Añadir jugador en la posición: " + position);
                
                // Aquí se debe implementar la lógica de añadir jugadores a la plantilla
                // Podrías abrir una ventana para seleccionar un jugador según la posición.
            }
        };
        
        
       
        
        
        btnPortero.addActionListener(addPlayerListener);
        btnDefensaCentral1.addActionListener(addPlayerListener);
        btnDefensaCentral2.addActionListener(addPlayerListener);
        btnLateralDerecho.addActionListener(addPlayerListener);
        btnLateralIzquierdo.addActionListener(addPlayerListener);
        btnCentro1.addActionListener(addPlayerListener);
        btnCentro2.addActionListener(addPlayerListener);
        btnMedioOfensivo.addActionListener(addPlayerListener);
        btnExtremoDerecho.addActionListener(addPlayerListener);
        btnExtremoIzquierdo.addActionListener(addPlayerListener);
        btnDelantero.addActionListener(addPlayerListener);

		
		
	}
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        	ManagerPlantilla frame = new ManagerPlantilla();
            frame.setVisible(true);
        });
    }

}
