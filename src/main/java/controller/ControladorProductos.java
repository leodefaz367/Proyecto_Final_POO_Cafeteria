package controller;

import dao.ProductoDAO;
import db.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Categoria;
import model.Producto;

import java.sql.Connection;
import java.sql.ResultSet;
import util.Alertas;

public class ControladorProductos {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private ComboBox<Categoria> cmbCategoria;

    @FXML private TableView<Producto> tvProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colCategoria;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private Producto productoSeleccionado = null;
    private final ProductoDAO productoDAO = new ProductoDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria")); // Nombre en vez de ID

        cargarCategorias();
        cargarProductos();
    }

    private void cargarCategorias() {
        ObservableList<Categoria> categorias = FXCollections.observableArrayList();
        try (Connection con = Conexion.conectar()) {
            ResultSet rs = con.prepareStatement("SELECT * FROM categorias").executeQuery();
            while(rs.next()) {
                categorias.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nombre")));
            }
            cmbCategoria.setItems(categorias);
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void cargarProductos() {
        listaProductos.setAll(productoDAO.listar());
        tvProductos.setItems(listaProductos);
    }

    @FXML
    void guardarProducto(ActionEvent event) {
        if (!validarCampos()) return;
        Producto nuevo = new Producto(0, txtNombre.getText(), txtDescripcion.getText(),
                Double.parseDouble(txtPrecio.getText()), Integer.parseInt(txtStock.getText()),
                "", cmbCategoria.getValue().getIdCategoria(), "");
        if (productoDAO.guardar(nuevo)) {
            Alertas.mostrar("Éxito", "Producto guardado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos(null);
            cargarProductos();
        } else {
            Alertas.mostrar("Error", "Error al guardar.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void editarProducto(ActionEvent event) {
        if (productoSeleccionado == null) {
            Alertas.mostrar("Atención", "Seleccione un producto.", Alert.AlertType.WARNING);
            return;
        }
        if (!validarCampos()) return;

        productoSeleccionado.setNombre(txtNombre.getText());
        productoSeleccionado.setDescripcion(txtDescripcion.getText());
        productoSeleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));
        productoSeleccionado.setStock(Integer.parseInt(txtStock.getText()));
        productoSeleccionado.setIdCategoria(cmbCategoria.getValue().getIdCategoria());

        if (productoDAO.editar(productoSeleccionado)) {
            Alertas.mostrar("Éxito", "Producto actualizado.", Alert.AlertType.INFORMATION);
            limpiarCampos(null);
            cargarProductos();
        } else {
            Alertas.mostrar("Error", "Error al actualizar.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void eliminarProducto(ActionEvent event) {
        if (productoSeleccionado == null) {
            Alertas.mostrar("Atención", "Seleccione un producto.", Alert.AlertType.WARNING);
            return;
        }
        if (productoDAO.eliminar(productoSeleccionado.getIdProducto())) {
            Alertas.mostrar("Éxito", "Producto eliminado.", Alert.AlertType.INFORMATION);
            limpiarCampos(null);
            cargarProductos();
        } else {
            Alertas.mostrar("Error", "No se puede eliminar un producto con ventas registradas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void seleccionarProducto(MouseEvent event) {
        productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            txtNombre.setText(productoSeleccionado.getNombre());
            txtDescripcion.setText(productoSeleccionado.getDescripcion());
            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
            txtStock.setText(String.valueOf(productoSeleccionado.getStock()));

            for (Categoria c : cmbCategoria.getItems()) {
                if (c.getIdCategoria() == productoSeleccionado.getIdCategoria()) {
                    cmbCategoria.setValue(c);
                    break;
                }
            }
        }
    }

    @FXML
    void limpiarCampos(ActionEvent event) {
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();
        cmbCategoria.setValue(null);
        productoSeleccionado = null;
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty() || cmbCategoria.getValue() == null) {
            Alertas.mostrar("Error", "Complete todos los campos.", Alert.AlertType.WARNING);
            return false;
        }
        try {
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());
            if (precio <= 0) {
                Alertas.mostrar("Error", "El precio debe ser mayor a cero.", Alert.AlertType.WARNING);
                return false;
            }
            if (stock < 0) {
                Alertas.mostrar("Error", "El stock no puede ser negativo.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            Alertas.mostrar("Error", "Precio y stock deben ser valores numéricos válidos.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}