package db;

import java.sql.*;

public class FantasyBD {

    private static final String URL = "jdbc:sqlite:futbol_fantasy.db"; 

    public static void main(String[] args) {
        vaciarBaseDeDatos();
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
        String sqlEquipos = "CREATE TABLE IF NOT EXISTS Equipos (" +
                "id INTEGER PRIMARY KEY," +
                "nombre TEXT NOT NULL," +
                "ciudad TEXT" +
                ");";

        String sqlJugadores = "CREATE TABLE IF NOT EXISTS Jugadores (" +
                "id INTEGER PRIMARY KEY," +
                "nombre TEXT NOT NULL," +
                "posicion TEXT," +
                "equipo_id INTEGER," +
                "pais TEXT," +
                "valor DOUBLE," +
                "puntos INTEGER DEFAULT 0," +
                "goles INTEGER DEFAULT 0," +
                "asistencias INTEGER DEFAULT 0," +
                "regates INTEGER DEFAULT 0," +
                "tarjetas_amarillas INTEGER DEFAULT 0," +
                "tarjetas_rojas INTEGER DEFAULT 0," +
                "FOREIGN KEY (equipo_id) REFERENCES Equipos(id)" +
                ");";

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

        String sqlJugadoresPartidos = "CREATE TABLE IF NOT EXISTS Jugadores_Partidos (" +
                "id INTEGER PRIMARY KEY," +
                "jugador_id INTEGER," +
                "partido_id INTEGER," +
                "goles INTEGER DEFAULT 0," +
                "asistencias INTEGER DEFAULT 0," +
                "regates INTEGER DEFAULT 0," +
                "tarjetas_amarillas INTEGER DEFAULT 0," +
                "tarjetas_rojas INTEGER DEFAULT 0," +
                "FOREIGN KEY (jugador_id) REFERENCES Jugadores(id)," +
                "FOREIGN KEY (partido_id) REFERENCES Partidos(id)" +
                ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlEquipos);
            stmt.execute(sqlJugadores);
            stmt.execute(sqlPartidos);
            stmt.execute(sqlJugadoresPartidos);

            System.out.println("Tablas creadas exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }

    // Insertar un equipo
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

    // Insertar un jugador
    private static void insertarJugador(String nombre, int equipoId, String posicion, String pais, double valor) {
        String sql = "INSERT INTO Jugadores (nombre, equipo_id, posicion, pais, valor) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setInt(2, equipoId);
            pstmt.setString(3, posicion);
            pstmt.setString(4, pais);
            pstmt.setDouble(5,  valor);  
            pstmt.executeUpdate();
            System.out.println("Jugador insertado: " + nombre);
        } catch (SQLException e) {
            System.out.println("Error al insertar jugador: " + e.getMessage());
        }
    }

