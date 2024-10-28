package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ManagerPerfil extends JFrame{
	
	public ManagerPerfil() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(300,300);
		setTitle("Perfil");
        setLocationRelativeTo(null);
        setResizable(false);
        
        
        
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
        	ManagerPerfil frame = new ManagerPerfil();
            frame.setVisible(true);
        });
    }
	

}
