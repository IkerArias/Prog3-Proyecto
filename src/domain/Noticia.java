package domain;

public class Noticia {
	
	private String id;
	private String jornada;
	private String noticia;
	public Noticia() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Noticia(String id, String jornada, String noticia) {
		super();
		this.id = id;
		this.jornada = jornada;
		this.noticia = noticia;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJornada() {
		return jornada;
	}
	public void setJornada(String jornada) {
		this.jornada = jornada;
	}
	public String getNoticia() {
		return noticia;
	}
	public void setNoticia(String noticia) {
		this.noticia = noticia;
	}
	@Override
	public String toString() {
		return "Noticia [id=" + id + ", jornada=" + jornada + ", noticia=" + noticia + "]";
	}
	
	

}
