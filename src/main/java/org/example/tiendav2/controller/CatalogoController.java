package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.util.List;

public class CatalogoController {

    @FXML
    private GridPane grid;

    private static class Producto {
        String nombre;
        String precio;

        Producto(String n, String p) {
            nombre = n;
            precio = p;
        }
    }

    @FXML
    private void initialize() {
        List<Producto> productos = List.of(
                new Producto("Camisa Oversize Nova", "$149.900"),
                new Producto("Sudadera Essential", "$199.900"),
                new Producto("Pantalón SlimFit", "$179.900"),
                new Producto("Hoodie Nova Essential Pro", "$239.900"),
                new Producto("Camiseta Minimal Core", "$129.900"),
                new Producto("Crop Top Nova SilkFit", "$109.900"),
                new Producto("Cargo StreetFlex 2.0", "$189.900"),
                new Producto("Chaqueta Nova LayerWind", "$299.900"),
                new Producto("Jogger SoftFlex Premium", "$159.900")
        );

        int col = 0;
        int row = 0;

        for (Producto p : productos) {
            VBox card = crearTarjetaProducto(p.nombre, p.precio);

            grid.add(card, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    private VBox crearTarjetaProducto(String nombre, String precio) {
        Rectangle img = new Rectangle(280, 360);
        img.setArcWidth(16);
        img.setArcHeight(16);
        img.setStyle("-fx-fill: linear-gradient(to bottom, #222, #111);");

        Label name = new Label(nombre);
        name.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Label price = new Label(precio);
        price.setStyle("-fx-text-fill: #00ff8a; -fx-font-size: 15px;");

        VBox box = new VBox(12, img, name, price);
        box.setPadding(new Insets(6));
        box.setStyle("-fx-background-color: transparent;");
        box.setOnMouseEntered(e -> animarHover(box, true));
        box.setOnMouseExited(e -> animarHover(box, false));

        return box;
    }

    private void animarHover(VBox box, boolean hover) {
        double scaleTo = hover ? 1.05 : 1.0;
        double translateTo = hover ? -8 : 0;

        ScaleTransition st = new ScaleTransition(Duration.millis(160), box);
        st.setToX(scaleTo);
        st.setToY(scaleTo);

        TranslateTransition tt = new TranslateTransition(Duration.millis(160), box);
        tt.setToY(translateTo);

        st.play();
        tt.play();
    }
}
