package controller;

import db.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import model.Administrador;
import model.Cajero;
import model.Reportes;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.Alertas;

public class ControladorUsuarios {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtClave;
    @FXML private ComboBox<String> cmbRol;

    @FXML private TableView<Usuario> tvUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colRol;

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private Usuario usuarioSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        cmbRol.setItems(FXCollections.observableArrayList("Administrador", "Cajero", "Reportes"));

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        try (Connection con = Conexion.conectar()) {
            String sql = "SELECT * FROM usuarios ORDER BY id_usuario ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                String clave = rs.getString("clave");
                String rolBD = rs.getString("rol");

                Usuario u = null;
                if (rolBD.equals("Administrador")) {
                    u = new Administrador(id, nombre, correo, clave);
                } else if (rolBD.equals("Cajero")) {
                    u = new Cajero(id, nombre, correo, clave);
                } else if (rolBD.equals("Reportes")) {
                    u = new Reportes(id, nombre, correo, clave);
                } else {
                    // Rol no reconocido: se mantiene un usuario genérico como respaldo defensivo
                    u = new Usuario(id, nombre, correo, clave, rolBD) {
                        @Override
                        public String obtenerPermisos() { return "Acceso a " + rolBD; }
                    };
                }
                listaUsuarios.add(u);
            }
            tvUsuarios.setItems(listaUsuarios);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    void guardar(ActionEvent event) {
        if (!validarCampos()) return;
        try (Connection con = Conexion.conectar()) {
            if (usuarioSeleccionado == null) {
                String sql = "INSERT INTO usuarios (nombre, correo, clave, rol) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, txtNombre.getText());
                ps.setString(2, txtCorreo.getText());
                ps.setString(3, txtClave.getText());
                ps.setString(4, cmbRol.getValue());
                ps.executeUpdate();
                Alertas.mostrar("Éxito", "Usuario registrado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                String sql = "UPDATE usuarios SET nombre=?, correo=?, clave=?, rol=? WHERE id_usuario=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, txtNombre.getText());
                ps.setString(2, txtCorreo.getText());
                ps.setString(3, txtClave.getText());
                ps.setString(4, cmbRol.getValue());
                ps.setInt(5, usuarioSeleccionado.getIdUsuario());
                ps.executeUpdate();
                Alertas.mostrar("Éxito", "Usuario modificado correctamente.", Alert.AlertType.INFORMATION);
            }
            limpiar(null);
            cargarUsuarios();
        } catch (Exception e) {
            Alertas.mostrar("Error", "Error al guardar (Verifique que el correo no esté repetido).", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void eliminar(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            Alertas.mostrar("Atención", "Seleccione un usuario para eliminarlo.", Alert.AlertType.WARNING);
            return;
        }
        try (Connection con = Conexion.conectar()) {
            String sql = "DELETE FROM usuarios WHERE id_usuario=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioSeleccionado.getIdUsuario());
            ps.executeUpdate();
            Alertas.mostrar("Éxito", "Usuario eliminado.", Alert.AlertType.INFORMATION);
            limpiar(null);
            cargarUsuarios();
        } catch (Exception e) {
            Alertas.mostrar("Error", "No se puede eliminar porque tiene ventas registradas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void seleccionar(MouseEvent event) {
        usuarioSeleccionado = tvUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            txtNombre.setText(usuarioSeleccionado.getNombre());
            txtCorreo.setText(usuarioSeleccionado.getCorreo());
            txtClave.setText(usuarioSeleccionado.getClave());
            cmbRol.setValue(usuarioSeleccionado.getRol());
        }
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtNombre.clear();
        txtCorreo.clear();
        txtClave.clear();
        cmbRol.setValue(null);
        usuarioSeleccionado = null;
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtCorreo.getText().isEmpty() || txtClave.getText().isEmpty() || cmbRol.getValue() == null) {
            Alertas.mostrar("Error", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}