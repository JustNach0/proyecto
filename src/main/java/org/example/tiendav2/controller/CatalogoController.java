package org.example.tiendav2.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.tiendav2.model.Carrito;
import org.example.tiendav2.model.ListaDoblementeEnlazada;
import org.example.tiendav2.model.Pedido;
import org.example.tiendav2.model.Producto;
import org.example.tiendav2.service.PedidoService;
import org.example.tiendav2.service.ProductoService;

import java.util.Optional;
import java.util.List;

public class CatalogoController {

    @FXML
    private GridPane grid;
    @FXML
    private Label contadorCarrito;
    @FXML
    private VBox carritoContenido;
    @FXML
    private ScrollPane scrollCarrito;
    @FXML
    private Label totalCarrito;
    
    private final Carrito carrito = new Carrito(1); // Asumiendo que el ID de usuario es 1 por ahora
    private final PedidoService pedidoService = PedidoService.getInstancia();
    private final ListaDoblementeEnlazada<Producto> productosDisponibles = new ListaDoblementeEnlazada<>();
    private final ProductoService productoService = new ProductoService();
    private int productoActualIndex = 0;

    @FXML
    private void initialize() {
        inicializarProductos();
        mostrarProductos();
        actualizarVistaCarrito();
    }
    
    private void inicializarProductos() {
        // Obtener los productos del servicio
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        
        // Agregar los productos a la lista doblemente enlazada
        for (Producto producto : productos) {
            productosDisponibles.agregar(producto);
        }
        
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
        // Crear un contenedor para la imagen
        StackPane imgContainer = new StackPane();
        imgContainer.setMinSize(280, 360);
        imgContainer.setMaxSize(280, 360);
        
        // Crear un rectángulo como fondo/placeholder
        Rectangle placeholder = new Rectangle(280, 360);
        placeholder.setArcWidth(16);
        placeholder.setArcHeight(16);
        placeholder.setStyle("-fx-fill: linear-gradient(to bottom, #222, #111);");
        
        // Crear el ImageView para la imagen del producto
        ImageView imgView = new ImageView();
        imgView.setFitWidth(280);
        imgView.setFitHeight(360);
        imgView.setPreserveRatio(true);
        
        // Intentar cargar la imagen del producto
        try {
            // Formatear el nombre del archivo de imagen según la convención
            // Mapeo de nombres de productos a nombres de archivo
            String nombreArchivo;
            String nombreProducto = producto.getNombre().toLowerCase();
            
            // Casos especiales
            if (nombreProducto.contains("atticus")) {
                nombreArchivo = "atticus.png";
            } else if (nombreProducto.contains("enzo")) {
                nombreArchivo = "enzowhiteblueed.png";
            } else if (nombreProducto.contains("hygiea")) {
                nombreArchivo = "hygiea.png";
            } else if (nombreProducto.contains("classic cap")) {
                nombreArchivo = "classiccap.png";
            } else if (nombreProducto.contains("gringnani")) {
                nombreArchivo = "gringnaniblackcap.png";
            } else if (nombreProducto.contains("lrn victoria top grey")) {
                nombreArchivo = "lrnvictoriatopgrey.png";
            } else if (nombreProducto.contains("lrn victoria top black")) {
                nombreArchivo = "lrnvictoriatopblack.png";
            } else {
                // Para el resto, usar la lógica original pero más simple
                nombreArchivo = nombreProducto
                    .replace(" | ", "")
                    .replace(" ", "")
                    .replace("/", "")
                    .replace("[", "")
                    .replace("]", "")
                    .replace("|", "")
                    + ".png";
            }
                
            // Usando la ruta completa desde src/main/resources
            String imagePath = "/org/example/tiendav2/images/productos/" + nombreArchivo;
            
            // Depuración: Mostrar la ruta completa que se está intentando cargar
            System.out.println("Intentando cargar: " + imagePath);
            
            // Verificar si el recurso existe
            java.net.URL imgUrl = getClass().getResource(imagePath);
            System.out.println("URL del recurso: " + imgUrl);
            
            java.io.InputStream inputStream = getClass().getResourceAsStream(imagePath);
            if (inputStream == null) {
                System.err.println("No se encontró la imagen: " + imagePath);
                System.err.println("Ruta absoluta del recurso: " + 
                    (imgUrl != null ? imgUrl.toExternalForm() : "null"));
                throw new Exception("Imagen no encontrada: " + imagePath);
            }
            
            // Cargar la imagen
            Image imagen = new Image(inputStream);
            if (imagen.isError()) {
                throw new Exception("Error al cargar la imagen: " + imagen.getException().getMessage());
            }
            
            imgView.setImage(imagen);
            imgContainer.getChildren().add(imgView);
            
        } catch (Exception e) {
            // Si hay un error, mostramos el placeholder con un mensaje
            System.err.println("No se pudo cargar la imagen para: " + producto.getNombre());
            System.err.println("Error: " + e.getMessage());
            
            Label placeholderText = new Label("Imagen no disponible\n" + producto.getNombre());
            placeholderText.setTextFill(Color.WHITE);
            placeholderText.setStyle("-fx-text-alignment: center; -fx-font-size: 14px;");
            
            imgContainer.getChildren().addAll(placeholder, placeholderText);
            StackPane.setAlignment(placeholderText, Pos.CENTER);
        }

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

        VBox box = new VBox(12, imgContainer, name, price, btnAgregar);
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
     * Agrega un producto al carrito y actualiza la vista.
     */
    private void agregarAlCarrito(Producto producto) {
        if (producto == null) return;
        
        carrito.agregarProducto(producto);
        actualizarVistaCarrito();
        
        // Mostrar notificación de producto agregado
        mostrarNotificacion("✓ " + producto.getNombre() + " agregado al carrito");
    }
    
    /**
     * Muestra una notificación temporal en la esquina inferior derecha
     */
    private void mostrarNotificacion(String mensaje) {
        Label notificacion = new Label(mensaje);
        notificacion.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 10px 15px;" +
            "-fx-background-radius: 5px;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 2);"
        );
        
        StackPane root = (StackPane) grid.getScene().getRoot();
        StackPane.setAlignment(notificacion, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(notificacion, new Insets(0, 20, 20, 0));
        
        root.getChildren().add(notificacion);
        
        // Animación de desvanecimiento
        FadeTransition ft = new FadeTransition(Duration.millis(3000), notificacion);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> root.getChildren().remove(notificacion));
        ft.play();
    }
    
