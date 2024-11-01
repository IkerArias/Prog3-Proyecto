package gui;

import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ManagerPlantilla extends JFrame{
	
	
	//
	public ManagerPlantilla() {
		setSize(300,300);
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

		
		
	}
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        	ManagerPlantilla frame = new ManagerPlantilla();
            frame.setVisible(true);
        });
    }

}
