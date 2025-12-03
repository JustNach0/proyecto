package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.tiendav2.model.HistorialCompra;
import org.example.tiendav2.service.HistorialService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class HistorialComprasController {

    @FXML
    private VBox contenedorCompras;
    @FXML
    private Label mensajeVacio;

    private final HistorialService historialService = HistorialService.getInstancia();
    private static final int USUARIO_ID = 1; // ID del usuario actual

    @FXML
    public void initialize() {
        cargarHistorialCompras();
    }

    private void cargarHistorialCompras() {
        contenedorCompras.getChildren().clear();
        var historial = historialService.obtenerHistorialUsuario(USUARIO_ID);

        if (historial.isEmpty()) {
            mensajeVacio.setVisible(true);
            return;
        }

        mensajeVacio.setVisible(false);
        for (HistorialCompra compra : historial) {
            contenedorCompras.getChildren().add(crearTarjetaCompra(compra));
        }
    }

    private javafx.scene.layout.VBox crearTarjetaCompra(HistorialCompra compra) {
        // Crear la tarjeta de la compra
        // Mostrar fecha, total, estado y productos
        return new javafx.scene.layout.VBox();
    }

    @FXML
    private void cerrar() {
        Stage stage = (Stage) contenedorCompras.getScene().getWindow();
        stage.close();
    }
}
