package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import org.example.tiendav2.service.AuthService;
import org.example.tiendav2.service.LoginService;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label messageLabel;

    @FXML
    private Button btnLogin;

    private final LoginService loginService = LoginService.getInstancia();

    @FXML
    private void handleLogin() {
        try {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();

            System.out.println("Intento de login con: " + email);
            
            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Completa todos los campos");
                return;
            }

            boolean ok = loginService.autenticarUsuario(email, password);
            System.out.println("Resultado autenticación: " + ok);

            if (!ok) {
                messageLabel.setStyle("-fx-text-fill: #ff5252;");
                messageLabel.setText("Credenciales incorrectas");
                return;
            }

            messageLabel.setStyle("-fx-text-fill: #00ff8a;");
            messageLabel.setText("Login correcto");
            
            // Pequeña pausa para mostrar el mensaje
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                try {
                    String fxmlPath;
                    
                    // Verificar si el usuario actual es admin
                    boolean esAdmin = AuthService.esAdmin();
                    System.out.println("Es admin: " + esAdmin);
                    
                    if (esAdmin) {
                        fxmlPath = "/org/example/tiendav2/fxml/admin/dashboard.fxml";
                        System.out.println("Redirigiendo a panel de administración");
                    } else {
                        fxmlPath = "/org/example/tiendav2/fxml/catalogo.fxml";
                        System.out.println("Redirigiendo a catálogo");
                    }
                    
                    // Usar getClass().getResource() con la ruta que comienza con /
                    URL fxmlUrl = getClass().getResource(fxmlPath);
                    if (fxmlUrl == null) {
                        throw new RuntimeException("No se pudo encontrar el archivo: " + fxmlPath);
                    }
                    System.out.println("Cargando FXML desde: " + fxmlUrl);
                    FXMLLoader loader = new FXMLLoader(fxmlUrl);
                    Parent root = loader.load();

                    Stage stage = (Stage) txtEmail.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    messageLabel.setStyle("-fx-text-fill: #ff5252;");
                    messageLabel.setText("Error: " + e.getMessage());
                }
            });
            pause.play();
            
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: #ff5252;");
            messageLabel.setText("Error inesperado: " + e.getMessage());
        }
    }
}
