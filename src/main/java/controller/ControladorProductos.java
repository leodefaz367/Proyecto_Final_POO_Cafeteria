package controller;

import dao.ProductoDAO;
import model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorProductos {

    @FXML private TextField txtNombre, txtDescripcion, txtPrecio, txtStock, txtCategoria;
    @FXML private TableView<Producto> tvProductos;
    @FXML private TableColumn<Producto, Integer> colId, colStock, colCategoria;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio;

    private ProductoDAO productoDAO = new ProductoDAO();
    private Producto productoSeleccionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        cargarTabla();
    }

    private void cargarTabla() {
        ObservableList<Producto> lista = FXCollections.observableArrayList(productoDAO.listar());
        tvProductos.setItems(lista);
    }

    @FXML
    public void guardarProducto() {
        if (!validarCampos()) return;

        Producto p = new Producto(0, txtNombre.getText(), txtDescripcion.getText(),
                Double.parseDouble(txtPrecio.getText()), Integer.parseInt(txtStock.getText()),
                "", Integer.parseInt(txtCategoria.getText()));

        if (productoDAO.guardar(p)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto guardado correctamente.");
            limpiarCampos();
            cargarTabla();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el producto.");
        }
    }

    @FXML
    public void editarProducto() {
        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un producto de la tabla.");
            return;
        }
        if (!validarCampos()) return;

        productoSeleccionado.setNombre(txtNombre.getText());
        productoSeleccionado.setDescripcion(txtDescripcion.getText());
        productoSeleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));
        productoSeleccionado.setStock(Integer.parseInt(txtStock.getText()));
        productoSeleccionado.setIdCategoria(Integer.parseInt(txtCategoria.getText()));

        if (productoDAO.editar(productoSeleccionado)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto editado correctamente.");
            limpiarCampos();
            cargarTabla();
        }
    }

    @FXML
    public void eliminarProducto() {
        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un producto de la tabla.");
            return;
        }

        if (productoDAO.eliminar(productoSeleccionado.getIdProducto())) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado.");
            limpiarCampos();
            cargarTabla();
        }
    }

    @FXML
    public void seleccionarProducto() {
        productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            txtNombre.setText(productoSeleccionado.getNombre());
            txtDescripcion.setText(productoSeleccionado.getDescripcion());
            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
            txtStock.setText(String.valueOf(productoSeleccionado.getStock()));
            txtCategoria.setText(String.valueOf(productoSeleccionado.getIdCategoria()));
        }
    }

    @FXML
    public void limpiarCampos() {
        txtNombre.clear(); txtDescripcion.clear(); txtPrecio.clear();
        txtStock.clear(); txtCategoria.clear();
        productoSeleccionado = null;
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty() || txtCategoria.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos obligatorios deben estar llenos.");
            return false;
        }
        try {
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());
            if (precio <= 0 || stock < 0) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "El precio debe ser mayor a 0 y el stock no puede ser negativo.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Precio y Stock deben ser valores numéricos válidos.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}