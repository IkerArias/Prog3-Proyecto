package BaseDeDatos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FantasyBD {
    
    private static final String URL = "jdbc:sqlite:futbol_fantasy.db"; 

    public static void main(String[] args) {

    	crearBaseDeDatos();
    	crearTablas();
        insertarDatos();
        
        
        // Mostrar los datos
        mostrarEquipos();
        mostrarJugadores();
        mostrarPartidos();
        
        //CUIDADO!!!!!
        //vaciarBaseDeDatos();
       
    }

    // Crear una nueva base de datos
    private static void crearBaseDeDatos() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                System.out.println("Base de datos creada exitosamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos: " + e.getMessage());
        }
    }
    

    // Crear las tablas de la base de datos
    private static void crearTablas() {
        // SQL para crear las tablas
    	// Tabla equipo (id, nombre, ciudad y fundacion)
        String sqlEquipos = "CREATE TABLE IF NOT EXISTS Equipos (" +
                "id INTEGER PRIMARY KEY," +
                "nombre TEXT NOT NULL," +
                "ciudad TEXT" +
                ");";

    	//Tabla jugador con varias estadisticas
    	String sqlJugadores = "CREATE TABLE IF NOT EXISTS Jugadores (" +
                "id INTEGER PRIMARY KEY," +
                "nombre TEXT NOT NULL," +
                "posicion TEXT," +
                "equipo_id INTEGER," +
                "valor INTEGER," +
                "puntos INTEGER DEFAULT 0," +
                "goles INTEGER DEFAULT 0," +
                "asistencias INTEGER DEFAULT 0," +
                "regates INTEGER DEFAULT 0," +
                "tarjetas_amarillas INTEGER DEFAULT 0," +
                "tarjetas_rojas INTEGER DEFAULT 0," +
                "FOREIGN KEY (equipo_id) REFERENCES Equipos(id)" +
                ");";

    	//tabla para partidos
    	// Corrección en el SQL de creación de la tabla Partidos
    	String sqlPartidos = "CREATE TABLE IF NOT EXISTS Partidos (" +
    	        "id INTEGER PRIMARY KEY," +
    	        "equipo_local_id INTEGER," +
    	        "equipo_visitante_id INTEGER," +
    	        "jornada INTEGER," +  
    	        "goles_local INTEGER," +
    	        "goles_visitante INTEGER," +
    	        "FOREIGN KEY (equipo_local_id) REFERENCES Equipos(id)," +
    	        "FOREIGN KEY (equipo_visitante_id) REFERENCES Equipos(id)" +
    	        ");";



        

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            // Crear las tablas
            stmt.execute(sqlEquipos);
            stmt.execute(sqlJugadores);
            stmt.execute(sqlPartidos);
      
            System.out.println("Tablas creadas exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }
    
    // Método para insertar un equipo
    private static void insertarEquipo(String nombre, String ciudad) {
        String sql = "INSERT INTO Equipos (nombre, ciudad) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, ciudad);
            pstmt.executeUpdate();
            System.out.println("Equipo insertado: " + nombre);
        } catch (SQLException e) {
            System.out.println("Error al insertar equipo: " + e.getMessage());
        }
    }
    
    // Método para insertar un jugador
    private static void insertarJugador(String nombre, int equipoId, String posicion, int valor) {
        String sql = "INSERT INTO Jugadores (nombre, equipo_id, posicion, valor) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setInt(2, equipoId);
            pstmt.setString(3, posicion);
            pstmt.setInt(4, valor);  
            pstmt.executeUpdate();
            System.out.println("Jugador insertado: " + nombre);
        } catch (SQLException e) {
            System.out.println("Error al insertar jugador: " + e.getMessage());
        }
    }

    // Método para insertar un partido
    private static void insertarPartido(int equipoLocal, int equipoVisitante, int golesLocal, int golesVisitante, int jornada) {
        String sql = "INSERT INTO Partidos (equipo_local_id, equipo_visitante_id, goles_local, goles_visitante, jornada) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipoLocal);
            pstmt.setInt(2, equipoVisitante);
            pstmt.setInt(3, golesLocal);
            pstmt.setInt(4, golesVisitante);
            pstmt.setInt(5, jornada);
            pstmt.executeUpdate();
            System.out.println("Partido insertado entre equipo " + equipoLocal + " y equipo " + equipoVisitante);
        } catch (SQLException e) {
            System.out.println("Error al insertar partido: " + e.getMessage());
        }
    }

    
    //Metodo para mostrar los equipos
    private static void mostrarEquipos() {
        String sql = "SELECT * FROM Equipos";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Equipos:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Nombre: " + rs.getString("nombre") + 
                                   ", Ciudad: " + rs.getString("ciudad"));
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar equipos: " + e.getMessage());
        }
    }
    
    
    //Metodo para mostrar todos los jugadores
    private static void mostrarJugadores() {
        String sql = "SELECT * FROM Jugadores";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Jugadores:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Nombre: " + rs.getString("nombre") + 
                                   ", Posición: " + rs.getString("posicion") + 
                                   ", Equipo ID: " + rs.getInt("equipo_id") + 
                                   ", Valor: " + rs.getDouble("valor") +
                                   ", Puntos: " + rs.getInt("puntos"));
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar jugadores: " + e.getMessage());
        }
    }
    
    // Método para mostrar todos los partidos
    private static void mostrarPartidos() {
        String sql = "SELECT * FROM Partidos";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Partidos:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Equipo Local ID: " + rs.getInt("equipo_local_id") + 
                                   ", Equipo Visitante ID: " + rs.getInt("equipo_visitante_id") + 
                                   ", Jornada: " + rs.getInt("jornada") + 
                                   ", Goles Local: " + rs.getInt("goles_local") + 
                                   ", Goles Visitante: " + rs.getInt("goles_visitante"));
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar partidos: " + e.getMessage());
        }
    }
    
    //CUIDADO!!!!!!!!
    // Método para vaciar todas las tablas
    private static void vaciarBaseDeDatos() {
        String sqlVaciarEquipos = "DELETE FROM Equipos";
        String sqlVaciarJugadores = "DELETE FROM Jugadores";
        String sqlVaciarPartidos = "DELETE FROM Partidos";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            // Eliminar todos los datos de cada tabla
            stmt.execute(sqlVaciarEquipos);
            stmt.execute(sqlVaciarJugadores);
            stmt.execute(sqlVaciarPartidos);
            
            System.out.println("Base de datos vaciada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al vaciar la base de datos: " + e.getMessage());
        }
    }


    // Método para insertar datos de prueba
    private static void insertarDatos() {
    	
    	insertarEquipo("Athletic Club De Bilbao", "Bilbao"); //1
    	insertarEquipo("FC Barcelona", "Barcelona");  //2
    	insertarEquipo("Real Madrid", "Madrid");  //3 
    	insertarEquipo("Atlético de Madrid", "Madrid");  //4
    	insertarEquipo("Real Sociedad", "San Sebastián");  //...
    	insertarEquipo("Real Betis", "Sevilla");
    	insertarEquipo("Sevilla FC", "Sevilla");
    	insertarEquipo("Valencia CF", "Valencia");
    	insertarEquipo("Villarreal CF", "Vila-real");
    	insertarEquipo("Celta de Vigo", "Vigo");
    	insertarEquipo("Getafe CF", "Getafe");
    	insertarEquipo("Rayo Vallecano", "Madrid");
    	insertarEquipo("UD Las Palmas", "Las Palmas de Gran Canaria");
    	insertarEquipo("Alavés", "Vitoria-Gasteiz");
    	insertarEquipo("Mallorca", "Palma");
    	insertarEquipo("Girona FC", "Girona");
    	insertarEquipo("Osasuna", "Pamplona");
    	insertarEquipo("RCD Espanyol", "Barcelona");
    	insertarEquipo("Real Valladolid", "Valladolid");
    	insertarEquipo("CD Leganés", "Leganés");
    	
    	
    	// Insertar jugadores del Athletic
    	insertarJugador("Unai Simón", 1, "Portero", 27);
    	insertarJugador("Julen Agirrezabala", 1, "Portero", 23);
    	insertarJugador("Álex Padilla", 1, "Portero", 21);
    	insertarJugador("Dani Vivian", 1, "Defensa central", 25);
    	insertarJugador("Aitor Paredes", 1, "Defensa central", 24);
    	insertarJugador("Yeray Álvarez", 1, "Defensa central", 29);
    	insertarJugador("Unai Núñez", 1, "Defensa central", 27);
    	insertarJugador("Yuri Berchiche", 1, "Lateral izquierdo", 34);
    	insertarJugador("Adama Boiro", 1, "Lateral izquierdo", 22);
    	insertarJugador("Andoni Gorosabel", 1, "Lateral derecho", 28);
    	insertarJugador("Iñigo Lekue", 1, "Lateral derecho", 31);
    	insertarJugador("Óscar de Marcos", 1, "Lateral derecho", 35);
    	insertarJugador("Mikel Vesga", 1, "Pivote", 31);
    	insertarJugador("Beñat Prados", 1, "Mediocentro", 23);
    	insertarJugador("Iñigo Ruiz de Galarreta", 1, "Mediocentro", 31);
    	insertarJugador("Mikel Jauregizar", 1, "Mediocentro", 20);
    	insertarJugador("Ander Herrera", 1, "Mediocentro", 35);
    	insertarJugador("Oihan Sancet", 1, "Mediocentro ofensivo", 24);
    	insertarJugador("Unai Gómez", 1, "Mediocentro ofensivo", 21);
    	insertarJugador("Nico Williams", 1, "Extremo izquierdo", 22);
    	insertarJugador("Álvaro Djaló", 1, "Extremo izquierdo", 25);
    	insertarJugador("Álex Berenguer", 1, "Extremo izquierdo", 29);
    	insertarJugador("Nico Serrano", 1, "Extremo izquierdo", 21);
    	insertarJugador("Iñaki Williams", 1, "Extremo derecho", 30);
    	insertarJugador("Gorka Guruzeta", 1, "Delantero centro", 28);
    	insertarJugador("Javier Martón", 1, "Delantero centro", 25);
    	
    	//Insertar jugadores del Barcelona
    	insertarJugador("Marc-André ter Stegen", 2, "Portero", 32);
    	insertarJugador("Iñaki Peña", 2, "Portero", 25);
    	insertarJugador("Wojciech Szczęsny", 2, "Portero", 34);
    	insertarJugador("Ronald Araujo", 2, "Defensa central", 25);
    	insertarJugador("Pau Cubarsí", 2, "Defensa central", 17);
    	insertarJugador("Andreas Christensen", 2, "Defensa central", 28);
    	insertarJugador("Eric García", 2, "Defensa central", 23);
    	insertarJugador("Iñigo Martínez", 2, "Defensa central", 33);
    	insertarJugador("Alejandro Balde", 2, "Lateral izquierdo", 21);
    	insertarJugador("Gerard Martín", 2, "Lateral izquierdo", 22);
    	insertarJugador("Jules Koundé", 2, "Lateral derecho", 25);
    	insertarJugador("Héctor Fort", 2, "Lateral derecho", 18);
    	insertarJugador("Marc Casadó", 2, "Pivote", 21);
    	insertarJugador("Marc Bernal", 2, "Pivote", 17);
    	insertarJugador("Gavi", 2, "Mediocentro", 20);
    	insertarJugador("Pedri", 2, "Mediocentro", 21);
    	insertarJugador("Frenkie de Jong", 2, "Mediocentro", 27);
    	insertarJugador("Fermín López", 2, "Mediocentro", 21);
    	insertarJugador("Dani Olmo", 2, "Mediocentro ofensivo", 26);
    	insertarJugador("Pablo Torre", 2, "Mediocentro ofensivo", 21);
    	insertarJugador("Raphinha", 2, "Extremo izquierdo", 27);
    	insertarJugador("Ansu Fati", 2, "Extremo izquierdo", 21);
    	insertarJugador("Lamine Yamal", 2, "Extremo derecho", 17);
    	insertarJugador("Ferran Torres", 2, "Extremo derecho", 24);
    	insertarJugador("Robert Lewandowski", 2, "Delantero centro", 36);
    	insertarJugador("Pau Víctor", 2, "Delantero centro", 22);
    	
    	
    	//Insertar jugadores del Madrid
    	insertarJugador("Thibaut Courtois", 3, "Portero", 32);
    	insertarJugador("Andriy Lunin", 3, "Portero", 25);
    	insertarJugador("Éder Militão", 3, "Defensa central", 26);
    	insertarJugador("Antonio Rüdiger", 3, "Defensa central", 31);
    	insertarJugador("David Alaba", 3, "Defensa central", 32);
    	insertarJugador("Jesús Vallejo", 3, "Defensa central", 27);
    	insertarJugador("Ferland Mendy", 3, "Lateral izquierdo", 29);
    	insertarJugador("Fran García", 3, "Lateral izquierdo", 25);
    	insertarJugador("Daniel Carvajal", 3, "Lateral derecho", 32);
    	insertarJugador("Lucas Vázquez", 3, "Lateral derecho", 33);
    	insertarJugador("Aurélien Tchouaméni", 3, "Pivote", 24);
    	insertarJugador("Federico Valverde", 3, "Mediocentro", 26);
    	insertarJugador("Eduardo Camavinga", 3, "Mediocentro", 21);
    	insertarJugador("Luka Modric", 3, "Mediocentro", 39);
    	insertarJugador("Dani Ceballos", 3, "Mediocentro", 28);
    	insertarJugador("Jude Bellingham", 3, "Mediocentro ofensivo", 21);
    	insertarJugador("Brahim Díaz", 3, "Mediocentro ofensivo", 25);
    	insertarJugador("Vinicius Junior", 3, "Extremo izquierdo", 24);
    	insertarJugador("Rodrygo", 3, "Extremo derecho", 23);
    	insertarJugador("Arda Güler", 3, "Extremo derecho", 19);
    	insertarJugador("Kylian Mbappé", 3, "Delantero centro", 25);
    	insertarJugador("Endrick", 3, "Delantero centro", 18);

    	
    	//Insertar jugadores del Atletico
    	insertarJugador("Jan Oblak", 4, "Portero", 31);
    	insertarJugador("Juan Musso", 4, "Portero", 30);
    	insertarJugador("Robin Le Normand", 4, "Defensa central", 27);
    	insertarJugador("José María Giménez", 4, "Defensa central", 29);
    	insertarJugador("Clément Lenglet", 4, "Defensa central", 29);
    	insertarJugador("Axel Witsel", 4, "Defensa central", 35);
    	insertarJugador("César Azpilicueta", 4, "Defensa central", 35);
    	insertarJugador("Reinildo Mandava", 4, "Lateral izquierdo", 30);
    	insertarJugador("Javi Galán", 4, "Lateral izquierdo", 29);
    	insertarJugador("Nahuel Molina", 4, "Lateral derecho", 26);
    	insertarJugador("Conor Gallagher", 4, "Mediocentro", 24);
    	insertarJugador("Rodrigo De Paul", 4, "Mediocentro", 30);
    	insertarJugador("Pablo Barrios", 4, "Mediocentro", 21);
    	insertarJugador("Koke", 4, "Mediocentro", 32);
    	insertarJugador("Marcos Llorente", 4, "Interior derecho", 29);
    	insertarJugador("Samuel Lino", 4, "Interior izquierdo", 24);
    	insertarJugador("Thomas Lemar", 4, "Mediocentro ofensivo", 28);
    	insertarJugador("Rodrigo Riquelme", 4, "Extremo izquierdo", 24);
    	insertarJugador("Ángel Correa", 4, "Extremo derecho", 29);
    	insertarJugador("Julián Alvarez", 4, "Delantero centro", 24);
    	insertarJugador("Antoine Griezmann", 4, "Delantero centro", 33);
    	insertarJugador("Alexander Sørloth", 4, "Delantero centro", 28);
    	insertarJugador("Giuliano Simeone", 4, "Delantero centro", 21);
    	insertarJugador("Borja Garcés", 4, "Delantero centro", 25);

    	//INSERTAR MAS JUGADORES DE TODOS LOS EQUIPOS EN EL FUTURO
    	
    	//Insertar partidos Jornada 1
    	insertarPartido(1, 2, 1, 1, 1); //1
    	insertarPartido(3, 4, 1, 3, 1); //2
    	insertarPartido(5, 6, 1, 0, 1); //3
    	
    	
    	//INSERTAR MAS PARTIDOS DE TODOS LOS EQUIPOS EN EL FUTURO
    	

    	
    	
    }
    
    

}

