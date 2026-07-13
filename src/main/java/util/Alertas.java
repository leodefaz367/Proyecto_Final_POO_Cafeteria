package util;

import javafx.scene.control.Alert;

public class Alertas {

    private Alertas() {
    }

    public static void mostrar(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
