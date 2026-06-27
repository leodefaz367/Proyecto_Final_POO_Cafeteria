package model;

public abstract class Usuario extends Persona {
    private String rol;
    private String estado;
    public abstract void obtenerPermisos();
}
