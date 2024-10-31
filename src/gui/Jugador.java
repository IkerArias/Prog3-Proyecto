package gui;

public class Jugador {
	private String nombre; 
	private String equipo;
	private String posicion; 
	private int edad;
	public Jugador(String nombre, String equipo, String posicion, int edad) {
		super();
		this.nombre = nombre;
		this.equipo = equipo;
		this.posicion = posicion;
		this.edad = edad;
	}
	public Jugador() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	public String getPosicion() {
		return posicion;
	}
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	@Override
	public String toString() {
		return "Jugador [nombre=" + nombre + ", equipo=" + equipo + ", posicion=" + posicion + ", edad=" + edad + "]";
	}
	

}
