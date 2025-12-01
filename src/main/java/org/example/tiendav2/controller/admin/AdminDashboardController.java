package org.example.tiendav2.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.tiendav2.service.AuthService;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Label lblNombreUsuario;
    
    @FXML
    private Label lblRol;
    
    @FXML
    private StackPane contentArea;
    
    @FXML
    public void initialize() {
        // Mostrar información del usuario actual
        if (AuthService.getUsuarioActual() != null) {
            lblNombreUsuario.setText(AuthService.getUsuarioActual().getNombre());
            lblRol.setText(AuthService.getUsuarioActual().getRol().toString());
        }
    }
    
    @FXML
    private void handleGestionUsuarios() {
        // Cargar la vista de gestión de usuarios
        loadView("Gestionar Usuarios", "/org/example/tiendav2/fxml/admin/usuarios.fxml");
    }
    
    @FXML
    private void handleGestionProductos() {
        // Cargar la vista de gestión de productos
        loadView("Gestionar Productos", "/org/example/tiendav2/fxml/admin/productos.fxml");
    }
    
    @FXML
    private void handleReportes() {
        // Cargar la vista de reportes
        loadView("Reportes", "/org/example/tiendav2/fxml/admin/reportes.fxml");
    }
    
    @FXML
    private void handleCerrarSesion() {
        try {
            // Cerrar sesión
            AuthService.cerrarSesion();
            
            // Cargar la pantalla de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tiendav2/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) lblNombreUsuario.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadView(String title, String fxmlPath) {
        try {
            // Limpiar el área de contenido
            contentArea.getChildren().clear();
            
            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            // Agregar la vista al área de contenido
            contentArea.getChildren().add(view);
            
            // Actualizar el título de la ventana
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setTitle("Panel de Administración - " + title);
            
        } catch (IOException e) {
            e.printStackTrace();
            // Mostrar mensaje de error
            System.err.println("Error al cargar la vista: " + fxmlPath);
        }
    }
}
