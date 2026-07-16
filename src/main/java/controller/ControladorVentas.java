package controller;

import dao.VentaDAO;
import db.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DetalleVenta;
import model.Producto;
import model.Usuario;
import model.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import util.Alertas;

public class ControladorVentas {

    @FXML private ComboBox<Producto> cmbProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtCedula;
    @FXML private TextField txtNombreCliente;
    @FXML private TableView<DetalleVenta> tvCarrito;
    @FXML private TableColumn<DetalleVenta, String> colProducto;
    @FXML private TableColumn<DetalleVenta, Integer> colCantidad;
    @FXML private TableColumn<DetalleVenta, Double> colPrecio;
    @FXML private TableColumn<DetalleVenta, Double> colSubtotal;
    @FXML private Label lblTotal;

    private ObservableList<DetalleVenta> listaCarrito = FXCollections.observableArrayList();
    private double totalVenta = 0.0;
    private VentaDAO ventaDAO = new VentaDAO();
    private Usuario cajeroLogueado;

    public void setCajero(Usuario cajero) { this.cajeroLogueado = cajero; }

    @FXML
    public void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tvCarrito.setItems(listaCarrito);
        cargarProductosDisponibles();
    }

    private void cargarProductosDisponibles() {
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        try (Connection con = Conexion.conectar()) {
            ResultSet rs = con.prepareStatement("SELECT * FROM productos WHERE stock > 0 ORDER BY nombre ASC").executeQuery();
            while(rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id_producto"), rs.getString("nombre"), "",
                        rs.getDouble("precio"), rs.getInt("stock"), "", rs.getInt("id_categoria"), ""
                ));
            }
            cmbProducto.setItems(productos);
        } catch(Exception e) { e.printStackTrace(); }
    }

    @FXML
    void agregarAlCarrito(ActionEvent event) {
        Producto prodSeleccionado = cmbProducto.getValue();
        if (prodSeleccionado == null || txtCantidad.getText().isEmpty()) {
            Alertas.mostrar("Error", "Seleccione un producto e ingrese la cantidad.", Alert.AlertType.ERROR);
            return;
        }
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            try (Connection con = Conexion.conectar()) {
                PreparedStatement ps = con.prepareStatement("SELECT stock FROM productos WHERE id_producto = ?");
                ps.setInt(1, prodSeleccionado.getIdProducto());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int stockReal = rs.getInt("stock");
                    if (cantidad > stockReal) {
                        Alertas.mostrar("Stock insuficiente", "Solo quedan " + stockReal + " unidades.", Alert.AlertType.WARNING);
                        return;
                    }
                    double subtotal = prodSeleccionado.getPrecio() * cantidad;
                    listaCarrito.add(new DetalleVenta(prodSeleccionado.getIdProducto(), prodSeleccionado.getNombre(), cantidad, prodSeleccionado.getPrecio(), subtotal));
                    actualizarTotal();
                    cmbProducto.setValue(null);
                    txtCantidad.clear();
                }
            }
        } catch (NumberFormatException e) {
            Alertas.mostrar("Error de Formato", "La cantidad debe ser un número entero válido.", Alert.AlertType.ERROR);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void eliminarDelCarrito(ActionEvent event) {
        DetalleVenta seleccionado = tvCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaCarrito.remove(seleccionado);
            actualizarTotal();
        } else {
            Alertas.mostrar("Atención", "Debe seleccionar un producto para eliminarlo.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void editarCantidad(ActionEvent event) {
        DetalleVenta seleccionado = tvCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        TextInputDialog dialog = new TextInputDialog(String.valueOf(seleccionado.getCantidad()));
        dialog.setTitle("Modificar Cantidad");
        dialog.setHeaderText("Modificando: " + seleccionado.getNombreProducto());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nuevaCantidadStr -> {
            try {
                int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);
                try (Connection con = Conexion.conectar()) {
                    PreparedStatement ps = con.prepareStatement("SELECT stock FROM productos WHERE id_producto = ?");
                    ps.setInt(1, seleccionado.getIdProducto());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && nuevaCantidad > rs.getInt("stock")) {
                        Alertas.mostrar("Stock insuficiente", "Solo quedan " + rs.getInt("stock") + " unidades.", Alert.AlertType.WARNING);
                        return;
                    }
                }
                if (nuevaCantidad > 0) {
                    seleccionado.setCantidad(nuevaCantidad);
                    seleccionado.setSubtotal(seleccionado.getPrecio() * nuevaCantidad);
                    tvCarrito.refresh();
                    actualizarTotal();
                }
            } catch (Exception e) {
                Alertas.mostrar("Error", "Ingrese un número válido.", Alert.AlertType.ERROR);
            }
        });
    }

    private void actualizarTotal() {
        totalVenta = 0.0;
        for (DetalleVenta dv : listaCarrito) {
            totalVenta += dv.getSubtotal();
        }
        lblTotal.setText(String.format("%.2f", totalVenta));
    }

    @FXML
    void procesarVenta(ActionEvent event) {
        if (listaCarrito.isEmpty()) {
            Alertas.mostrar("Carrito vacío", "Agregue productos antes de vender.", Alert.AlertType.WARNING);
            return;
        }

        int idCajeroReal = (cajeroLogueado != null) ? cajeroLogueado.getIdUsuario() : 2;
        String cedula = txtCedula.getText().isEmpty() ? "9999999999" : txtCedula.getText();
        String nombre = txtNombreCliente.getText().isEmpty() ? "Consumidor Final" : txtNombreCliente.getText();

        Venta nuevaVenta = new Venta(0, null, totalVenta, 1, idCajeroReal);
        nuevaVenta.setNombreCliente(nombre);
        nuevaVenta.setCedulaCliente(cedula);

        if (ventaDAO.registrarVenta(nuevaVenta, listaCarrito)) {
            generarComprobante(cedula, nombre);
            listaCarrito.clear();
            actualizarTotal();
            txtCedula.clear();
            txtNombreCliente.clear();
            cargarProductosDisponibles();
        } else {
            Alertas.mostrar("Error", "No se pudo registrar la venta.", Alert.AlertType.ERROR);
        }
    }

    private void generarComprobante(String cedula, String nombreCliente) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("===================================\n");
        ticket.append("         CAFETERÍA SYSTEM          \n");
        ticket.append("===================================\n");
        ticket.append("Cajero: ").append(cajeroLogueado != null ? cajeroLogueado.getNombre() : "Cajero").append("\n");
        ticket.append("Cliente: ").append(nombreCliente).append("\n");
        ticket.append("CI/RUC: ").append(cedula).append("\n");
        ticket.append("-----------------------------------\n");
        ticket.append(String.format("%-20s %-5s %s\n", "Producto", "Cant", "Subtotal"));
        ticket.append("-----------------------------------\n");
        for (DetalleVenta dv : listaCarrito) {
            String prod = dv.getNombreProducto().length() > 18 ? dv.getNombreProducto().substring(0, 18) : dv.getNombreProducto();
            ticket.append(String.format("%-20s %-5d $%.2f\n", prod, dv.getCantidad(), dv.getSubtotal()));
        }
        ticket.append("-----------------------------------\n");
        ticket.append(String.format("TOTAL PAGADO:           $%.2f\n", totalVenta));
        ticket.append("===================================\n");
        ticket.append("      ¡Gracias por su compra!      \n");

        Alertas.mostrar("Factura Generada", ticket.toString(), Alert.AlertType.INFORMATION);
    }

}