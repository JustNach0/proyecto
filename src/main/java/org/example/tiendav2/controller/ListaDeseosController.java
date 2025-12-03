package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.example.tiendav2.model.Producto;
import org.example.tiendav2.model.Carrito;
import org.example.tiendav2.service.ListaDeseosService;
import java.io.IOException;
import java.util.function.Consumer;

public class ListaDeseosController {

    @FXML
    private VBox contenedorProductos;
    @FXML
    private Label mensajeVacio;

    private final ListaDeseosService listaDeseosService = ListaDeseosService.getInstancia();
    private static final int USUARIO_ID = 1; // ID del usuario actual
    private Consumer<Producto> onAgregarAlCarritoCallback;
    
    // Método para establecer el callback de agregar al carrito
    public void setOnAgregarAlCarrito(Consumer<Producto> callback) {
        this.onAgregarAlCarritoCallback = callback;
    }

    @FXML
    public void initialize() {
        cargarListaDeseos();
    }

    private void cargarListaDeseos() {
        contenedorProductos.getChildren().clear();
        var listaDeseos = listaDeseosService.obtenerListaDeseos(USUARIO_ID);

        if (listaDeseos.getProductos().isEmpty()) {
            mensajeVacio.setVisible(true);
            return;
        }

        mensajeVacio.setVisible(false);
        for (Producto producto : listaDeseos.getProductos()) {
            contenedorProductos.getChildren().add(crearTarjetaProducto(producto));
        }
    }

    private String obtenerRutaImagen(Producto producto) throws Exception {
        // Formatear el nombre del archivo de imagen según el nombre del producto
        String nombreProducto = producto.getNombre();
        System.out.println("Buscando imagen para producto: " + nombreProducto);
        
        String nombreArchivo = nombreProducto.toLowerCase()
            .replace(" ", "")  // Eliminar espacios
            .replace("&", "and") // Reemplazar & con 'and'
            .replace("'", "")    // Eliminar comillas simples
            .replace("®", "")     // Eliminar símbolos de marca registrada
            .replace("™", "")     // Eliminar símbolos de marca
            .replace("+", "plus") // Reemplazar + con 'plus'
            .replace("é", "e")    // Reemplazar caracteres especiales
            .replace("á", "a")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u");
            
        // Primero intentamos con .png
        String nombreArchivoPNG = nombreArchivo + ".png";
        String imagePathPNG = "/org/example/tiendav2/images/productos/" + nombreArchivoPNG;
        
        System.out.println("Buscando imagen en: " + imagePathPNG);
        
        // Verificar si existe el archivo .png
        if (getClass().getResource(imagePathPNG) != null) {
            return imagePathPNG;
        }
        
        // Si no existe, intentamos con .jpg
        String nombreArchivoJPG = nombreArchivo + ".jpg";
        String imagePathJPG = "/org/example/tiendav2/images/productos/" + nombreArchivoJPG;
        
        System.out.println("Imagen no encontrada, buscando: " + imagePathJPG);
        
        // Si tampoco existe, lanzamos una excepción
        if (getClass().getResource(imagePathJPG) == null) {
            throw new Exception("No se encontró la imagen para: " + nombreProducto);
        }
        
        return imagePathJPG;
    }
    
    private VBox crearTarjetaProducto(Producto producto) {
        // Crear el contenedor principal
        VBox tarjeta = new VBox(10);
        tarjeta.setStyle(
            "-fx-background-color: #1a1a1a;" +
            "-fx-padding: 15;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        );
        tarjeta.setMaxWidth(300);
        
        // Contenedor para la información del producto
        VBox contenedorInfo = new VBox(5);
        
        
        // Información del producto
        Label nombre = new Label(producto.getNombre());
        nombre.setStyle(
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 16px;"
        );
        nombre.setWrapText(true);
        nombre.setMaxWidth(250);
        
        String precioFormateado = String.format("$%,.2f", producto.getPrecio());
        Label precio = new Label(precioFormateado);
        precio.setStyle(
            "-fx-text-fill: #00ff8a; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 18px;"
        );
        
        contenedorInfo.getChildren().addAll(nombre, precio);
        
        // Botones de acción
        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setStyle(
            "-fx-background-color: #ff4444;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 5 15;"
        );
        btnEliminar.setOnAction(e -> {
            listaDeseosService.eliminarDeListaDeseos(USUARIO_ID, producto.getId());
            cargarListaDeseos(); // Recargar la lista
        });
        
        Button btnAlCarrito = new Button("Añadir al carrito");
        btnAlCarrito.setStyle(
            "-fx-background-color: #00ff8a;" +
            "-fx-text-fill: #0d0d0d;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 5 15;"
        );
        btnAlCarrito.setOnAction(e -> {
            // Añadir al carrito usando el callback
            if (onAgregarAlCarritoCallback != null) {
                onAgregarAlCarritoCallback.accept(producto);
            }
            // Eliminar de la lista de deseos después de añadir al carrito
            listaDeseosService.eliminarDeListaDeseos(USUARIO_ID, producto.getId());
            cargarListaDeseos(); // Recargar la lista
            
            // Mostrar mensaje de éxito
            mostrarMensaje("Producto añadido al carrito");
        });
        
        botones.getChildren().addAll(btnEliminar, btnAlCarrito);
        
        // Agregar elementos a la tarjeta
        tarjeta.getChildren().addAll(contenedorInfo, botones);
        
        return tarjeta;
    }

    private void mostrarMensaje(String mensaje) {
        // Crear un diálogo de información
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cerrar() {
        Stage stage = (Stage) contenedorProductos.getScene().getWindow();
        stage.close();
    }
}
