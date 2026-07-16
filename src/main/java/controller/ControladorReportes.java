package controller;

import db.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.DetalleVenta;
import model.Producto;
import model.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class ControladorReportes {

    @FXML private TableView<Producto> tvReportes;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private TableView<Venta> tvVentas;
    @FXML private TableColumn<Venta, Integer> colIdVenta;
    @FXML private TableColumn<Venta, Timestamp> colFecha;
    @FXML private TableColumn<Venta, Double> colTotal;
    @FXML private TableColumn<Venta, String> colCajero;

    @FXML private TableView<DetalleVenta> tvDetalles;
    @FXML private TableColumn<DetalleVenta, Integer> colDetIdProd;
    @FXML private TableColumn<DetalleVenta, String> colDetProducto;
    @FXML private TableColumn<DetalleVenta, Integer> colDetCantidad;
    @FXML private TableColumn<DetalleVenta, Double> colDetPrecio;
    @FXML private TableColumn<DetalleVenta, Double> colDetSubtotal;

    @FXML private Label lblClienteInfo;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private ObservableList<Venta> listaVentas = FXCollections.observableArrayList();
    private ObservableList<DetalleVenta> listaDetalles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colIdVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCajero.setCellValueFactory(new PropertyValueFactory<>("nombreCajero"));

        colDetIdProd.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colDetProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colDetCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colDetPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDetSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        cargarReporteInventario();
        cargarHistorialVentas();
    }

    private void cargarReporteInventario() {
        listaProductos.clear();
        try (Connection con = Conexion.conectar()) {
            String sql = "SELECT p.*, c.nombre AS nombre_categoria FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria ORDER BY p.id_producto ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id_producto"), rs.getString("nombre"), "", 0.0,
                        rs.getInt("stock"), "", rs.getInt("id_categoria"), ""
                );
                p.setNombreCategoria(rs.getString("nombre_categoria"));
                listaProductos.add(p);
            }
            tvReportes.setItems(listaProductos);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarHistorialVentas() {
        listaVentas.clear();
        try (Connection con = Conexion.conectar()) {
            String sql = "SELECT v.*, u.nombre AS nombre_cajero FROM ventas v INNER JOIN usuarios u ON v.id_cajero = u.id_usuario ORDER BY v.fecha DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Venta v = new Venta(
                        rs.getInt("id_venta"), rs.getTimestamp("fecha"), rs.getDouble("total"),
                        rs.getInt("id_cliente"), rs.getInt("id_cajero")
                );
                v.setNombreCajero(rs.getString("nombre_cajero"));
                v.setNombreCliente(rs.getString("nombre_cliente"));
                v.setCedulaCliente(rs.getString("cedula_cliente"));
                listaVentas.add(v);
            }
            tvVentas.setItems(listaVentas);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void seleccionarVenta(MouseEvent event) {
        Venta ventaSeleccionada = tvVentas.getSelectionModel().getSelectedItem();
        if (ventaSeleccionada == null) return;

        lblClienteInfo.setText("Cliente: " + ventaSeleccionada.getNombreCliente() + " | CI/RUC: " + ventaSeleccionada.getCedulaCliente());

        listaDetalles.clear();
        String sql = "SELECT dv.id_producto, p.nombre AS producto_nombre, dv.cantidad, dv.precio, dv.subtotal " +
                "FROM detalle_venta dv INNER JOIN productos p ON dv.id_producto = p.id_producto " +
                "WHERE dv.id_venta = ?";

        try (Connection con = Conexion.conectar()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ventaSeleccionada.getIdVenta());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetalleVenta dv = new DetalleVenta(
                        rs.getInt("id_producto"), rs.getString("producto_nombre"),
                        rs.getInt("cantidad"), rs.getDouble("precio"), rs.getDouble("subtotal")
                );
                listaDetalles.add(dv);
            }
            tvDetalles.setItems(listaDetalles);
        } catch (Exception e) { e.printStackTrace(); }
    }
}