    /**
     * Actualiza la vista del carrito mostrando los productos y el total
     */
    private void actualizarVistaCarrito() {
        carritoContenido.getChildren().clear();
        
        if (carrito.estaVacio()) {
            Label vacio = new Label("El carrito está vacío");
            vacio.setStyle("-fx-text-fill: #888; -fx-font-style: italic;");
            carritoContenido.getChildren().add(vacio);
        } else {
            for (int i = 0; i < carrito.getCantidadProductos(); i++) {
                Producto producto = carrito.obtenerProducto(i);
                HBox item = crearItemCarrito(producto);
                carritoContenido.getChildren().add(item);
            }
        }
        
        // Actualizar total
        totalCarrito.setText(String.format("$%,.2f", carrito.getTotal()));
        actualizarContadorCarrito();
    }
    
    /**
     * Crea un elemento de producto para mostrar en el carrito
     */
    private HBox crearItemCarrito(Producto producto) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setStyle("-fx-padding: 10; -fx-background-color: #1a1a1a; -fx-background-radius: 5;");
        
        Label nombre = new Label(producto.getNombre());
        nombre.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        
        Label precio = new Label(String.format("$%,.0f", producto.getPrecio()));
        precio.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        
        Button btnEliminar = new Button("×");
        btnEliminar.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #ff4444; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 0 8px;"
        );
        btnEliminar.setOnAction(e -> {
            carrito.removerProducto(producto);
            actualizarVistaCarrito();
        });
        
        item.getChildren().addAll(nombre, new Region(), precio, btnEliminar);
        HBox.setHgrow(item.getChildren().get(1), Priority.ALWAYS);
        
        return item;
    }
    

    @FXML
    private void finalizarCompra() {
        if (carrito.estaVacio()) {
            mostrarAlerta("Carrito vacío", "No hay productos en el carrito.");
            return;
        }

        // Mostrar diálogo para ingresar dirección de envío
        TextInputDialog direccionDialog = new TextInputDialog("Calle Falsa 123");
        direccionDialog.setTitle("Dirección de envío");
        direccionDialog.setHeaderText("Ingrese la dirección de envío");
        direccionDialog.setContentText("Dirección:");

        Optional<String> direccionResult = direccionDialog.showAndWait();
        if (direccionResult.isEmpty() || direccionResult.get().trim().isEmpty()) {
            return;
        }
        String direccion = direccionResult.get();

        // Mostrar diálogo para seleccionar método de pago
        ChoiceDialog<String> pagoDialog = new ChoiceDialog<>("Tarjeta de crédito", 
            "Tarjeta de crédito", "PayPal", "Transferencia bancaria");
        pagoDialog.setTitle("Método de pago");
        pagoDialog.setHeaderText("Seleccione el método de pago");
        pagoDialog.setContentText("Método de pago:");

        Optional<String> pagoResult = pagoDialog.showAndWait();
        if (pagoResult.isEmpty()) {
            return;
        }
        String metodoPago = pagoResult.get();

        try {
            // Crear el pedido
            Pedido pedido = pedidoService.crearPedido(carrito, direccion, metodoPago);
            
            // Mostrar confirmación
            Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
            confirmacion.setTitle("Compra exitosa");
            confirmacion.setHeaderText("¡Gracias por tu compra!");
            confirmacion.setContentText(String.format(
                "Pedido #%d realizado con éxito.\n" +
                "Total: $%,.2f\n" +
                "Dirección de envío: %s\n" +
                "Método de pago: %s",
                pedido.getId(), 
                pedido.getTotal(), 
                direccion, 
                metodoPago
            ));
            confirmacion.showAndWait();
            
            // Limpiar el carrito después de la compra
            carrito.vaciarCarrito();
            actualizarVistaCarrito();
            
            // Mostrar estado de la cola de pedidos
            pedidoService.mostrarEstadoCola();
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al procesar el pedido: " + e.getMessage());
        }
    }
    

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    

    private void actualizarContadorCarrito() {
        int cantidad = carrito.getCantidadProductos();
        contadorCarrito.setText(String.valueOf(cantidad));
        contadorCarrito.setVisible(cantidad > 0);
    }

    @FXML
    private void productoAnterior() {
        if (productosDisponibles.anterior()) {
            mostrarProductoActual();
        } else {
            mostrarNotificacion("Ya estás en el primer producto");
        }
    }
    
    @FXML
    private void productoSiguiente() {
        if (productosDisponibles.siguiente()) {
            mostrarProductoActual();
        } else {
            mostrarNotificacion("No hay más productos");
        }
    }
    
    @FXML
    private void activarModoNavegacion() {
        productosDisponibles.irAlPrimero();
        mostrarProductoActual();
        mostrarNotificacion("Modo de navegación detallada activado");
    }
    
    @FXML
    private void desactivarModoNavegacion() {
        mostrarProductos();
        mostrarNotificacion("Vista de cuadrícula activada");
    }
    
    @FXML
    private void vaciarCarrito() {
        if (carrito.estaVacio()) {
            mostrarNotificacion("El carrito ya está vacío");
            return;
        }
        
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Vaciar carrito");
        confirmacion.setHeaderText("¿Estás seguro de que quieres vaciar el carrito?");
        confirmacion.setContentText("Se eliminarán todos los productos del carrito.");
        
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            carrito.vaciarCarrito();
            actualizarVistaCarrito();
            mostrarNotificacion("Carrito vaciado");
        }
    }
    
    private void mostrarProductoActual() {
        if (!productosDisponibles.estaVacia()) {
            Producto producto = productosDisponibles.obtenerActual();
            if (producto != null) {
                grid.getChildren().clear();
                VBox tarjeta = crearTarjetaProducto(producto);
                grid.add(tarjeta, 0, 0);
                System.out.println("Mostrando producto " + (productosDisponibles.obtenerIndiceActual() + 1) + 
                               " de " + productosDisponibles.tamaño());
            }
        }
    }

    private void animarHover(VBox box, boolean hover) {
        double scaleTo = hover ? 1.03 : 1.0;
        
        ScaleTransition st = new ScaleTransition(Duration.millis(200), box);
        st.setToX(scaleTo);
        st.setToY(scaleTo);

        if (hover) {
            // Aplicar estilo de hover
            box.setStyle(
                "-fx-background-color: #1a1a1a; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,255,138,0.4), 15, 0, 0, 8);"
            );
        } else {
            // Restaurar el estilo original
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
