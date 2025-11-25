package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.example.tiendav2.Main;

public class LoginController {
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private void initialize() {
        // Configuración inicial si es necesaria
        messageLabel.setText("");
    }
    
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        // Validación simple (puedes mejorarla según tus necesidades)
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Por favor, complete todos los campos");
            return;
        }
        
        // Aquí iría la lógica de autenticación
        if ("admin@example.com".equals(email) && "admin".equals(password)) {
            // Login exitoso
            Main.showMainView();
        } else {
            // Credenciales incorrectas
            messageLabel.setText("Correo o contraseña incorrectos");
        }
    }
}
