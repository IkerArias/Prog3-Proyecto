package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ManagerNotif extends JFrame{
	
	public ManagerNotif() {
		
		setSize(300,300);
		setTitle("Notificaciones");
		setResizable(false);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Abrir la ventana de ManagerLogin cuando se cierra esta ventana
            	dispose();
                ManagerPerfil wel = new ManagerPerfil();
                wel.setVisible(true);
            }
        });

	}
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        	ManagerNotif frame = new ManagerNotif();
            frame.setVisible(true);
        });
    }

	
}
