package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.tiendav2.model.HistorialCompra;
import org.example.tiendav2.model.Producto;
import org.example.tiendav2.service.HistorialService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class HistorialComprasController {

    @FXML
    private VBox contenedorCompras;
    @FXML
    private Label mensajeVacio;
    @FXML
    private Label lblTotalCompras;
    @FXML
    private Label lblTotalGastado;
    @FXML
    private Label lblTotalProductos;

    private final HistorialService historialService = HistorialService.getInstancia();
    private static final int USUARIO_ID = 1; // ID del usuario actual

    @FXML
    public void initialize() {
        cargarHistorialCompras();
    }
    
    @FXML
    private void actualizarHistorial() {
        cargarHistorialCompras();
    }

    private void cargarHistorialCompras() {
        contenedorCompras.getChildren().clear();
        List<HistorialCompra> historial = historialService.obtenerHistorialUsuario(USUARIO_ID);

        if (historial.isEmpty()) {
            mensajeVacio.setVisible(true);
            actualizarEstadisticas(historial);
            return;
        }

        mensajeVacio.setVisible(false);
        for (HistorialCompra compra : historial) {
            contenedorCompras.getChildren().add(crearTarjetaCompra(compra));
        }
        
        actualizarEstadisticas(historial);
    }
    
    private void actualizarEstadisticas(List<HistorialCompra> historial) {
        int totalCompras = historial.size();
        double totalGastado = historial.stream()
                .mapToDouble(HistorialCompra::getTotal)
                .sum();
        int totalProductos = historial.stream()
                .mapToInt(c -> c.getProductos().size())
                .sum();
        
        if (lblTotalCompras != null) {
            lblTotalCompras.setText(String.valueOf(totalCompras));
        }
        if (lblTotalGastado != null) {
            lblTotalGastado.setText(String.format("$%,.2f", totalGastado));
        }
        if (lblTotalProductos != null) {
            lblTotalProductos.setText(String.valueOf(totalProductos));
        }
    }

    private VBox crearTarjetaCompra(HistorialCompra compra) {
        VBox tarjeta = new VBox(12);
        tarjeta.getStyleClass().add("tarjeta-compra");
        tarjeta.setStyle(
                "-fx-background-color: #1a1a1a;" +
                "-fx-padding: 20;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #00ff8a;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 3);"
        );
        tarjeta.setMaxWidth(800);

        // Encabezado con ID, fecha y estado
        HBox encabezado = new HBox(15);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        
        Label idLabel = new Label("Pedido #" + compra.getId());
        idLabel.setStyle("-fx-text-fill: #00ff8a; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        Label fechaLabel = new Label("📅 " + compra.getFechaCompra().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        ));
        fechaLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 14px;");
        
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        
        String estadoTexto = compra.getEstado().toUpperCase();
        String colorEstado = compra.getEstado().equalsIgnoreCase("entregado") ? "#4CAF50" : 
                            compra.getEstado().equalsIgnoreCase("en proceso") ? "#FFC107" : "#2196F3";
        
        Label estadoLabel = new Label(estadoTexto);
        estadoLabel.setStyle(
            "-fx-background-color: " + colorEstado + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 5 15;" +
            "-fx-background-radius: 15;"
        );
        
        encabezado.getChildren().addAll(idLabel, fechaLabel, espacio, estadoLabel);

        // Línea divisoria
        Separator divisor = new Separator();
        divisor.setStyle("-fx-background-color: #333;");

        // Lista de productos
        VBox listaProductos = new VBox(8);
        for (Producto producto : compra.getProductos()) {
            HBox filaProducto = new HBox(10);
            filaProducto.setAlignment(Pos.CENTER_LEFT);
            
            Label nombreProducto = new Label(producto.getNombre());
            nombreProducto.setStyle("-fx-text-fill: #eee; -fx-font-size: 14px;");
            
            Region espacioPrecio = new Region();
            HBox.setHgrow(espacioPrecio, Priority.ALWAYS);
            
            Label precioProducto = new Label(String.format("$%.2f", producto.getPrecio()));
            precioProducto.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            
            filaProducto.getChildren().addAll(nombreProducto, espacioPrecio, precioProducto);
            listaProductos.getChildren().add(filaProducto);
        }

        // Dirección de envío
        HBox direccionBox = new HBox(10);
        direccionBox.setAlignment(Pos.CENTER_LEFT);
        direccionBox.setStyle("-fx-padding: 10 0 0 0;");
        
        Label iconoDireccion = new Label("📍");
        iconoDireccion.setStyle("-fx-font-size: 14px;");
        
        Label direccionLabel = new Label("Dirección: " + compra.getDireccionEnvio());
        direccionLabel.setStyle("-fx-text-fill: #ccc; -fx-font-size: 13px;");
        
        direccionBox.getChildren().addAll(iconoDireccion, direccionLabel);
        
        // Total
        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setStyle("-fx-padding: 10 0 0 0;");
        
        Label totalLabel = new Label("Total: " + String.format("$%,.2f", compra.getTotal()));
        totalLabel.setStyle("-fx-text-fill: #00ff8a; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        totalBox.getChildren().add(totalLabel);

        // Agregar todo a la tarjeta
        tarjeta.getChildren().addAll(encabezado, divisor, listaProductos, direccionBox, totalBox);
        
        return tarjeta;
    }

    @FXML
    private void cerrar() {
        Stage stage = (Stage) contenedorCompras.getScene().getWindow();
        stage.close();
    }
}
