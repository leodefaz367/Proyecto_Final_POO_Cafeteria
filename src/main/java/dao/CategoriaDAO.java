package dao;

import model.Categoria;
import java.util.List;

public class CategoriaDAO implements ICRUD<Categoria>{
    @Override
    public boolean guardar(Categoria entidad) {
        // Aquí irá el código SQL INSERT
        return false;
    }

    @Override
    public boolean editar(Categoria entidad) {
        // Aquí irá el código SQL UPDATE
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // Aquí irá el código SQL DELETE
        return false;
    }

    @Override
    public List<Categoria> listar() {
        // Aquí irá el código SQL SELECT
        return null;
    }
}