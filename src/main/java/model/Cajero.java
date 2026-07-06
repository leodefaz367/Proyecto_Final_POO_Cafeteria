package model;

public class Cajero extends Usuario{
    public Cajero(int idUsuario, String nombre, String correo, String clave, String rol, boolean estado) {
        super(idUsuario, nombre, correo, clave, rol, estado);
    }

    @Override
    public String obtenerPermisos() {
        return "Acceso: Usuarios, Productos, Ventas";
    }
}