    // Insertar un partido
    private static void insertarPartido(int equipoLocal, int equipoVisitante, int golesLocal, int golesVisitante, int jornada) {
        String sql = "INSERT INTO Partidos (equipo_local_id, equipo_visitante_id, goles_local, goles_visitante, jornada) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, equipoLocal);
            pstmt.setInt(2, equipoVisitante);
            pstmt.setInt(3, golesLocal);
            pstmt.setInt(4, golesVisitante);
            pstmt.setInt(5, jornada);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int partidoId = rs.getInt(1);
                System.out.println("Partido insertado con ID: " + partidoId);
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar partido: " + e.getMessage());
        }
    }

    // Insertar estadísticas de un jugador en un partido y actualizar puntos
    private static void insertarEstadisticasPartido(int jugadorId, int partidoId, int goles, int asistencias, int regates, int tarjetasAmarillas, int tarjetasRojas) {
        String sqlInsertarEstadisticas = "INSERT INTO Jugadores_Partidos (jugador_id, partido_id, goles, asistencias, regates, tarjetas_amarillas, tarjetas_rojas) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlActualizarEstadisticas = "UPDATE Jugadores SET goles = goles + ?, asistencias = asistencias + ?, regates = regates + ?, tarjetas_amarillas = tarjetas_amarillas + ?, tarjetas_rojas = tarjetas_rojas + ? WHERE id = ?";
        String sqlActualizarPuntos = "UPDATE Jugadores SET puntos = puntos + ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.setAutoCommit(false);  // Deshabilitar auto commit

            try (PreparedStatement pstmtInsertar = conn.prepareStatement(sqlInsertarEstadisticas);
                 PreparedStatement pstmtActualizarEstadisticas = conn.prepareStatement(sqlActualizarEstadisticas);
                 PreparedStatement pstmtActualizarPuntos = conn.prepareStatement(sqlActualizarPuntos)) {

                pstmtInsertar.setInt(1, jugadorId);
                pstmtInsertar.setInt(2, partidoId);
                pstmtInsertar.setInt(3, goles);
                pstmtInsertar.setInt(4, asistencias);
                pstmtInsertar.setInt(5, regates);
                pstmtInsertar.setInt(6, tarjetasAmarillas);
                pstmtInsertar.setInt(7, tarjetasRojas);
                pstmtInsertar.executeUpdate();

                pstmtActualizarEstadisticas.setInt(1, goles);
                pstmtActualizarEstadisticas.setInt(2, asistencias);
                pstmtActualizarEstadisticas.setInt(3, regates);
                pstmtActualizarEstadisticas.setInt(4, tarjetasAmarillas);
                pstmtActualizarEstadisticas.setInt(5, tarjetasRojas);
                pstmtActualizarEstadisticas.setInt(6, jugadorId);
                pstmtActualizarEstadisticas.executeUpdate();

                int puntosJugador = calcularPuntosJugador(goles, asistencias, regates, tarjetasAmarillas, tarjetasRojas);
                pstmtActualizarPuntos.setInt(1, puntosJugador);
                pstmtActualizarPuntos.setInt(2, jugadorId);
                pstmtActualizarPuntos.executeUpdate();

                conn.commit();  // Confirmar la transacción
                System.out.println("Estadísticas del jugador insertadas y puntos actualizados.");
            } catch (SQLException e) {
                conn.rollback();  // Si algo falla, revertir todo
                System.out.println("Error al insertar estadísticas del jugador: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // Método para calcular los puntos del jugador
    private static int calcularPuntosJugador(int goles, int asistencias, int regates, int tarjetasAmarillas, int tarjetasRojas) {
        int puntos = 0;

        puntos += goles * 6;  // 6 punto por cada gol
        puntos += asistencias * 3;  // 3 punto por cada asistencia
        puntos += regates * 1;  // 1 puntos por cada regate
        puntos -= tarjetasAmarillas * 1;  // -1 punto por cada tarjeta amarilla
        puntos -= tarjetasRojas * 2;  // -2 puntos por cada tarjeta roja

        return puntos;
    }

    // Mostrar equipos
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

    // Mostrar jugadores
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
                                   ", País: " + rs.getString("pais") +
                                   ", Puntos: " + rs.getInt("puntos"));
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar jugadores: " + e.getMessage());
        }
    }

    // Mostrar partidos
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

    // Vaciar base de datos
    private static void vaciarBaseDeDatos() {
        String sql = "DROP TABLE IF EXISTS Jugadores_Partidos; DROP TABLE IF EXISTS Partidos; DROP TABLE IF EXISTS Jugadores; DROP TABLE IF EXISTS Equipos;";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Base de datos vaciada.");
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
    	insertarJugador("Unai Simón", 1, "Portero", "España", 30.00);
    	insertarJugador("Julen Agirrezabala", 1, "Portero", "España", 3.50);
    	insertarJugador("Álex Padilla", 1, "Portero", "España", 0.80);
    	insertarJugador("Dani Vivian", 1, "Defensa central", "España", 15.00);
    	insertarJugador("Aitor Paredes", 1, "Defensa central", "España", 10.00);
    	insertarJugador("Yeray Álvarez", 1, "Defensa central", "España", 8.50);
    	insertarJugador("Unai Núñez", 1, "Defensa central", "España", 7.00);
    	insertarJugador("Yuri Berchiche", 1, "Lateral izquierdo", "España", 2.00);
    	insertarJugador("Adama Boiro", 1, "Lateral izquierdo", "España", 1.20);
    	insertarJugador("Andoni Gorosabel", 1, "Lateral derecho", "España", 3.00);
    	insertarJugador("Iñigo Lekue", 1, "Lateral derecho", "España", 1.50);
    	insertarJugador("Óscar de Marcos", 1, "Lateral derecho", "España", 0.80);
    	insertarJugador("Mikel Vesga", 1, "Pivote", "España", 4.00);
    	insertarJugador("Beñat Prados", 1, "Mediocentro", "España", 2.50);
    	insertarJugador("Iñigo Ruiz de Galarreta", 1, "Mediocentro", "España", 3.50);
    	insertarJugador("Mikel Jauregizar", 1, "Mediocentro", "España", 0.90);
    	insertarJugador("Ander Herrera", 1, "Mediocentro", "España", 1.00);
    	insertarJugador("Oihan Sancet", 1, "Mediocentro ofensivo", "España", 22.00);
    	insertarJugador("Unai Gómez", 1, "Mediocentro ofensivo", "España", 1.50);
    	insertarJugador("Nico Williams", 1, "Extremo izquierdo", "España", 35.00);
    	insertarJugador("Álvaro Djaló", 1, "Extremo izquierdo", "España", 5.50);
    	insertarJugador("Álex Berenguer", 1, "Extremo izquierdo", "España", 7.00);
    	insertarJugador("Nico Serrano", 1, "Extremo izquierdo", "España", 2.00);
    	insertarJugador("Iñaki Williams", 1, "Extremo derecho", "España", 25.00);
    	insertarJugador("Gorka Guruzeta", 1, "Delantero centro", "España", 4.50);
    	insertarJugador("Javier Martón", 1, "Delantero centro", "España", 2.50);

    	// Insertar jugadores del Barcelona
    	insertarJugador("Marc-André ter Stegen", 2, "Portero", "Alemania", 40.00);
    	insertarJugador("Iñaki Peña", 2, "Portero", "España", 3.00);
    	insertarJugador("Wojciech Szczęsny", 2, "Portero", "Polonia", 6.00);
    	insertarJugador("Ronald Araujo", 2, "Defensa central", "Uruguay", 60.00);
    	insertarJugador("Pau Cubarsí", 2, "Defensa central", "España", 1.80);
    	insertarJugador("Andreas Christensen", 2, "Defensa central", "Dinamarca", 30.00);
    	insertarJugador("Eric García", 2, "Defensa central", "España", 10.00);
    	insertarJugador("Iñigo Martínez", 2, "Defensa central", "España", 5.00);
    	insertarJugador("Alejandro Balde", 2, "Lateral izquierdo", "España", 50.00);
    	insertarJugador("Gerard Martín", 2, "Lateral izquierdo", "España", 1.50);
    	insertarJugador("Jules Koundé", 2, "Lateral derecho", "Francia", 40.00);
    	insertarJugador("Héctor Fort", 2, "Lateral derecho", "España", 0.90);
    	insertarJugador("Marc Casadó", 2, "Pivote", "España", 2.00);
    	insertarJugador("Marc Bernal", 2, "Pivote", "España", 1.00);
    	insertarJugador("Gavi", 2, "Mediocentro", "España", 90.00);
    	insertarJugador("Pedri", 2, "Mediocentro", "España", 100.00);
    	insertarJugador("Frenkie de Jong", 2, "Mediocentro", "Países Bajos", 80.00);
    	insertarJugador("Fermín López", 2, "Mediocentro", "España", 3.50);
    	insertarJugador("Dani Olmo", 2, "Mediocentro ofensivo", "España", 50.00);
    	insertarJugador("Pablo Torre", 2, "Mediocentro ofensivo", "España", 1.50);
    	insertarJugador("Raphinha", 2, "Extremo izquierdo", "Brasil", 35.00);
    	insertarJugador("Ansu Fati", 2, "Extremo izquierdo", "España", 25.00);
    	insertarJugador("Lamine Yamal", 2, "Extremo derecho", "España", 25.00);
    	insertarJugador("Ferran Torres", 2, "Extremo derecho", "España", 30.00);
    	insertarJugador("Robert Lewandowski", 2, "Delantero centro", "Polonia", 15.00);
    	insertarJugador("Pau Víctor", 2, "Delantero centro", "España", 2.50);

    	//Real Madrid
    	insertarJugador("Thibaut Courtois", 3, "Portero", "Bélgica", 55.00);
    	insertarJugador("Andriy Lunin", 3, "Portero", "Ucrania", 10.00);
    	insertarJugador("Éder Militão", 3, "Defensa central", "Brasil", 70.00);
    	insertarJugador("Antonio Rüdiger", 3, "Defensa central", "Alemania", 20.00);
    	insertarJugador("David Alaba", 3, "Defensa central", "Austria", 10.00);
    	insertarJugador("Jesús Vallejo", 3, "Defensa central", "España", 1.50);
    	insertarJugador("Ferland Mendy", 3, "Lateral izquierdo", "Francia", 20.00);
    	insertarJugador("Fran García", 3, "Lateral izquierdo", "España", 8.00);
    	insertarJugador("Daniel Carvajal", 3, "Lateral derecho", "España", 15.00);
    	insertarJugador("Lucas Vázquez", 3, "Lateral derecho", "España", 3.50);
    	insertarJugador("Aurélien Tchouaméni", 3, "Pivote", "Francia", 85.00);
    	insertarJugador("Federico Valverde", 3, "Mediocentro", "Uruguay", 100.00);
    	insertarJugador("Eduardo Camavinga", 3, "Mediocentro", "Francia", 80.00);
    	insertarJugador("Luka Modric", 3, "Mediocentro", "Croacia", 5.00);
    	insertarJugador("Dani Ceballos", 3, "Mediocentro", "España", 15.00);
    	insertarJugador("Jude Bellingham", 3, "Mediocentro ofensivo", "Inglaterra", 150.00);
    	insertarJugador("Brahim Díaz", 3, "Mediocentro ofensivo", "España", 10.00);
    	insertarJugador("Vinicius Junior", 3, "Extremo izquierdo", "Brasil", 120.00);
    	insertarJugador("Rodrygo", 3, "Extremo derecho", "Brasil", 80.00);
    	insertarJugador("Arda Güler", 3, "Extremo derecho", "Turquía", 25.00);
    	insertarJugador("Kylian Mbappé", 3, "Delantero centro", "Francia", 180.00);
    	insertarJugador("Endrick", 3, "Delantero centro", "Brasil", 20.00);
    	
    	//Atletico de Madrid
    	insertarJugador("Jan Oblak", 4, "Portero", "Eslovenia", 35.00);
    	insertarJugador("Juan Musso", 4, "Portero", "Argentina", 12.00);
    	insertarJugador("Robin Le Normand", 4, "Defensa central", "Francia", 30.00);
    	insertarJugador("José María Giménez", 4, "Defensa central", "Uruguay", 25.00);
    	insertarJugador("Clément Lenglet", 4, "Defensa central", "Francia", 10.00);
    	insertarJugador("Axel Witsel", 4, "Defensa central", "Bélgica", 2.00);
    	insertarJugador("César Azpilicueta", 4, "Defensa central", "España", 3.00);
    	insertarJugador("Reinildo Mandava", 4, "Lateral izquierdo", "Mozambique", 5.00);
    	insertarJugador("Javi Galán", 4, "Lateral izquierdo", "España", 7.00);
    	insertarJugador("Nahuel Molina", 4, "Lateral derecho", "Argentina", 30.00);
    	insertarJugador("Conor Gallagher", 4, "Mediocentro", "Inglaterra", 28.00);
    	insertarJugador("Rodrigo De Paul", 4, "Mediocentro", "Argentina", 30.00);
    	insertarJugador("Pablo Barrios", 4, "Mediocentro", "España", 12.00);
    	insertarJugador("Koke", 4, "Mediocentro", "España", 10.00);
    	insertarJugador("Marcos Llorente", 4, "Interior derecho", "España", 20.00);
    	insertarJugador("Samuel Lino", 4, "Interior izquierdo", "Brasil", 15.00);
    	insertarJugador("Thomas Lemar", 4, "Mediocentro ofensivo", "Francia", 15.00);
    	insertarJugador("Rodrigo Riquelme", 4, "Extremo izquierdo", "España", 7.00);
    	insertarJugador("Ángel Correa", 4, "Extremo derecho", "Argentina", 18.00);
    	insertarJugador("Julián Alvarez", 4, "Delantero centro", "Argentina", 75.00);
    	insertarJugador("Antoine Griezmann", 4, "Delantero centro", "Francia", 25.00);
    	insertarJugador("Alexander Sørloth", 4, "Delantero centro", "Noruega", 12.00);
    	insertarJugador("Giuliano Simeone", 4, "Delantero centro", "España", 6.00);
    	insertarJugador("Borja Garcés", 4, "Delantero centro", "España", 1.00);
    	
    	//Real Sociedad
    	insertarJugador("Álex Remiro", 5, "Portero", "España", 25.00);
    	insertarJugador("Unai Marrero", 5, "Portero", "España", 0.30);
    	insertarJugador("Nayef Aguerd", 5, "Defensa central", "Marruecos", 35.00);
    	insertarJugador("Igor Zubeldia", 5, "Defensa central", "España", 22.00);
    	insertarJugador("Jon Pacheco", 5, "Defensa central", "España", 15.00);
    	insertarJugador("Aritz Elustondo", 5, "Defensa central", "España", 5.00);
    	insertarJugador("Jon Martín", 5, "Defensa central", "España", 2.00);
    	insertarJugador("Aihen Muñoz", 5, "Lateral izquierdo", "España", 8.00);
    	insertarJugador("Javi López", 5, "Lateral izquierdo", "España", 7.50);
    	insertarJugador("Hamari Traoré", 5, "Lateral derecho", "Malí", 6.00);
    	insertarJugador("Jon Aramburu", 5, "Lateral derecho", "Venezuela", 5.00);
    	insertarJugador("Álvaro Odriozola", 5, "Lateral derecho", "España", 1.50);
    	insertarJugador("Martín Zubimendi", 5, "Pivote", "España", 60.00);
    	insertarJugador("Urko González de Zarate", 5, "Pivote", "España", 1.20);
    	insertarJugador("Beñat Turrientes", 5, "Mediocentro", "España", 20.00);
    	insertarJugador("Luka Sucic", 5, "Mediocentro", "Croacia", 15.00);
    	insertarJugador("Jon Ander Olasagasti", 5, "Mediocentro", "España", 1.20);
    	insertarJugador("Brais Méndez", 5, "Mediocentro ofensivo", "España", 40.00);
    	insertarJugador("Arsen Zakharyan", 5, "Mediocentro ofensivo", "Rusia", 18.00);
    	insertarJugador("Pablo Marín", 5, "Mediocentro ofensivo", "España", 1.50);
    	insertarJugador("Jon Magunazelaia", 5, "Mediocentro ofensivo", "España", 1.00);
    	insertarJugador("Ander Barrenetxea", 5, "Extremo izquierdo", "España", 20.00);
    	insertarJugador("Sheraldo Becker", 5, "Extremo izquierdo", "Surinam", 7.50);
    	insertarJugador("Takefusa Kubo", 5, "Extremo derecho", "Japón", 50.00);
    	insertarJugador("Sergio Gómez", 5, "Extremo derecho", "España", 10.00);
    	insertarJugador("Mikel Oyarzabal", 5, "Delantero centro", "España", 40.00);
    	insertarJugador("Orri Óskarsson", 5, "Delantero centro", "Islandia", 20.00);
    	insertarJugador("Umar Sadiq", 5, "Delantero centro", "Nigeria", 6.00);

    	//Betis
    	insertarJugador("Rui Silva", 6, "Portero", "Portugal", 8.00);
    	insertarJugador("Adrián", 6, "Portero", "España", 0.60);
    	insertarJugador("Fran Vieites", 6, "Portero", "España", 0.50);
    	insertarJugador("Natan", 6, "Defensa central", "Brasil", 12.00);
    	insertarJugador("Diego Llorente", 6, "Defensa central", "España", 7.00);
    	insertarJugador("Marc Bartra", 6, "Defensa central", "España", 1.00);
    	insertarJugador("Romain Perraud", 6, "Lateral izquierdo", "Francia", 5.00);
    	insertarJugador("Ricardo Rodríguez", 6, "Lateral izquierdo", "Suiza", 3.50);
    	insertarJugador("Héctor Bellerín", 6, "Lateral derecho", "España", 4.00);
    	insertarJugador("Youssouf Sabaly", 6, "Lateral derecho", "Senegal", 2.50);
    	insertarJugador("Johnny Cardoso", 6, "Pivote", "Estados Unidos", 20.00);
    	insertarJugador("Marc Roca", 6, "Pivote", "España", 9.00);
    	insertarJugador("William Carvalho", 6, "Pivote", "Portugal", 3.00);
    	insertarJugador("Giovani Lo Celso", 6, "Mediocentro", "Argentina", 20.00);
    	insertarJugador("Sergi Altimira", 6, "Mediocentro", "España", 3.50);
    	insertarJugador("Pablo Fornals", 6, "Mediocentro ofensivo", "España", 14.00);
    	insertarJugador("Isco", 6, "Mediocentro ofensivo", "España", 8.00);
    	insertarJugador("Abde Ezzalzouli", 6, "Extremo izquierdo", "Marruecos", 12.00);
    	insertarJugador("Juanmi", 6, "Extremo izquierdo", "España", 6.00);
    	insertarJugador("Assane Diao", 6, "Extremo derecho", "España", 12.00);
    	insertarJugador("Aitor Ruibal", 6, "Extremo derecho", "España", 3.50);
    	insertarJugador("Iker Losada", 6, "Mediapunta", "España", 1.80);
    	insertarJugador("Vitor Roque", 6, "Delantero centro", "Brasil", 30.00);
    	insertarJugador("Chimy Ávila", 6, "Delantero centro", "Argentina", 3.00);
    	insertarJugador("Cédric Bakambu", 6, "Delantero centro", "RD del Congo", 2.50);

    	//Sevilla
    	
    	insertarJugador("Álvaro Fernández", 7, "Portero", "España", 2.50);
    	insertarJugador("Ørjan Nyland", 7, "Portero", "Noruega", 1.40);
    	insertarJugador("Loïc Badé", 7, "Defensa central", "Francia", 20.00);
    	insertarJugador("Kike Salas", 7, "Defensa central", "España", 8.00);
    	insertarJugador("Marcão", 7, "Defensa central", "Brasil", 4.00);
    	insertarJugador("Tanguy Nianzou", 7, "Defensa central", "Francia", 3.00);
    	insertarJugador("Valentín Barco", 7, "Lateral izquierdo", "Argentina", 10.00);
    	insertarJugador("Adrià Pedrosa", 7, "Lateral izquierdo", "España", 6.00);
    	insertarJugador("Juanlu Sánchez", 7, "Lateral derecho", "España", 15.00);
    	insertarJugador("Gonzalo Montiel", 7, "Lateral derecho", "Argentina", 8.00);
    	insertarJugador("José Ángel Carmona", 7, "Lateral derecho", "España", 5.00);
    	insertarJugador("Jesús Navas", 7, "Lateral derecho", "España", 2.50);
    	insertarJugador("Lucien Agoumé", 7, "Pivote", "Francia", 6.00);
    	insertarJugador("Nemanja Gudelj", 7, "Pivote", "Serbia", 3.50);
    	insertarJugador("Albert Sambi Lokonga", 7, "Mediocentro", "Bélgica", 12.00);
    	insertarJugador("Djibril Sow", 7, "Mediocentro", "Suiza", 7.50);
    	insertarJugador("Saúl Ñíguez", 7, "Mediocentro", "España", 5.00);
    	insertarJugador("Manu Bueno", 7, "Mediocentro", "España", 0.50);
    	insertarJugador("Pedro Ortiz", 7, "Mediocentro", "España", 0.40);
    	insertarJugador("Chidera Ejuke", 7, "Extremo izquierdo", "Nigeria", 6.00);
    	insertarJugador("Dodi Lukébakio", 7, "Extremo derecho", "Bélgica", 12.00);
    	insertarJugador("Suso", 7, "Extremo derecho", "España", 3.00);
    	insertarJugador("Peque Fernández", 7, "Mediapunta", "España", 5.00);
    	insertarJugador("Isaac Romero", 7, "Delantero centro", "España", 18.00);
    	insertarJugador("Kelechi Iheanacho", 7, "Delantero centro", "Nigeria", 9.00);

    	
    	//Valencia
    	
    	insertarJugador("Giorgi Mamardashvili", 8, "Portero", "Georgia", 45.00);
    	insertarJugador("Stole Dimitrievski", 8, "Portero", "Macedonia del Norte", 4.00);
    	insertarJugador("Jaume Doménech", 8, "Portero", "España", 0.80);
    	insertarJugador("Cristhian Mosquera", 8, "Defensa central", "España", 30.00);
    	insertarJugador("Yarek Gasiorowski", 8, "Defensa central", "España", 15.00);
    	insertarJugador("César Tárrega", 8, "Defensa central", "España", 4.00);
    	insertarJugador("Maximiliano Caufriez", 8, "Defensa central", "Bélgica", 2.50);
    	insertarJugador("Mouctar Diakhaby", 8, "Defensa central", "Guinea", 2.00);
    	insertarJugador("José Gayà", 8, "Lateral izquierdo", "España", 20.00);
    	insertarJugador("Jesús Vázquez", 8, "Lateral izquierdo", "España", 4.00);
    	insertarJugador("Thierry Correia", 8, "Lateral derecho", "Portugal", 6.00);
    	insertarJugador("Dimitri Foulquier", 8, "Lateral derecho", "Guadalupe", 2.50);
    	insertarJugador("Pepelu", 8, "Pivote", "España", 22.00);
    	insertarJugador("Hugo Guillamón", 8, "Pivote", "España", 8.00);
    	insertarJugador("Enzo Barrenechea", 8, "Pivote", "Argentina", 7.50);
    	insertarJugador("Javi Guerra", 8, "Mediocentro", "España", 20.00);
    	insertarJugador("André Almeida", 8, "Mediocentro ofensivo", "Portugal", 15.00);
    	insertarJugador("Luis Rioja", 8, "Extremo izquierdo", "España", 4.00);
    	insertarJugador("Diego López", 8, "Extremo derecho", "España", 12.00);
    	insertarJugador("Fran Pérez", 8, "Extremo derecho", "España", 6.00);
    	insertarJugador("Sergi Canós", 8, "Extremo derecho", "España", 2.50);
    	insertarJugador("Germán Valera", 8, "Extremo derecho", "España", 2.00);
    	insertarJugador("Hugo Duro", 8, "Delantero centro", "España", 16.00);
    	insertarJugador("Dani Gómez", 8, "Delantero centro", "España", 1.20);
    	insertarJugador("Rafa Mir", 8, "Delantero centro", "España", 1.00);

    	
    	//Villareal
    	
    	insertarJugador("Luiz Júnior", 9, "Portero", "Brasil", 6.00);
    	insertarJugador("Diego Conde", 9, "Portero", "España", 5.00);
    	insertarJugador("Iker Álvarez", 9, "Portero", "Andorra", 0.80);
    	insertarJugador("Logan Costa", 9, "Defensa central", "Cabo Verde", 18.00);
    	insertarJugador("Willy Kambwala", 9, "Defensa central", "Francia", 5.00);
    	insertarJugador("Eric Bailly", 9, "Defensa central", "Costa de Marfil", 3.00);
    	insertarJugador("Raúl Albiol", 9, "Defensa central", "España", 1.50);
    	insertarJugador("Sergi Cardona", 9, "Lateral izquierdo", "España", 7.00);
    	insertarJugador("Alfonso Pedraza", 9, "Lateral izquierdo", "España", 5.00);
    	insertarJugador("Juan Bernat", 9, "Lateral izquierdo", "España", 4.00);
    	insertarJugador("Juan Foyth", 9, "Lateral derecho", "Argentina", 12.00);
    	insertarJugador("Kiko Femenía", 9, "Lateral derecho", "España", 1.40);
    	insertarJugador("Pape Gueye", 9, "Pivote", "Senegal", 8.00);
    	insertarJugador("Ramón Terrats", 9, "Pivote", "España", 4.00);
    	insertarJugador("Santi Comesaña", 9, "Mediocentro", "España", 8.00);
    	insertarJugador("Dani Parejo", 9, "Mediocentro", "España", 2.50);
    	insertarJugador("Denis Suárez", 9, "Mediocentro ofensivo", "España", 1.50);
    	insertarJugador("Álex Baena", 9, "Extremo izquierdo", "España", 50.00);
    	insertarJugador("Ayoze Pérez", 9, "Extremo izquierdo", "España", 10.00);
    	insertarJugador("Yéremy Pino", 9, "Extremo derecho", "España", 30.00);
    	insertarJugador("Ilias Akhomach", 9, "Extremo derecho", "Marruecos", 15.00);
    	insertarJugador("Nicolas Pépé", 9, "Extremo derecho", "Costa de Marfil", 9.00);
    	insertarJugador("Thierno Barry", 9, "Delantero centro", "Francia", 14.00);
    	insertarJugador("Gerard Moreno", 9, "Delantero centro", "España", 10.00);

    	
    	//Celta
    	
    	insertarJugador("Iván Villar", 10, "Portero", "España", 1.20);
    	insertarJugador("Vicente Guaita", 10, "Portero", "España", 0.80);
    	insertarJugador("Carl Starfelt", 10, "Defensa central", "Suecia", 6.00);
    	insertarJugador("Joseph Aidoo", 10, "Defensa central", "Ghana", 3.00);
    	insertarJugador("Carlos Domínguez", 10, "Defensa central", "España", 3.00);
    	insertarJugador("Javi Rodríguez", 10, "Defensa central", "España", 3.00);
    	insertarJugador("Marcos Alonso", 10, "Lateral izquierdo", "España", 2.00);
    	insertarJugador("Mihailo Ristic", 10, "Lateral izquierdo", "Serbia", 1.00);
    	insertarJugador("Óscar Mingueza", 10, "Lateral derecho", "España", 12.00);
    	insertarJugador("Javier Manquillo", 10, "Lateral derecho", "España", 1.80);
    	insertarJugador("Sergio Carreira", 10, "Lateral derecho", "España", 1.20);
    	insertarJugador("Damián Rodríguez", 10, "Pivote", "España", 3.00);
    	insertarJugador("Jailson", 10, "Pivote", "Brasil", 1.50);
    	insertarJugador("Fran Beltrán", 10, "Mediocentro", "España", 6.00);
    	insertarJugador("Ilaix Moriba", 10, "Mediocentro", "Guinea", 3.00);
    	insertarJugador("Luca de la Torre", 10, "Mediocentro", "Estados Unidos", 3.00);
    	insertarJugador("Hugo Sotelo", 10, "Mediocentro", "España", 2.50);
    	insertarJugador("Jonathan Bamba", 10, "Extremo izquierdo", "Costa de Marfil", 6.00);
    	insertarJugador("Williot Swedberg", 10, "Extremo izquierdo", "Suecia", 5.00);
    	insertarJugador("Hugo Álvarez", 10, "Extremo izquierdo", "España", 3.00);
    	insertarJugador("Franco Cervi", 10, "Extremo izquierdo", "Argentina", 1.00);
    	insertarJugador("Alfon González", 10, "Extremo izquierdo", "España", 0.30);
    	insertarJugador("Tadeo Allende", 10, "Extremo derecho", "Argentina", 3.00);
    	insertarJugador("Anastasios Douvikas", 10, "Delantero centro", "Grecia", 6.00);
    	insertarJugador("Borja Iglesias", 10, "Delantero centro", "España", 5.00);
    	insertarJugador("Iago Aspas", 10, "Delantero centro", "España", 3.00);
    	insertarJugador("Pablo Durán", 10, "Delantero centro", "España", 0.30);

    	
    	//Getafe
    	
    	insertarJugador("David Soria", 11, "Portero", "España", 5.00);
    	insertarJugador("Jiri Letacek", 11, "Portero", "República Checa", 2.00);
    	insertarJugador("Omar Alderete", 11, "Defensa central", "Paraguay", 5.00);
    	insertarJugador("Djené", 11, "Defensa central", "Togo", 2.50);
    	insertarJugador("Juan Berrocal", 11, "Defensa central", "España", 2.20);
    	insertarJugador("Domingos Duarte", 11, "Defensa central", "Portugal", 1.00);
    	insertarJugador("Nabil Aberdin", 11, "Defensa central", "Marruecos", 0.60);
    	insertarJugador("Diego Rico", 11, "Lateral izquierdo", "España", 1.50);
    	insertarJugador("Fabrizio Angileri", 11, "Lateral izquierdo", "Argentina", 1.00);
    	insertarJugador("Juan Iglesias", 11, "Lateral derecho", "España", 2.80);
    	insertarJugador("Allan Nyom", 11, "Lateral derecho", "Camerún", 0.50);
    	insertarJugador("Mauro Arambarri", 11, "Mediocentro", "Uruguay", 5.00);
    	insertarJugador("Luis Milla", 11, "Mediocentro", "España", 3.50);
    	insertarJugador("Carles Aleñá", 11, "Mediocentro", "España", 3.00);
    	insertarJugador("Yellu Santiago", 11, "Mediocentro", "España", 1.00);
    	insertarJugador("John Patrick", 11, "Mediocentro", "Irlanda", 0.20);
    	insertarJugador("Christantus Uche", 11, "Mediocentro ofensivo", "Nigeria", 5.00);
    	insertarJugador("Álex Sola", 11, "Extremo derecho", "España", 4.00);
    	insertarJugador("Carles Pérez", 11, "Extremo derecho", "España", 3.00);
    	insertarJugador("Peter Federico", 11, "Extremo derecho", "República Dominicana", 3.00);
    	insertarJugador("Borja Mayoral", 11, "Delantero centro", "España", 15.00);
    	insertarJugador("Bertuğ Yıldırım", 11, "Delantero centro", "Turquía", 3.50);
    	insertarJugador("Álvaro Rodríguez", 11, "Delantero centro", "Uruguay", 3.50);

    	
    	//Rayo
    	
    	insertarJugador("Augusto Batalla", 12, "Portero", "Argentina", 5.00);
    	insertarJugador("Dani Cárdenas", 12, "Portero", "España", 1.80);
    	insertarJugador("Abdul Mumin", 12, "Defensa central", "Ghana", 4.00);
    	insertarJugador("Florian Lejeune", 12, "Defensa central", "Francia", 3.00);
    	insertarJugador("Aridane Hernández", 12, "Defensa central", "España", 0.50);
    	insertarJugador("Pelayo Fernández", 12, "Defensa central", "España", 0.50);
    	insertarJugador("Alfonso Espino", 12, "Lateral izquierdo", "Uruguay", 2.40);
    	insertarJugador("Pep Chavarría", 12, "Lateral izquierdo", "España", 2.00);
    	insertarJugador("Andrei Rațiu", 12, "Lateral derecho", "Rumania", 4.00);
    	insertarJugador("Iván Balliu", 12, "Lateral derecho", "Albania", 2.00);
    	insertarJugador("Gerard Gumbau", 12, "Pivote", "España", 3.00);
    	insertarJugador("Óscar Valentín", 12, "Mediocentro", "España", 4.00);
    	insertarJugador("Unai López", 12, "Mediocentro", "España", 3.00);
    	insertarJugador("Pedro Díaz", 12, "Mediocentro", "España", 2.50);
    	insertarJugador("Pathé Ciss", 12, "Mediocentro", "Senegal", 2.50);
    	insertarJugador("James Rodríguez", 12, "Mediocentro ofensivo", "Colombia", 5.00);
    	insertarJugador("Randy Nteka", 12, "Mediocentro ofensivo", "Angola", 1.50);
    	insertarJugador("Óscar Trejo", 12, "Mediocentro ofensivo", "Argentina", 1.00);
    	insertarJugador("Joni Montiel", 12, "Mediocentro ofensivo", "España", 1.00);
    	insertarJugador("Álvaro García", 12, "Extremo izquierdo", "España", 3.00);
    	insertarJugador("Isi Palazón", 12, "Extremo derecho", "España", 8.00);
    	insertarJugador("Jorge de Frutos", 12, "Extremo derecho", "España", 4.00);
    	insertarJugador("Adrián Embarba", 12, "Extremo derecho", "España", 1.80);
    	insertarJugador("Sergio Camello", 12, "Delantero centro", "España", 4.00);
    	insertarJugador("Raúl de Tomás", 12, "Delantero centro", "España", 1.00);
    	insertarJugador("Sergi Guardiola", 12, "Delantero centro", "España", 0.80);

    	
    	//Las Palmas
    	
    	insertarJugador("Álvaro Valles", 13, "Portero", "España", 7.50);
    	insertarJugador("Dinko Horkas", 13, "Portero", "Croacia", 1.20);
    	insertarJugador("Jasper Cillessen", 13, "Portero", "Países Bajos", 1.00);
    	insertarJugador("Mika Mármol", 13, "Defensa central", "España", 15.00);
    	insertarJugador("Scott McKenna", 13, "Defensa central", "Escocia", 9.00);
    	insertarJugador("Alex Suárez", 13, "Defensa central", "España", 1.20);
    	insertarJugador("Juanma Herzog", 13, "Defensa central", "España", 1.00);
    	insertarJugador("Álex Muñoz", 13, "Lateral izquierdo", "España", 1.20);
    	insertarJugador("Daley Sinkgraven", 13, "Lateral izquierdo", "Países Bajos", 1.00);
    	insertarJugador("Marvin Park", 13, "Lateral derecho", "España", 2.00);
    	insertarJugador("Viti Rozada", 13, "Lateral derecho", "España", 1.50);
    	insertarJugador("Dário Essugo", 13, "Pivote", "Portugal", 3.50);
    	insertarJugador("Enzo Loiodice", 13, "Mediocentro", "Francia", 4.00);
    	insertarJugador("Javi Muñoz", 13, "Mediocentro", "España", 3.00);
    	insertarJugador("José Campaña", 13, "Mediocentro", "España", 1.50);
    	insertarJugador("Fabio González", 13, "Mediocentro", "España", 0.50);
    	insertarJugador("Alberto Moleiro", 13, "Mediocentro ofensivo", "España", 20.00);
    	insertarJugador("Kirian Rodríguez", 13, "Mediocentro ofensivo", "España", 12.00);
    	insertarJugador("Iván Gil", 13, "Mediocentro ofensivo", "España", 1.00);
    	insertarJugador("Manu Fuster", 13, "Extremo izquierdo", "España", 2.40);
    	insertarJugador("Pejiño", 13, "Extremo izquierdo", "España", 1.20);
    	insertarJugador("Benito Ramírez", 13, "Extremo izquierdo", "España", 0.60);
    	insertarJugador("Adnan Januzaj", 13, "Extremo derecho", "Bélgica", 2.00);
    	insertarJugador("Fábio Silva", 13, "Delantero centro", "Portugal", 11.00);
    	insertarJugador("Oli McBurnie", 13, "Delantero centro", "Escocia", 4.50);
    	insertarJugador("Sandro Ramírez", 13, "Delantero centro", "España", 1.50);
    	insertarJugador("Marc Cardona", 13, "Delantero centro", "España", 1.40);
    	insertarJugador("Jaime Mata", 13, "Delantero centro", "España", 0.90);

    	
    	
    	//Alaves
    	
    	insertarJugador("Antonio Sivera", 14, "Portero", "España", 6.00);
    	insertarJugador("Jesús Owono", 14, "Portero", "Guinea Ecuatorial", 1.00);
    	insertarJugador("Abdel Abqar", 14, "Defensa central", "Marruecos", 7.50);
    	insertarJugador("Santiago Mouriño", 14, "Defensa central", "Uruguay", 2.00);
    	insertarJugador("Moussa Diarra", 14, "Defensa central", "Malí", 2.00);
    	insertarJugador("Aleksandar Sedlar", 14, "Defensa central", "Serbia", 0.40);
    	insertarJugador("Manu Sánchez", 14, "Lateral izquierdo", "España", 6.00);
    	insertarJugador("Nahuel Tenaglia", 14, "Lateral derecho", "Argentina", 2.50);
    	insertarJugador("Hugo Novoa", 14, "Lateral derecho", "España", 2.50);
    	insertarJugador("Antonio Blanco", 14, "Pivote", "España", 7.50);
    	insertarJugador("Carlos Protesoni", 14, "Pivote", "Uruguay", 1.50);
    	insertarJugador("Ander Guevara", 14, "Mediocentro", "España", 5.00);
    	insertarJugador("Jon Guridi", 14, "Mediocentro", "España", 5.00);
    	insertarJugador("Joan Jordán", 14, "Mediocentro", "España", 3.50);
    	insertarJugador("Tomás Conechny", 14, "Extremo izquierdo", "Argentina", 4.50);
    	insertarJugador("Abde Rebbach", 14, "Extremo izquierdo", "Argelia", 0.80);
    	insertarJugador("Carlos Vicente", 14, "Extremo derecho", "España", 8.00);
    	insertarJugador("Luka Romero", 14, "Extremo derecho", "Argentina", 5.00);
    	insertarJugador("Stoichkov", 14, "Mediapunta", "España", 4.00);
    	insertarJugador("Toni Martínez", 14, "Delantero centro", "España", 5.00);
    	insertarJugador("Carlos Martín", 14, "Delantero centro", "España", 4.00);
    	insertarJugador("Asier Villalibre", 14, "Delantero centro", "España", 2.50);
    	insertarJugador("Kike García", 14, "Delantero centro", "España", 1.00);

    	
    	//Malorca 
    	
    	insertarJugador("Dominik Greif", 15, "Portero", "Eslovaquia", 4.00);
    	insertarJugador("Leo Román", 15, "Portero", "España", 1.80);
    	insertarJugador("Iván Cuéllar", 15, "Portero", "España", 0.10);
    	insertarJugador("Martin Valjent", 15, "Defensa central", "Eslovaquia", 6.00);
    	insertarJugador("José Copete", 15, "Defensa central", "España", 2.80);
    	insertarJugador("Siebe Van der Heyden", 15, "Defensa central", "Bélgica", 2.00);
    	insertarJugador("Antonio Raíllo", 15, "Defensa central", "España", 2.00);
    	insertarJugador("Toni Lato", 15, "Lateral izquierdo", "España", 3.50);
    	insertarJugador("Johan Mojica", 15, "Lateral izquierdo", "Colombia", 2.20);
    	insertarJugador("Pablo Maffeo", 15, "Lateral derecho", "Argentina", 5.00);
    	insertarJugador("Mateu Morey", 15, "Lateral derecho", "España", 0.80);
    	insertarJugador("Samú Costa", 15, "Pivote", "Portugal", 12.00);
    	insertarJugador("Omar Mascarell", 15, "Pivote", "Guinea Ecuatorial", 1.00);
    	insertarJugador("Sergi Darder", 15, "Mediocentro", "España", 6.00);
    	insertarJugador("Manu Morlanes", 15, "Mediocentro", "España", 2.50);
    	insertarJugador("Antonio Sánchez", 15, "Mediocentro", "España", 2.50);
    	insertarJugador("Robert Navarro", 15, "Mediocentro ofensivo", "España", 6.00);
    	insertarJugador("Dani Rodríguez", 15, "Mediocentro ofensivo", "España", 1.00);
    	insertarJugador("Chiquinho", 15, "Extremo izquierdo", "Portugal", 3.00);
    	insertarJugador("Javi Llabrés", 15, "Extremo izquierdo", "España", 0.70);
    	insertarJugador("Takuma Asano", 15, "Extremo derecho", "Japón", 4.00);
    	insertarJugador("Valery Fernández", 15, "Extremo derecho", "España", 2.50);
    	insertarJugador("Vedat Muriqi", 15, "Delantero centro", "Kosovo", 12.00);
    	insertarJugador("Cyle Larin", 15, "Delantero centro", "Canadá", 4.00);
    	insertarJugador("Abdón Prats", 15, "Delantero centro", "España", 1.80);
    	insertarJugador("Marc Domènech", 15, "Delantero centro", "España", 0.50);

    	
    	//Girona
    	
    	insertarJugador("Pau López", 16, "Portero", "España", 7.50);
    	insertarJugador("Paulo Gazzaniga", 16, "Portero", "Argentina", 4.00);
    	insertarJugador("Juan Carlos", 16, "Portero", "España", 0.20);
    	insertarJugador("Ladislav Krejci", 16, "Defensa central", "República Checa", 10.00);
    	insertarJugador("Alejandro Francés", 16, "Defensa central", "España", 5.00);
    	insertarJugador("Daley Blind", 16, "Defensa central", "Países Bajos", 3.00);
    	insertarJugador("David López", 16, "Defensa central", "España", 2.50);
    	insertarJugador("Juanpe", 16, "Defensa central", "España", 1.00);
    	insertarJugador("Miguel Gutiérrez", 16, "Lateral izquierdo", "España", 25.00);
    	insertarJugador("Arnau Martínez", 16, "Lateral derecho", "España", 10.00);
    	insertarJugador("Oriol Romeu", 16, "Pivote", "España", 2.00);
    	insertarJugador("Yangel Herrera", 16, "Mediocentro", "Venezuela", 25.00);
    	insertarJugador("Donny van de Beek", 16, "Mediocentro", "Países Bajos", 5.00);
    	insertarJugador("Jhon Solís", 16, "Mediocentro", "Colombia", 4.00);
    	insertarJugador("Iván Martín", 16, "Mediocentro ofensivo", "España", 15.00);
    	insertarJugador("Gabriel Misehouy", 16, "Mediocentro ofensivo", "Países Bajos", 1.00);
    	insertarJugador("Bryan Gil", 16, "Extremo izquierdo", "España", 14.00);
    	insertarJugador("Arnaut Danjuma", 16, "Extremo izquierdo", "Países Bajos", 13.00);
    	insertarJugador("Viktor Tsygankov", 16, "Extremo derecho", "Ucrania", 30.00);
    	insertarJugador("Yaser Asprilla", 16, "Extremo derecho", "Colombia", 18.00);
    	insertarJugador("Portu", 16, "Extremo derecho", "España", 3.50);
    	insertarJugador("Abel Ruiz", 16, "Delantero centro", "España", 12.00);
    	insertarJugador("Bojan Miovski", 16, "Delantero centro", "Macedonia del Norte", 4.00);
    	insertarJugador("Cristhian Stuani", 16, "Delantero centro", "Uruguay", 1.80);

    	
    	//Osasuna
    	
    	insertarJugador("Sergio Herrera", 17, "Portero", "España", 6.00);
    	insertarJugador("Aitor Fernández", 17, "Portero", "España", 1.80);
    	insertarJugador("Enzo Boyomo", 17, "Defensa central", "Camerún", 10.00);
    	insertarJugador("Jorge Herrando", 17, "Defensa central", "España", 6.00);
    	insertarJugador("Alejandro Catena", 17, "Defensa central", "España", 4.00);
    	insertarJugador("Unai García", 17, "Defensa central", "España", 1.20);
    	insertarJugador("Abel Bretones", 17, "Lateral izquierdo", "España", 3.00);
    	insertarJugador("Juan Cruz", 17, "Lateral izquierdo", "España", 2.20);
    	insertarJugador("Jesús Areso", 17, "Lateral derecho", "España", 7.50);
    	insertarJugador("Rubén Peña", 17, "Lateral derecho", "España", 2.20);
    	insertarJugador("Nacho Vidal", 17, "Lateral derecho", "España", 2.00);
    	insertarJugador("Iker Muñoz", 17, "Pivote", "España", 5.00);
    	insertarJugador("Lucas Torró", 17, "Pivote", "España", 4.00);
    	insertarJugador("Jon Moncayola", 17, "Mediocentro", "España", 7.00);
    	insertarJugador("Pablo Ibáñez", 17, "Mediocentro", "España", 2.50);
    	insertarJugador("Aimar Oroz", 17, "Mediocentro ofensivo", "España", 15.00);
    	insertarJugador("Javi Martínez", 17, "Mediocentro ofensivo", "España", 1.20);
    	insertarJugador("Bryan Zaragoza", 17, "Extremo izquierdo", "España", 12.00);
    	insertarJugador("Moi Gómez", 17, "Extremo izquierdo", "España", 3.50);
    	insertarJugador("José Manuel Arnáiz", 17, "Extremo izquierdo", "España", 2.00);
    	insertarJugador("Rubén García", 17, "Extremo derecho", "España", 2.50);
    	insertarJugador("Kike Barja", 17, "Extremo derecho", "España", 2.00);
    	insertarJugador("Iker Benito", 17, "Extremo derecho", "España", 0.80);
    	insertarJugador("Ante Budimir", 17, "Delantero centro", "Croacia", 5.00);
    	insertarJugador("Raúl García", 17, "Delantero centro", "España", 4.00);

    	
    	//Espayol
    	
    	insertarJugador("Joan García", 18, "Portero", "España", 10.00);
    	insertarJugador("Fernando Pacheco", 18, "Portero", "España", 1.00);
    	insertarJugador("Ángel Fortuño", 18, "Portero", "España", 0.30);
    	insertarJugador("Marash Kumbulla", 18, "Defensa central", "Albania", 4.50);
    	insertarJugador("Fernando Calero", 18, "Defensa central", "España", 1.80);
    	insertarJugador("Leandro Cabrera", 18, "Defensa central", "Uruguay", 1.20);
    	insertarJugador("Sergi Gómez", 18, "Defensa central", "España", 1.00);
    	insertarJugador("Brian Oliván", 18, "Lateral izquierdo", "España", 2.50);
    	insertarJugador("Carlos Romero", 18, "Lateral izquierdo", "España", 2.50);
    	insertarJugador("Omar El Hilali", 18, "Lateral derecho", "Marruecos", 3.00);
    	insertarJugador("Álvaro Tejero", 18, "Lateral derecho", "España", 2.00);
    	insertarJugador("Alex Král", 18, "Mediocentro", "República Checa", 6.00);
    	insertarJugador("Álvaro Aguado", 18, "Mediocentro", "España", 2.80);
    	insertarJugador("José Gragera", 18, "Mediocentro", "España", 2.50);
    	insertarJugador("Edu Expósito", 18, "Mediocentro", "España", 2.00);
    	insertarJugador("Pol Lozano", 18, "Mediocentro", "España", 2.00);
    	insertarJugador("Javi Puado", 18, "Extremo izquierdo", "España", 8.00);
    	insertarJugador("Naci Ünüvar", 18, "Extremo izquierdo", "Turquía", 2.50);
    	insertarJugador("Pere Milla", 18, "Extremo izquierdo", "España", 2.00);
    	insertarJugador("Jofre Carreras", 18, "Extremo derecho", "España", 2.50);
    	insertarJugador("Salvi Sánchez", 18, "Extremo derecho", "España", 1.20);
    	insertarJugador("Antoniu Roca", 18, "Extremo derecho", "España", 0.20);
    	insertarJugador("Alejo Veliz", 18, "Delantero centro", "Argentina", 9.00);
    	insertarJugador("Walid Cheddira", 18, "Delantero centro", "Marruecos", 6.00);
    	insertarJugador("Irvin Cardona", 18, "Delantero centro", "Francia", 2.50);
    	insertarJugador("Omar Sadik", 18, "Delantero centro", "Marruecos", 0.30);

    	
    	//Valladolid 
    	
    	insertarJugador("Karl Hein", 19, "Portero", "Estonia", 3.00);
    	insertarJugador("André Ferreira", 19, "Portero", "Portugal", 0.80);
    	insertarJugador("Eray Cömert", 19, "Defensa central", "Suiza", 4.00);
    	insertarJugador("Cenk Özkacar", 19, "Defensa central", "Turquía", 2.50);
    	insertarJugador("Javi Sánchez", 19, "Defensa central", "España", 1.50);
    	insertarJugador("David Torres", 19, "Defensa central", "España", 1.00);
    	insertarJugador("Luis Pérez", 19, "Lateral derecho", "España", 2.00);
    	insertarJugador("Lucas Rosa", 19, "Lateral derecho", "Brasil", 2.00);
    	insertarJugador("Stanko Juric", 19, "Pivote", "Croacia", 2.20);
    	insertarJugador("Mario Martín", 19, "Pivote", "España", 0.50);
    	insertarJugador("César de la Hoz", 19, "Pivote", "España", 0.50);
    	insertarJugador("Víctor Meseguer", 19, "Mediocentro", "España", 3.00);
    	insertarJugador("Kike Pérez", 19, "Mediocentro", "España", 3.00);
    	insertarJugador("Selim Amallah", 19, "Mediocentro ofensivo", "Marruecos", 1.50);
    	insertarJugador("Chuki", 19, "Mediocentro ofensivo", "España", 0.50);
    	insertarJugador("Raúl Moro", 19, "Extremo izquierdo", "España", 4.00);
    	insertarJugador("Darwin Machís", 19, "Extremo izquierdo", "Venezuela", 1.50);
    	insertarJugador("Kenedy", 19, "Extremo izquierdo", "Brasil", 1.00);
    	insertarJugador("Anuar", 19, "Extremo izquierdo", "Marruecos", 0.90);
    	insertarJugador("Amath Ndiaye", 19, "Extremo derecho", "Senegal", 1.50);
    	insertarJugador("Iván Sánchez", 19, "Extremo derecho", "España", 1.00);
    	insertarJugador("Juanmi Latasa", 19, "Delantero centro", "España", 2.80);
    	insertarJugador("Mamadou Sylla", 19, "Delantero centro", "Senegal", 1.50);
    	insertarJugador("Marcos André", 19, "Delantero centro", "Brasil", 1.50);

    	
    	//Leganes
    	
    	insertarJugador("Juan Soriano", 20, "Portero", "España", 1.50);
    	insertarJugador("Marko Dmitrovic", 20, "Portero", "Serbia", 0.90);
    	insertarJugador("Alvin Abajas", 20, "Portero", "España", 0.025);
    	insertarJugador("Jackson Porozo", 20, "Defensa central", "Ecuador", 2.50);
    	insertarJugador("Sergio González", 20, "Defensa central", "España", 1.80);
    	insertarJugador("Jorge Sáenz", 20, "Defensa central", "España", 1.50);
    	insertarJugador("Matija Nastasic", 20, "Defensa central", "Serbia", 1.50);
    	insertarJugador("Javi Hernández", 20, "Lateral izquierdo", "España", 2.50);
    	insertarJugador("Enric Franquesa", 20, "Lateral izquierdo", "España", 1.50);
    	insertarJugador("Valentin Rosier", 20, "Lateral derecho", "Francia", 4.00);
    	insertarJugador("Adrià Altimira", 20, "Lateral derecho", "España", 2.00);
    	insertarJugador("Renato Tapia", 20, "Pivote", "Perú", 5.00);
    	insertarJugador("Yvan Neyou", 20, "Pivote", "Camerún", 2.00);
    	insertarJugador("Julián Chicco", 20, "Pivote", "Argentina", 1.00);
    	insertarJugador("Seydouba Cissé", 20, "Mediocentro", "Guinea", 2.00);
    	insertarJugador("Darko Brasanac", 20, "Mediocentro", "Serbia", 1.40);
    	insertarJugador("Óscar Rodríguez", 20, "Mediocentro ofensivo", "España", 2.50);
    	insertarJugador("Roberto López", 20, "Mediocentro ofensivo", "España", 2.50);
    	insertarJugador("Juan Cruz", 20, "Extremo derecho", "España", 3.00);
    	insertarJugador("Munir El Haddadi", 20, "Extremo derecho", "Marruecos", 2.50);
    	insertarJugador("Dani Raba", 20, "Extremo derecho", "España", 2.20);
    	insertarJugador("Naim García", 20, "Extremo derecho", "España", 0.50);
    	insertarJugador("Sébastien Haller", 20, "Delantero centro", "Costa de Marfil", 7.50);
    	insertarJugador("Miguel de la Fuente", 20, "Delantero centro", "España", 2.50);
    	insertarJugador("Diego García", 20, "Delantero centro", "España", 2.00);


    	// Partido 1
    	insertarPartido(1, 2, 2, 1, 11); // Athletic Club vs FC Barcelona
    	insertarEstadisticasPartido(24, 1, 1, 1, 2, 0, 0); // Iñaki Williams (Athletic) - 1 gol, 1 asistencia, 2 regates
    	insertarEstadisticasPartido(51, 1, 1, 0, 1, 0, 0); // Robert Lewandowski (Barcelona) - 1 gol, 1 regate

    	// Partido 2
    	insertarPartido(3, 4, 1, 1, 12); // Real Madrid vs Atlético de Madrid
    	insertarEstadisticasPartido(66, 2, 0, 1, 0, 1, 0); // Luka Modric (Real Madrid) - 1 asistencia, 1 regate
    	insertarEstadisticasPartido(95, 2, 1, 0, 2, 0, 0); // Antoine Griezmann (Atlético) - 1 gol, 2 regates

    	// Partido 3
    	insertarPartido(5, 6, 2, 0, 13); // Real Sociedad vs Real Betis
    	insertarEstadisticasPartido(124, 3, 1, 0, 3, 0, 0); // Mikel Oyarzabal (Real Sociedad) - 1 gol, 3 regates
    	insertarEstadisticasPartido(143, 3, 0, 0, 2, 1, 0); // Isco (Real Betis) - 2 regates, 1 tarjeta amarilla

    	// Partido 4
    	insertarPartido(7, 8, 1, 2, 14); // Sevilla FC vs Valencia CF
    	insertarEstadisticasPartido(173, 4, 0, 1, 1, 0, 0); // Suso (Sevilla) - 1 asistencia, 1 regate
    	insertarEstadisticasPartido(199, 4, 1, 0, 0, 0, 0); // Hugo Duro (Valencia) - 1 gol

    	// Partido 5
    	insertarPartido(9, 10, 3, 0, 15); // Villarreal CF vs Celta de Vigo
    	insertarEstadisticasPartido(225, 5, 1, 0, 4, 0, 0); // Gerard Moreno (Villarreal) - 1 gol, 4 regates
    	insertarEstadisticasPartido(251, 5, 0, 0, 2, 1, 0); // Iago Aspas (Celta) - 2 regates, 1 tarjeta amarilla

    	// Partido 6
    	insertarPartido(11, 12, 2, 2, 16); // Getafe CF vs Rayo Vallecano
    	insertarEstadisticasPartido(273, 6, 0, 1, 1, 0, 0); // Borja Mayoral (Getafe) - 1 asistencia, 1 regate
    	insertarEstadisticasPartido(296, 6, 1, 0, 3, 0, 0); // Isi Palazón (Rayo) - 1 gol, 3 regates

    	// Partido 7
    	insertarPartido(13, 14, 4, 1, 17); // UD Las Palmas vs Alavés
    	insertarEstadisticasPartido(149, 7, 2, 0, 5, 0, 0); // Vitor Roque (Las Palmas) - 2 goles, 5 regates
    	insertarEstadisticasPartido(351, 7, 0, 0, 3, 1, 0); // Asier Villalibre (Alavés) - 3 regates, 1 tarjeta amarilla

    	// Partido 8
    	insertarPartido(15, 16, 2, 3, 18); // Mallorca vs Girona
    	insertarEstadisticasPartido(375, 8, 1, 1, 2, 0, 0); // Vedat Muriqi (Mallorca) - 1 gol, 1 asistencia, 2 regates
    	insertarEstadisticasPartido(402, 8, 1, 1, 3, 0, 0); // Cristhian Stuani (Girona) - 1 gol, 1 asistencia, 3 regates

    	// Partido 9
    	insertarPartido(17, 18, 1, 1, 19); // Osasuna vs Espanyol
    	insertarEstadisticasPartido(426, 9, 0, 0, 2, 0, 1); // Ante Budimir (Osasuna) - 2 regates, 1 tarjeta roja
    	insertarEstadisticasPartido(444, 9, 1, 0, 1, 0, 0); // Javi Puado (Espanyol) - 1 gol, 1 regate

    	// Partido 10
    	insertarPartido(19, 20, 2, 0, 20); // Real Valladolid vs Leganés
    	insertarEstadisticasPartido(477, 10, 1, 0, 1, 0, 0); // Marcos André (Valladolid) - 1 gol, 1 regate
    	insertarEstadisticasPartido(485, 10, 0, 0, 2, 0, 0); // Javi Hernández (Leganés) - 2 regates

    	// Partido 11
    	insertarPartido(2, 17, 3, 1, 21); // FC Barcelona vs Osasuna
    	insertarEstadisticasPartido(42, 11, 1, 1, 2, 0, 0); // Pedri (Barcelona) - 1 gol, 1 asistencia, 2 regates
    	insertarEstadisticasPartido(426, 11, 0, 0, 3, 1, 0); // Ante Budimir (Osasuna) - 3 regates, 1 tarjeta amarilla

    	// Partido 12
    	insertarPartido(3, 5, 2, 0, 22); // Real Madrid vs Real Sociedad
    	insertarEstadisticasPartido(66, 12, 0, 1, 1, 0, 0); // Luka Modric (Real Madrid) - 1 asistencia, 1 regate
    	insertarEstadisticasPartido(124, 12, 1, 0, 3, 0, 0); // Mikel Oyarzabal (Real Sociedad) - 1 gol, 3 regates

    	// Partido 13
    	insertarPartido(4, 6, 2, 2, 23); // Atlético de Madrid vs Real Betis
    	insertarEstadisticasPartido(95, 13, 1, 1, 4, 0, 0); // Antoine Griezmann (Atlético) - 1 gol, 1 asistencia, 4 regates
    	insertarEstadisticasPartido(144, 13, 0, 0, 2, 1, 0); // Abde (Real Betis) - 2 regates, 1 tarjeta amarilla

    	// Partido 14
    	insertarPartido(6, 8, 1, 3, 24); // Real Betis vs Valencia
    	insertarEstadisticasPartido(144, 14, 0, 1, 2, 1, 0); // Abde (Real Betis) - 1 asistencia, 2 regates, 1 amarilla
    	insertarEstadisticasPartido(199, 14, 2, 0, 4, 0, 0); // Hugo Duro (Valencia) - 2 goles, 4 regates

    	// Partido 15
    	insertarPartido(2, 4, 2, 2, 25); // FC Barcelona vs Atlético de Madrid
    	insertarEstadisticasPartido(51, 15, 1, 0, 3, 0, 0); // Robert Lewandowski (Barcelona) - 1 gol, 3 regates
    	insertarEstadisticasPartido(95, 15, 1, 1, 3, 0, 0); // Antoine Griezmann (Atlético) - 1 gol, 1 asistencia, 3 regates

    	// Partido 16
    	insertarPartido(9, 5, 0, 1, 26); // Villarreal vs Real Sociedad
    	insertarEstadisticasPartido(225, 16, 0, 0, 2, 1, 0); // Gerard Moreno (Villarreal) - 2 regates, 1 amarilla
    	insertarEstadisticasPartido(124, 16, 1, 0, 4, 0, 0); // Mikel Oyarzabal (Real Sociedad) - 1 gol, 4 regates

    	// Partido 17
    	insertarPartido(13, 15, 1, 2, 27); // Las Palmas vs Girona
    	insertarEstadisticasPartido(149, 17, 1, 0, 5, 0, 0); // Vitor Roque (Las Palmas) - 1 gol, 5 regates
    	insertarEstadisticasPartido(402, 17, 1, 1, 3, 0, 0); // Cristhian Stuani (Girona) - 1 gol, 1 asistencia, 3 regates

    	// Partido 18
    	insertarPartido(10, 20, 0, 0, 28); // Celta de Vigo vs Leganés
    	insertarEstadisticasPartido(251, 18, 0, 0, 2, 1, 0); // Iago Aspas (Celta) - 2 regates, 1 amarilla
    	insertarEstadisticasPartido(485, 18, 0, 0, 1, 0, 0); // Javi Hernández (Leganés) - 1 regate

    	// Partido 19
    	insertarPartido(7, 1, 2, 1, 29); // Sevilla vs Athletic Club
    	insertarEstadisticasPartido(173, 19, 0, 2, 3, 0, 0); // Suso (Sevilla) - 2 asistencias, 3 regates
    	insertarEstadisticasPartido(24, 19, 1, 0, 3, 0, 0); // Iñaki Williams (Athletic) - 1 gol, 3 regates

    	// Partido 20
    	insertarPartido(18, 12, 1, 3, 30); // Espanyol vs Rayo Vallecano
    	insertarEstadisticasPartido(444, 20, 1, 0, 2, 0, 0); // Javi Puado (Espanyol) - 1 gol, 2 regates
    	insertarEstadisticasPartido(296, 20, 2, 1, 4, 0, 0); // Isi Palazón (Rayo) - 2 goles, 1 asistencia, 4 regates


    	
    }
    
    

}

