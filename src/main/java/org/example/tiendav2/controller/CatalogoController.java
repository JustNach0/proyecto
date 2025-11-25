package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.tiendav2.model.Producto;
import org.example.tiendav2.model.Carrito;
import org.example.tiendav2.model.Pedido;
import org.example.tiendav2.model.ListaDoblementeEnlazada;
import org.example.tiendav2.service.PedidoService;

import java.util.ArrayList;
import java.util.List;

public class CatalogoController {

    @FXML
    private GridPane grid;
    
    private final Carrito carrito = new Carrito(1); // Asumiendo que el ID de usuario es 1 por ahora
    private final PedidoService pedidoService = PedidoService.getInstancia();
    private final ListaDoblementeEnlazada<Producto> productosDisponibles = new ListaDoblementeEnlazada<>();
    private int productoActualIndex = 0;

    @FXML
    private void initialize() {

        inicializarProductos();

        mostrarProductos();
    }
    
    private void inicializarProductos() {

        productosDisponibles.agregar(new Producto(1, "Chaqueta Nova LayerWind", 299900, "Chaqueta resistente al viento con capucha desmontable"));
        productosDisponibles.agregar(new Producto(2, "Pantalón Cargo StreetFlex 2.0", 189900, "Pantalones cargo con múltiples bolsillos y ajuste moderno"));
        productosDisponibles.agregar(new Producto(3, "Jogger SoftFlex Premium", 159900, "Pantalones jogger en algodón elástico para máxima comodidad"));
        productosDisponibles.agregar(new Producto(4, "Camiseta Essential Cotton", 49900, "Camiseta básica 100% algodón, disponible en varios colores"));
        productosDisponibles.agregar(new Producto(5, "Sudadera Oversize Urban", 179900, "Sudadera con capucha y corte oversize"));
        productosDisponibles.agregar(new Producto(6, "Zapatillas UrbanWalk Pro", 349900, "Zapatillas deportivas con soporte para caminata"));
        productosDisponibles.agregar(new Producto(7, "Gorra Snapback Classic", 45900, "Gorra ajustable con diseño urbano"));
        productosDisponibles.agregar(new Producto(8, "Chaleco Térmico HeatTech", 219900, "Chaleco fino con tecnología de aislamiento térmico"));
        productosDisponibles.agregar(new Producto(9, "Polo SportFit", 89900, "Polo de manga corta con tecnología de secado rápido"));
        productosDisponibles.agregar(new Producto(10, "Shorts Running Flex", 129900, "Shorts deportivos con bolsillo para celular"));
        productosDisponibles.agregar(new Producto(11, "Camisa Lino Essential", 149900, "Camisa de lino para looks casuales de verano"));
        productosDisponibles.agregar(new Producto(12, "Cinturón Cuero Premium", 99900, "Cinturón de cuero genuino con hebilla metálica"));
        

        productosDisponibles.irAlPrimero();
    }
    
    private void mostrarProductos() {

        grid.getChildren().clear();
        

        int columnas = 3;
        int fila = 0;
        int columna = 0;
        

        Producto productoActual = productosDisponibles.obtenerActual();
        

        productosDisponibles.irAlPrimero();

        for (int i = 0; i < productosDisponibles.tamaño(); i++) {
            Producto producto = productosDisponibles.obtenerActual();
            VBox tarjeta = crearTarjetaProducto(producto);

            grid.add(tarjeta, columna, fila);

            productosDisponibles.siguiente();

            columna++;
            if (columna == columnas) {
                columna = 0;
                fila++;
            }
        }

        if (productoActual != null) {
            productosDisponibles.buscar(productoActual);
        }
    }

