package model;

public class Administrador extends Usuario {
    public Administrador(int idUsuario, String nombre, String correo, String clave, String rol, boolean estado) {
        super(idUsuario, nombre, correo, clave, rol, estado);
    }

    @Override
    public String obtenerPermisos() {
        return "Acceso total: Usuarios, Productos, Ventas y Reportes";
    }
}