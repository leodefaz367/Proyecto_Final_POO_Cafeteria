package dao;

import model.Producto;
import java.util.List;

public class ProductoDAO implements ICRUD<Producto> {
    @Override
    public boolean guardar(Producto entidad) {
        // Aquí irá el código SQL INSERT
        return false;
    }

    @Override
    public boolean editar(Producto entidad) {
        // Aquí irá el código SQL UPDATE
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // Aquí irá el código SQL DELETE
        return false;
    }

    @Override
    public List<Producto> listar() {
        // Aquí irá el código SQL SELECT
        return null;
    }
}