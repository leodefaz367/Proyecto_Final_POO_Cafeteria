package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Usuario;

public class ControladorPanelPrincipal {

    @FXML private BorderPane borderPaneMain;
    @FXML private Label lblBienvenida;
    @FXML private Button btnVentas;
    @FXML private Button btnProductos;
    @FXML private Button btnUsuarios;
    @FXML private Button btnReportes;
    @FXML private Button btnConfiguracion;

    private Usuario usuarioLogueado;

    public void inicializarDatos(Usuario usuario) {
        this.usuarioLogueado = usuario;
        lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + "\nRol: " + usuario.getRol());

        if (usuario.getRol().equals("Cajero")) {
            btnProductos.setVisible(false);
            btnUsuarios.setVisible(false);
            btnConfiguracion.setVisible(false);
        } else if (usuario.getRol().equals("Reportes")) {
            btnVentas.setVisible(false);
            btnProductos.setVisible(false);
            btnUsuarios.setVisible(false);
            btnConfiguracion.setVisible(false);
        }
    }

    @FXML
    void abrirVentas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ventas.fxml"));
            Parent vista = loader.load();
            ControladorVentas controller = loader.getController();
            controller.setCajero(this.usuarioLogueado);
            borderPaneMain.setCenter(vista);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void abrirProductos(ActionEvent event) { cargarVista("/view/productos.fxml"); }
    @FXML void abrirUsuarios(ActionEvent event) { cargarVista("/view/usuarios.fxml"); }
    @FXML void abrirReportes(ActionEvent event) { cargarVista("/view/reportes.fxml"); }
    @FXML void abrirConfiguracion(ActionEvent event) { cargarVista("/view/configuracion.fxml"); }

    @FXML
    void cerrarSesion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/inicio_sesion.fxml"));
            Stage stage = (Stage) borderPaneMain.getScene().getWindow();
            stage.setTitle("Cafetería - Iniciar Sesión");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarVista(String ruta) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(ruta));
            borderPaneMain.setCenter(vista);
        } catch (Exception e) { e.printStackTrace(); }
    }
}