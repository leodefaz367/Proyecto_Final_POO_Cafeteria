package dao;

import db.Conexion;
import model.Administrador;
import model.Cajero;
import model.Reportes;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class UsuarioDAO implements ICRUD<Usuario>{

    public Usuario autenticarUsuario(String correo, String clave){
        String sql = "select * from usuarios where correo=? and clave=? and estado=true";

        try(Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, correo);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                boolean estado = rs.getBoolean("estado");

                if (rol.equals("Administrador")) return new Administrador(id, nombre, correo, clave, rol, estado);
                if (rol.equals("Cajero")) return new Cajero(id, nombre, correo, clave, rol, estado);
                if (rol.equals("Reportes")) return new Reportes(id, nombre, correo, clave, rol, estado);
            }
        } catch (Exception e) {
            System.out.println("Error al autenticar: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean guardar(Usuario entidad) {
        // Aquí irá el código SQL INSERT
        return false;
    }

    @Override
    public boolean editar(Usuario entidad) {
        // Aquí irá el código SQL UPDATE
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // Aquí irá el código SQL DELETE
        return false;
    }

    @Override
    public List<Usuario> listar() {
        // Aquí irá el código SQL SELECT
        return null;
    }
}