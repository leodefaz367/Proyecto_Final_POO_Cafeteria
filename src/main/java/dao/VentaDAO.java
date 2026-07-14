package dao;

import db.Conexion;
import model.DetalleVenta;
import model.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class VentaDAO {

    public boolean registrarVenta(Venta venta, List<DetalleVenta> carrito) {
        Connection con = Conexion.conectar();
        try {
            con.setAutoCommit(false);

            String sqlVenta = "INSERT INTO ventas (total, id_cliente, id_cajero, nombre_cliente, cedula_cliente) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setDouble(1, venta.getTotal());
            psVenta.setInt(2, venta.getIdCliente());
            psVenta.setInt(3, venta.getIdCajero());
            psVenta.setString(4, venta.getNombreCliente());
            psVenta.setString(5, venta.getCedulaCliente());
            psVenta.executeUpdate();

            ResultSet rs = psVenta.getGeneratedKeys();
            int idVentaGenerado = 0;
            if (rs.next()) {
                idVentaGenerado = rs.getInt(1);
            }

            String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";

            PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
            PreparedStatement psStock = con.prepareStatement(sqlUpdateStock);

            for (DetalleVenta detalle : carrito) {
                psDetalle.setInt(1, idVentaGenerado);
                psDetalle.setInt(2, detalle.getIdProducto());
                psDetalle.setInt(3, detalle.getCantidad());
                psDetalle.setDouble(4, detalle.getPrecio());
                psDetalle.setDouble(5, detalle.getSubtotal());
                psDetalle.addBatch();

                psStock.setInt(1, detalle.getCantidad());
                psStock.setInt(2, detalle.getIdProducto());
                psStock.addBatch();
            }

            psDetalle.executeBatch();
            psStock.executeBatch();

            con.commit();
            return true;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}