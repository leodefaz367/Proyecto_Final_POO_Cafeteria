package model;

public class Reportes extends Usuario {
    public Reportes(int idUsuario, String nombre, String correo, String clave) {
        super(idUsuario, nombre, correo, clave, "Reportes");
    }

    @Override
    public String obtenerPermisos() {
        return "Acceso limitado: Solo módulo de Reportes.";
    }
}
