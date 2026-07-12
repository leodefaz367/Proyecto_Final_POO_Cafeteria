package dao;

import db.Conexion;
import model.Administrador;
import model.Cajero;
import model.Reportes;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = "INSERT INTO usuarios (nombre, correo, clave, rol) VALUES (?, ?, ?, ?)";
        try (Connection con = db.Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getCorreo());
            ps.setString(3, entidad.getClave());
            ps.setString(4, entidad.getRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean editar(Usuario entidad) {
        String sql = "UPDATE usuarios SET nombre=?, correo=?, clave=?, rol=? WHERE id_usuario=?";
        try (Connection con = db.Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getCorreo());
            ps.setString(3, entidad.getClave());
            ps.setString(4, entidad.getRol());
            ps.setInt(5, entidad.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";
        try (Connection con = db.Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection con = db.Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Usamos la clase hija Administrador genéricamente para listarlos en la tabla
                lista.add(new model.Administrador(rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("correo"), rs.getString("clave"), rs.getString("rol"), rs.getBoolean("estado")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}