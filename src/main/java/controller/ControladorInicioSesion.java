package controller;

import dao.UsuarioDAO;
import model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Alertas;

public class ControladorInicioSesion {

    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtClave;

    @FXML
    public void onIniciarSesion() {
        String correo = txtCorreo.getText();
        String clave = txtClave.getText();

        if (correo.isEmpty() || clave.isEmpty()) {
            Alertas.mostrar("Campos incompletos", "Por favor, ingrese su correo y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuarioLogueado = dao.iniciarSesion(correo, clave);

        if (usuarioLogueado != null) {
            abrirDashboard(usuarioLogueado);
        } else {
            Alertas.mostrar("Error de Autenticación", "Correo o contraseña incorrectos, o usuario inactivo.", Alert.AlertType.ERROR);
        }
    }

    private void abrirDashboard(Usuario usuarioLogueado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/panel_principal.fxml"));
            Parent root = loader.load();

            ControladorPanelPrincipal controlador = loader.getController();
            controlador.inicializarDatos(usuarioLogueado);

            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.setTitle("Panel Principal - Sistema de Cafetería");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            Alertas.mostrar("Error", "No se pudo cargar el panel principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}