package model;

public class Reportes extends Usuario{
    public Reportes(int idUsuario, String nombre, String correo, String clave, String rol, boolean estado) {
        super(idUsuario, nombre, correo, clave, rol, estado);
    }

    @Override
    public String obtenerPermisos() {
        return "Acceso total: Usuarios, Productos, Ventas y Reportes";
    }
}
