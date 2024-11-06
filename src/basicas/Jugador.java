package basicas;

public class Jugador {
    private String nombre; 
    private String equipoNombre; 
    private String posicion; 
    private String pais;
    private Double valor;
    private int puntos;
    private int goles;
    private int asistencias;
    private int regates;
    private int tarjetas_amarillas;
    private int tarjetas_rojas;
	public Jugador() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Jugador(String nombre, String equipoNombre, String posicion, String pais, Double valor, int puntos,
			int goles, int asistencias, int regates, int tarjetas_amarillas, int tarjetas_rojas) {
		super();
		this.nombre = nombre;
		this.equipoNombre = equipoNombre;
		this.posicion = posicion;
		this.pais = pais;
		this.valor = valor;
		this.puntos = puntos;
		this.goles = goles;
		this.asistencias = asistencias;
		this.regates = regates;
		this.tarjetas_amarillas = tarjetas_amarillas;
		this.tarjetas_rojas = tarjetas_rojas;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEquipoNombre() {
		return equipoNombre;
	}
	public void setEquipoNombre(String equipoNombre) {
		this.equipoNombre = equipoNombre;
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
	public int getPuntos() {
		return puntos;
	}
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	public int getGoles() {
		return goles;
	}
	public void setGoles(int goles) {
		this.goles = goles;
	}
	public int getAsistencias() {
		return asistencias;
	}
	public void setAsistencias(int asistencias) {
		this.asistencias = asistencias;
	}
	public int getRegates() {
		return regates;
	}
	public void setRegates(int regates) {
		this.regates = regates;
	}
	public int getTarjetas_amarillas() {
		return tarjetas_amarillas;
	}
	public void setTarjetas_amarillas(int tarjetas_amarillas) {
		this.tarjetas_amarillas = tarjetas_amarillas;
	}
	public int getTarjetas_rojas() {
		return tarjetas_rojas;
	}
	public void setTarjetas_rojas(int tarjetas_rojas) {
		this.tarjetas_rojas = tarjetas_rojas;
	}
	@Override
	public String toString() {
		return "Jugador [nombre=" + nombre + ", equipoNombre=" + equipoNombre + ", posicion=" + posicion + ", pais="
				+ pais + ", valor=" + valor + ", puntos=" + puntos + ", goles=" + goles + ", asistencias=" + asistencias
				+ ", regates=" + regates + ", tarjetas_amarillas=" + tarjetas_amarillas + ", tarjetas_rojas="
				+ tarjetas_rojas + "]";
	}
    
    
   
    
}
