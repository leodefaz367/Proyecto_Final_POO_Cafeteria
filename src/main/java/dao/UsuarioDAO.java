package dao;

import db.Conexion;
import model.Administrador;
import model.Cajero;
import model.Reportes;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario iniciarSesion(String correo, String clave) {
        Usuario usuario = null;
        // LÓGICA DE NEGOCIO: Solo permitimos el login si el estado es true (activo)
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND clave = ? AND estado = true";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String correoBD = rs.getString("correo");
                String claveBD = rs.getString("clave");
                String rolBD = rs.getString("rol");

                if (rolBD.equals("Administrador")) {
                    usuario = new Administrador(id, nombre, correoBD, claveBD);
                } else if (rolBD.equals("Cajero")) {
                    usuario = new Cajero(id, nombre, correoBD, claveBD);
                } else if (rolBD.equals("Reportes")) {
                    usuario = new Reportes(id, nombre, correoBD, claveBD);
                } else {
                    usuario = new Usuario(id, nombre, correoBD, claveBD, rolBD) {
                        @Override
                        public String obtenerPermisos() {
                            return "Acceso a " + rolBD;
                        }
                    };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }
}