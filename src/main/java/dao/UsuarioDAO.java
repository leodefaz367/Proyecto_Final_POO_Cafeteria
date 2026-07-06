package dao;

import model.Usuario;
import java.util.List;

public class UsuarioDAO implements ICRUD<Usuario>{
    @Override
    public boolean guardar(Usuario entidad) {
        // Aquí irá el código SQL INSERT
        return false;
    }

    @Override
    public boolean editar(Usuario entidad) {
        // Aquí irá el código SQL UPDATE
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // Aquí irá el código SQL DELETE
        return false;
    }

    @Override
    public List<Usuario> listar() {
        // Aquí irá el código SQL SELECT
        return null;
    }
}
