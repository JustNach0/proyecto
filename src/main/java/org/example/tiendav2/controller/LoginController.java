package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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

    private final LoginService loginService = new LoginService();

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Completa todos los campos");
            return;
        }

        boolean ok = loginService.autenticarUsuario(email, password);

        if (!ok) {
            messageLabel.setStyle("-fx-text-fill: #ff5252;");
            messageLabel.setText("Credenciales incorrectas");
            return;
        }

        messageLabel.setStyle("-fx-text-fill: #00ff8a;");
        messageLabel.setText("Login correcto");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tiendav2/fxml/catalogo.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtEmail.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: #ff5252;");
            messageLabel.setText("Error al cargar el catálogo");
        }
    }
}
