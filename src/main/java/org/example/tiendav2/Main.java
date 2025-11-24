package org.example.tiendav2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación TiendaV2
 */
public class Main extends Application {
    
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showLoginView();
        stage.setTitle("TiendaV2");
        stage.show();
    }
    
    /**
     * Muestra la vista de login
     */
    public static void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/org/example/tiendav2/fxml/login.fxml"));
            Parent root = loader.load();
            
            // Aplicar estilos
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(Main.class.getResource("/org/example/tiendav2/css/styles.css").toExternalForm());
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("Inicio de Sesión - TiendaV2");
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra la vista principal del catálogo
     */
    public static void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/org/example/tiendav2/fxml/catalogo.fxml"));
            Parent root = loader.load();
            
            // Aplicar estilos
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(Main.class.getResource("/org/example/tiendav2/css/styles.css").toExternalForm());
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("Catálogo - TiendaV2");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Punto de entrada principal de la aplicación
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Obtiene el stage principal
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