    private VBox crearTarjetaProducto(Producto producto) {
        Rectangle img = new Rectangle(280, 360);
        img.setArcWidth(16);
        img.setArcHeight(16);
        img.setStyle("-fx-fill: linear-gradient(to bottom, #222, #111);");

        Label name = new Label(producto.getNombre());
        name.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        String precioFormateado = String.format("$%,d", (int)producto.getPrecio());
        Label price = new Label(precioFormateado);
        price.setStyle("-fx-text-fill: #00ff8a; -fx-font-size: 15px; -fx-font-weight: bold;");

        Button btnAgregar = new Button("Agregar al carrito");
        btnAgregar.setStyle(
            "-fx-background-color: #00ff8a; " +
            "-fx-text-fill: #0d0d0d; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 8 16; " +
            "-fx-border-radius: 4; " +
            "-fx-background-radius: 4;"
        );

        btnAgregar.setOnMouseEntered(e -> {
            btnAgregar.setStyle(
                "-fx-background-color: #00cc6f; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8 16; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4;"
            );
        });

        btnAgregar.setOnMouseExited(e -> {
            btnAgregar.setStyle(
                "-fx-background-color: #00ff8a; " +
                "-fx-text-fill: #0d0d0d; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8 16; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4;"
            );
        });

        btnAgregar.setOnAction(e -> agregarAlCarrito(producto));

        VBox box = new VBox(12, img, name, price, btnAgregar);
        box.setPadding(new Insets(16));
        box.setStyle(
            "-fx-background-color: #1a1a1a; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        );

        box.setOnMouseEntered(e -> animarHover(box, true));
        box.setOnMouseExited(e -> animarHover(box, false));

        return box;
    }
    
    /**
     * Agrega un producto al carrito y muestra un mensaje de confirmación.
     */
    private void agregarAlCarrito(Producto producto) {
        carrito.agregarProducto(producto);
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Producto agregado");
        alert.setHeaderText(null);
        alert.setContentText("¡" + producto.getNombre() + " ha sido agregado al carrito!");
        alert.showAndWait();

        actualizarContadorCarrito();
    }
    

    @FXML
    private void finalizarCompra() {
        if (carrito.estaVacio()) {
            mostrarAlerta("Carrito vacío", "No hay productos en el carrito.");
            return;
        }

        String direccion = "Calle Falsa 123";
        String metodoPago = "Tarjeta de crédito";
        
        try {
            Pedido pedido = pedidoService.crearPedido(carrito, direccion, metodoPago);

            mostrarAlerta("Pedido realizado", "Su pedido ha sido registrado con éxito. Número de pedido: " + 
                             pedido.getId());

            carrito.vaciarCarrito();
            actualizarContadorCarrito();

            pedidoService.mostrarEstadoCola();
            
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error al crear el pedido", e.getMessage());
        }
    }
    

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    

    private void actualizarContadorCarrito() {
        System.out.println("Productos en el carrito: " + carrito.getCantidadProductos());
        System.out.println("Total: $" + carrito.getTotal());
    }

    @FXML
    private void productoAnterior() {
        if (productosDisponibles.anterior()) {
            mostrarProductoActual();
        } else {
            System.out.println("Ya estás en el primer producto");
        }
    }

    @FXML
    private void productoSiguiente() {
        if (productosDisponibles.siguiente()) {
            mostrarProductoActual();
        } else {
            System.out.println("Ya estás en el último producto");
        }
    }

    private void mostrarProductoActual() {
        Producto producto = productosDisponibles.obtenerActual();
        if (producto != null) {
            grid.getChildren().clear();

            VBox tarjeta = crearTarjetaProducto(producto);
            grid.add(tarjeta, 0, 0);
            System.out.println("Mostrando producto " + (productosDisponibles.obtenerIndiceActual() + 1) + 
                             " de " + productosDisponibles.tamaño());
        }
    }

    @FXML
    private void activarModoNavegacion() {
        productosDisponibles.irAlPrimero();

        mostrarProductoActual();
    }

    @FXML
    private void desactivarModoNavegacion() {
        mostrarProductos();
    }

    private void animarHover(VBox box, boolean hover) {
        double scaleTo = hover ? 1.03 : 1.0;
        
        ScaleTransition st = new ScaleTransition(Duration.millis(200), box);
        st.setToX(scaleTo);
        st.setToY(scaleTo);

        if (hover) {
            box.setStyle(
                box.getStyle() + 
                "-fx-effect: dropshadow(gaussian, rgba(0,255,138,0.4), 15, 0, 0, 8);"
            );
        } else {
            box.setStyle(
                "-fx-background-color: #1a1a1a; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);"
            );
        }
        
        st.play();
    }
}
