package fatasy.gui;

import javax.swing.*;
import java.awt.*;


public class ManagerWelcome extends JFrame{
	
	public ManagerWelcome() {
	
		//Configuración de la ventana principal
        setTitle("Fantasy Manager - Ventana Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        //Elementos de la ventana principal
        JLabel labelBienvenida = new JLabel("Bienvenido a Fantasy Manager", SwingConstants.CENTER);
        labelBienvenida.setFont(new Font("Serif", Font.BOLD, 24));
        
        // Añadir la etiqueta al panel principal
        add(labelBienvenida, BorderLayout.CENTER);
        
        
		
	}
	
	
	
  
    
}
