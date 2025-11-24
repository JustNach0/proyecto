package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.example.tiendav2.Main;

public class CatalogoController {
    @FXML
    private ListView<String> productosList;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private VBox root;
    
    @FXML
    private void initialize() {
        // Inicializar la lista de productos
        productosList.getItems().addAll(
            "Producto 1 - $10.00",
            "Producto 2 - $15.00",
            "Producto 3 - $20.00",
            "Producto 4 - $25.00",
            "Producto 5 - $30.00"
        );
        
        // Configurar el botón de cerrar sesión
        logoutButton.setOnAction(event -> {
            // Volver a la pantalla de login
            Main.showLoginView();
        });
    }
    
    @FXML
    private void handleLogout() {
        // Este método ahora es manejado directamente por el FXML
        Main.showLoginView();
    }
}
