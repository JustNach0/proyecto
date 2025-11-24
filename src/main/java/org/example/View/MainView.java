package org.example.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tienda Louren - Java 23");

        Label label = new Label("¡Bienvenido a Tienda Louren con Java 23!");
        Button button = new Button("Haz clic aquí");

        button.setOnAction(e -> label.setText("¡Funciona con Java 23!"));

        VBox root = new VBox(20, label, button);
        root.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);

        String css = """
            -fx-padding: 20;
            -fx-font-size: 16px;
            -fx-background-color: #f5f5f5;
        """;
        root.setStyle(css);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
