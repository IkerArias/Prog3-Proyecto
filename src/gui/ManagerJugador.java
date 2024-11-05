package gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import basicas.Jugador;

public class ManagerJugador extends JFrame{
	
	public ManagerJugador(Jugador jugador) {
        setTitle("Ficha del Jugador");
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        
        panel.add(new JLabel("Nombre: " + jugador.getNombre()));
        panel.add(new JLabel("Equipo: " + jugador.getEquipoNombre()));
        panel.add(new JLabel("Posición: " + jugador.getPosicion()));
        panel.add(new JLabel("País: " + jugador.getPais()));
        panel.add(new JLabel("Valor: " + jugador.getValor()));

        add(panel);
        this.setVisible(true);
    }

}
