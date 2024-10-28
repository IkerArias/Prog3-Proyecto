package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ManagerClasificacion extends JFrame{
	
	public ManagerClasificacion() {
		setSize(300,300);
		setTitle("Clasificación");
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
		
		
		
		
		
	}
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        	ManagerClasificacion frame = new ManagerClasificacion();
            frame.setVisible(true);
        });
    }
	
	

}
