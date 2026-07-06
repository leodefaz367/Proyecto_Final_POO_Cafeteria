package dao;

import model.Venta;
import java.util.List;

public class VentaDAO implements ICRUD<Venta>{
    @Override
    public boolean guardar(Venta entidad) {
        // Aquí irá el código SQL INSERT
        return false;
    }

    @Override
    public boolean editar(Venta entidad) {
        // Aquí irá el código SQL UPDATE
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // Aquí irá el código SQL DELETE
        return false;
    }

    @Override
    public List<Venta> listar() {
        // Aquí irá el código SQL SELECT
        return null;
    }
}