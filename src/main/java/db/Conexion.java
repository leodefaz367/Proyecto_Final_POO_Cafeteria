package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:6543/postgres";
    private static final String USER = "postgres.crkgqqtubxhtjnptwxgx";
    private static final String PASSWORD = "Iblame2007**";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("Driver de PostgreSQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            //System.err.println("Error al conectar a Supabase: " + e.getMessage());
        }
        return conexion;
    }
}