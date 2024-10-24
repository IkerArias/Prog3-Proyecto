package manager;

import gui.ManagerLogin;

public class Main {
	
	public static void main(String[] args) {
		
        // Ejecutar la ventana de inicio de sesión
        javax.swing.SwingUtilities.invokeLater(() -> {
            ManagerLogin v = new ManagerLogin();
            v.setVisible(true);
            
        });
    }

}
