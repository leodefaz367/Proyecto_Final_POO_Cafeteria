module com.example.proyecto_final_poo_cafeteria {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proyecto_final_poo_cafeteria to javafx.fxml;
    exports com.example.proyecto_final_poo_cafeteria;
}