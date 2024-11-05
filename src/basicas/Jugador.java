package basicas;

public class Jugador {
    private String nombre; 
    private String equipoNombre; 
    private String posicion; 
    private String pais;
    private Double valor;

    // Constructor vacío
    public Jugador() {
        super();
    }

    
    public Jugador(String nombre, String equipoNombre, String posicion, String pais, Double valor) {
        super();
        this.nombre = nombre;
        this.equipoNombre = equipoNombre; 
        this.posicion = posicion;
        this.pais = pais;
        this.valor = valor;
    }

    // Getters y setters
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


    @Override
    public String toString() {
        return nombre + " | " + equipoNombre + " | " + posicion + " | " + pais + " | " + "Valor: " + valor + " M";
    }
}
