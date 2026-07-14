package model;

public class Cliente extends Usuario {
    public Cliente(int idUsuario, String nombre, String correo) {
        super(idUsuario, nombre, correo, null, "Cliente");
    }

    @Override
    public String obtenerPermisos() {
        return "Sin acceso al sistema. Entidad para facturación.";
    }
}