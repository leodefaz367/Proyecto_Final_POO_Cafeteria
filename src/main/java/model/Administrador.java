package model;

public class Administrador extends Usuario {
    public Administrador(int idUsuario, String nombre, String correo, String clave) {
        super(idUsuario, nombre, correo, clave, "Administrador");
    }

    @Override
    public String obtenerPermisos() {
        return "Acceso total al sistema: CRUD, Ventas y Reportes.";
    }
}