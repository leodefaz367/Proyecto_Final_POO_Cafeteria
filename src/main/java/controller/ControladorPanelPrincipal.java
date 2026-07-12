package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class ControladorPanelPrincipal {

    @FXML
    private Label lblBienvenida;
    @FXML
    private Button btnUsuarios;
    @FXML
    private Button btnConfiguracion;
    @FXML
    private BorderPane borderPaneMain;

    public void inicializarDashboard(Usuario usuarioLogueado) {
        lblBienvenida.setText("Bienvenido, " + usuarioLogueado.getNombre() + " (" + usuarioLogueado.getRol() + ")");


        if (usuarioLogueado.getRol().equals("Cajero") || usuarioLogueado.getRol().equals("Reportes")) {
            btnUsuarios.setVisible(false); // Solo el admin gestiona usuarios
            btnConfiguracion.setVisible(false);
        }
    }

    @FXML
    public void abrirProductos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/productos.fxml"));
            Parent vistaProductos = loader.load();

            borderPaneMain.setCenter(vistaProductos);
        } catch (Exception e) {
            System.out.println("Error al cargar la pantalla de productos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}