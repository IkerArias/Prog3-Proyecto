package basicas;

public class Jugador {
	private String nombre; 
	private int equipo;
	private String posicion; 
	private String pais;
	private Double valor;
	public Jugador() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Jugador(String nombre, int equipo, String posicion, String pais, Double valor) {
		super();
		this.nombre = nombre;
		this.equipo = equipo;
		this.posicion = posicion;
		this.pais = pais;
		this.valor = valor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getEquipo() {
		return equipo;
	}
	public void setEquipo(int equipo) {
		this.equipo = equipo;
	}
	public String getPosicion() {
		return posicion;
	}
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	@Override
	public String toString() {
		return "Jugador [nombre=" + nombre + ", equipo=" + equipo + ", posicion=" + posicion + ", pais=" + pais
				+ ", valor=" + valor + "]";
	}
	
	

	

}
