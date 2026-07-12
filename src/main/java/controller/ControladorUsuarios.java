package controller;

import dao.UsuarioDAO;
import model.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorUsuarios {
    @FXML
    private TextField txtNombre, txtCorreo, txtClave, txtRol;
    @FXML
    private TableView<Usuario> tvUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> colId;
    @FXML
    private TableColumn<Usuario, String> colNombre, colCorreo, colRol;

    private UsuarioDAO dao = new UsuarioDAO();
    private Usuario seleccionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        cargarTabla();
    }

    private void cargarTabla() {
        tvUsuarios.setItems(FXCollections.observableArrayList(dao.listar()));
    }

    @FXML
    public void guardar() {
        Usuario u = new model.Administrador(0, txtNombre.getText(), txtCorreo.getText(), txtClave.getText(), txtRol.getText(), true);
        if(dao.guardar(u)) { limpiar(); cargarTabla(); }
    }

    @FXML
    public void seleccionar() {
        seleccionado = tvUsuarios.getSelectionModel().getSelectedItem();
        if(seleccionado != null) {
            txtNombre.setText(seleccionado.getNombre());
            txtCorreo.setText(seleccionado.getCorreo());
            txtClave.setText(seleccionado.getClave());
            txtRol.setText(seleccionado.getRol());
        }
    }

    @FXML
    public void eliminar() {
        if(seleccionado != null && dao.eliminar(seleccionado.getIdUsuario())) {
            limpiar(); cargarTabla();
        }
    }

    @FXML
    public void limpiar() {
        txtNombre.clear();
        txtCorreo.clear();
        txtClave.clear();
        txtRol.clear();
        seleccionado = null; }
}