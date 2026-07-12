package controller;

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

    public void inicializarDashboard(Usuario usuarioLogueado) {
        lblBienvenida.setText("Bienvenido, " + usuarioLogueado.getNombre() + " (" + usuarioLogueado.getRol() + ")");

        // Restricción de roles
        if (usuarioLogueado.getRol().equals("Cajero") || usuarioLogueado.getRol().equals("Reportes")) {
            btnUsuarios.setVisible(false); // Solo el admin gestiona usuarios
            btnConfiguracion.setVisible(false);
        }
    }
}