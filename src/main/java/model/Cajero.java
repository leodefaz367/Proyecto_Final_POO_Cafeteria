package model;

public class Cajero extends Usuario {
    public Cajero(int idUsuario, String nombre, String correo, String clave) {
        super(idUsuario, nombre, correo, clave, "Cajero");
    }

    @Override
    public String obtenerPermisos() {
        return "Acceso limitado: Solo módulo de Ventas y visualización de stock.";
    }
}