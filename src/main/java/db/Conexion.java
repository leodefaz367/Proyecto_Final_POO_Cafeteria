package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/cafeteria";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Iblame2007*";

    public static Connection conectar() {
        try {
            System.out.println("Conexión exitosa a la base de datos.");
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return null;
        }
    }
}