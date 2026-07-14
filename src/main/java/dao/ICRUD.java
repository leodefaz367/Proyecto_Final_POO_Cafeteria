package dao;

import java.util.List;

public interface ICRUD<T> {
    boolean guardar(T entidad);
    boolean editar(T entidad);
    boolean eliminar(int id);
    List<T> listar();
}
