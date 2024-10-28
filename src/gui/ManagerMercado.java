package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ManagerMercado extends JFrame{
	
	public ManagerMercado() {
		
		setSize(300,300);
		setTitle("Mercado");
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
        	ManagerMercado frame = new ManagerMercado();
            frame.setVisible(true);
        });
    }

	
}
