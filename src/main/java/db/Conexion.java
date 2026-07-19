package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:postgresql://db.crkgqqtubxhtjnptwxgx.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Iblame2007**";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("Driver de PostgreSQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar a Supabase: " + e.getMessage());
        }
        return conexion;
    }
}