package org.example.tiendav2.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.example.tiendav2.model.Carrito;
import org.example.tiendav2.model.ListaDoblementeEnlazada;
import org.example.tiendav2.model.ListaDeseos;
import org.example.tiendav2.model.Pedido;
import org.example.tiendav2.model.Producto;
import org.example.tiendav2.service.HistorialService;
import org.example.tiendav2.service.ListaDeseosService;
import org.example.tiendav2.service.PedidoService;
import org.example.tiendav2.service.ProductoService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

public class CatalogoController {

    @FXML
    private GridPane grid;
    @FXML
    private Label contadorCarrito;
    @FXML
    private Label contadorListaDeseos;
    @FXML
    private Label contadorItems;
    @FXML
    private VBox carritoContenido;
    @FXML
    private ScrollPane scrollCarrito;
    @FXML
    private Label totalCarrito;
    
    private final Carrito carrito = new Carrito(1); // Asumiendo que el ID de usuario es 1 por ahora
    private final PedidoService pedidoService = PedidoService.getInstancia();
    private final ListaDoblementeEnlazada<Producto> productosDisponibles = new ListaDoblementeEnlazada<>();
    private ListaDoblementeEnlazada<Producto> productosFiltrados = new ListaDoblementeEnlazada<>();
    private final ProductoService productoService = new ProductoService();
    private final ListaDeseosService listaDeseosService = ListaDeseosService.getInstancia();
    private final HistorialService historialService = HistorialService.getInstancia();
    private int productoActualIndex = 0;

    @FXML
    private StackPane btnCarrito;
    @FXML
    private VBox panelCarrito;
    
    @FXML
    private VBox navegacionDetallada;
    @FXML
    private Label nombreProductoDetallado;
    @FXML
    private Label precioProductoDetallado;
    @FXML
    private Label descripcionProductoDetallado;
    @FXML
    private ImageView imagenProductoDetallado;
    
    private Producto productoActualDetallado;
    
    @FXML
    private void initialize() {
        inicializarProductos();
        mostrarProductos();
        actualizarVistaCarrito();
        actualizarContadorListaDeseos();
        
        // Configurar el evento de clic para el botón del carrito
        btnCarrito.setOnMouseClicked(e -> toggleCarrito());
    }
    
    /**
     * Muestra u oculta el panel del carrito
     */
    private void toggleCarrito() {
        boolean isVisible = panelCarrito.isVisible();
        panelCarrito.setVisible(!isVisible);
        
        // Aplicar una animación suave
        FadeTransition ft = new FadeTransition(Duration.millis(200), panelCarrito);
        ft.setFromValue(isVisible ? 1.0 : 0.0);
        ft.setToValue(isVisible ? 0.0 : 1.0);
        
        if (!isVisible) {
            panelCarrito.setVisible(true);
            panelCarrito.setOpacity(0);
        }
        
        ft.play();
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
    
    @FXML
    private void productoAnterior() {
        if (productosDisponibles.anterior()) {
            mostrarProductoDetallado(productosDisponibles.obtenerActual());
        } else {
            // Si no hay anterior, ir al último
            productosDisponibles.irAlUltimo();
            mostrarProductoDetallado(productosDisponibles.obtenerActual());
        }
    }
    
    @FXML
    private void productoSiguiente() {
        if (productosDisponibles.siguiente()) {
            mostrarProductoDetallado(productosDisponibles.obtenerActual());
        } else {
            // Si no hay siguiente, volver al primero
            productosDisponibles.irAlPrimero();
            mostrarProductoDetallado(productosDisponibles.obtenerActual());
        }
    }
    
    @FXML
    private void activarModoNavegacion() {
        grid.setVisible(false);
        navegacionDetallada.setVisible(true);
        
        // Mostrar el primer producto
        if (productosDisponibles.tamaño() > 0) {
            productosDisponibles.irAlPrimero();
            mostrarProductoDetallado(productosDisponibles.obtenerActual());
        }
        mostrarNotificacion("Modo de navegación detallada activado");
    }
    
    @FXML
    private void desactivarModoNavegacion() {
        navegacionDetallada.setVisible(false);
        grid.setVisible(true);
        mostrarNotificacion("Vista de cuadrícula activada");
    }
    
    private void mostrarProductoDetallado(Producto producto) {
        if (producto == null) return;
        
        productoActualDetallado = producto;
        nombreProductoDetallado.setText(producto.getNombre());
        precioProductoDetallado.setText(String.format("$%,d", (int)producto.getPrecio()));
        descripcionProductoDetallado.setText(obtenerDescripcionProducto(producto));
        
        // Cargar la imagen del producto
        try {
            // Formatear el nombre del archivo de imagen según la convención
            // Mapeo de nombres de productos a nombres de archivo
            String nombreArchivo = producto.getNombre().toLowerCase()
                .replace(" ", "")
                .replace("|", "")
                .replace("[", "")
                .replace("]", "")
                .replace("/", "")
                .replace(" ", "") + ".png";
                
            String imagePath = "/org/example/tiendav2/images/productos/" + nombreArchivo;
            
            Image imagen = new Image(getClass().getResourceAsStream(imagePath));
            
            if (imagen.isError()) {
                throw new Exception("Error al cargar la imagen");
            }
            
            imagenProductoDetallado.setImage(imagen);
            imagenProductoDetallado.setFitWidth(500);
            imagenProductoDetallado.setPreserveRatio(true);
            
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            // Usar una imagen por defecto
            imagenProductoDetallado.setImage(new Image("/org/example/tiendav2/images/placeholder.png"));
        }
    }
    
    private String obtenerDescripcionProducto(Producto producto) {
        // Aquí puedes personalizar la descripción según el producto
        // Por ahora, devolvemos la descripción genérica
        return producto.getDescripcion() + "\n\nTalla única. Envíos a todo el país.";
    }
    
    @FXML
    private void agregarAlCarritoDesdeDetalle() {
        if (productoActualDetallado != null) {
            agregarAlCarrito(productoActualDetallado);
        }
    }
    
    @FXML
    private void agregarAListaDeseosDesdeDetalle() {
        if (productoActualDetallado != null) {
            agregarAListaDeseos(productoActualDetallado);
        }
    }
    
    private void mostrarProductos() {
        grid.getChildren().clear();
        
        int columnas = 3;
        int fila = 0;
        int columna = 0;
        
        // Use the filtered list if it has items, otherwise use the full list
        ListaDoblementeEnlazada<Producto> listaAMostrar = 
            (productosFiltrados.tamaño() > 0) ? productosFiltrados : productosDisponibles;
            
        Producto productoActual = listaAMostrar.obtenerActual();
        
        listaAMostrar.irAlPrimero();

        for (int i = 0; i < listaAMostrar.tamaño(); i++) {
            Producto producto = listaAMostrar.obtenerActual();
            if (producto != null) {
                VBox tarjeta = crearTarjetaProducto(producto);
                grid.add(tarjeta, columna, fila);

                columna++;
                if (columna == columnas) {
                    columna = 0;
                    fila++;
                }
            }
            
            if (!listaAMostrar.siguiente()) {
                break; // In case there's an issue with the list
            }
        }

        if (productoActual != null) {
            listaAMostrar.buscar(productoActual);
        }
    }

    private VBox crearTarjetaProducto(Producto producto) {
        // Crear un contenedor para la imagen
        StackPane imgContainer = new StackPane();
        
        // Botón de lista de deseos - Mejorado para mejor visibilidad
        Button btnListaDeseos = new Button("❤");
        btnListaDeseos.setStyle(
            "-fx-background-color: rgba(255,255,255,0.9); " +
            "-fx-text-fill: #ff4444; " +
            "-fx-font-size: 18px; " +
            "-fx-min-width: 40px; " +
            "-fx-min-height: 40px; " +
            "-fx-background-radius: 20px; " +
            "-fx-cursor: hand;"
        );
        
        // Efecto hover para mejor feedback visual
        btnListaDeseos.setOnMouseEntered(e -> {
            btnListaDeseos.setStyle(
                "-fx-background-color: #ff4444; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 20px; " +
                "-fx-min-width: 40px; " +
                "-fx-min-height: 40px; " +
                "-fx-background-radius: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 2);"
            );
        });
        
        btnListaDeseos.setOnMouseExited(e -> {
            btnListaDeseos.setStyle(
                "-fx-background-color: rgba(255,255,255,0.9); " +
                "-fx-text-fill: #ff4444; " +
                "-fx-font-size: 18px; " +
                "-fx-min-width: 40px; " +
                "-fx-min-height: 40px; " +
                "-fx-background-radius: 20px;"
            );
        });
        
        btnListaDeseos.setOnAction(e -> {
            agregarAListaDeseos(producto);
            // Efecto de escala al hacer clic
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnListaDeseos);
            st.setFromX(1.0);
            st.setFromY(1.0);
            st.setToX(1.3);
            st.setToY(1.3);
            st.setAutoReverse(true);
            st.setCycleCount(2);
            st.play();
        });
        
        // Configurar el contenedor de la imagen
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
            
            // Agregar el botón de lista de deseos después de la imagen
            imgContainer.getChildren().add(btnListaDeseos);
            btnListaDeseos.toFront(); // Asegurar que esté en la parte superior
            StackPane.setAlignment(btnListaDeseos, Pos.TOP_RIGHT);
            StackPane.setMargin(btnListaDeseos, new Insets(15, 15, 0, 0));
            
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
     * Agrega un producto a la lista de deseos, actualiza el contador y muestra una notificación
     */
    private void agregarAListaDeseos(Producto producto) {
        if (producto == null) {
            System.out.println("Error: Producto nulo al intentar agregar a lista de deseos");
            return;
        }
        
        System.out.println("Intentando agregar producto a lista de deseos: " + producto.getNombre());
        int usuarioId = 1; // ID del usuario actual (deberías obtenerlo de la sesión)
        
        try {
            // Crear el pedido
            boolean exito = listaDeseosService.agregarAListaDeseos(usuarioId, producto.getId());
            
            if (exito) {
                System.out.println("Producto agregado exitosamente a la lista de deseos");
                mostrarNotificacion("❤ " + producto.getNombre() + " agregado a la lista de deseos");
                // Actualizar el contador de la lista de deseos
                actualizarContadorListaDeseos();
            } else {
                System.out.println("No se pudo agregar el producto a la lista de deseos");
                mostrarNotificacion("✗ No se pudo agregar el producto a la lista de deseos");
            }
        } catch (Exception e) {
            System.err.println("Error al agregar a lista de deseos: " + e.getMessage());
            e.printStackTrace();
            mostrarNotificacion("✗ Error al agregar a la lista de deseos");
        }
    }
    
    /**
     * Muestra una notificación temporal en la esquina inferior derecha
     */
    private void mostrarNotificacion(String mensaje) {
        try {
            Label notificacion = new Label(mensaje);
            notificacion.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8);" +
                "-fx-text-fill: white;" +
                "-fx-padding: 10px 15px;" +
                "-fx-background-radius: 5px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 2);"
            );
            
            // Obtener el nodo raíz de la escena
            Parent root = grid.getScene().getRoot();
            
            // Crear una ventana emergente para la notificación
            Stage popup = new Stage();
            popup.initStyle(StageStyle.UNDECORATED);
            popup.initModality(Modality.NONE);
            
            StackPane popupContent = new StackPane(notificacion);
            popupContent.setStyle("-fx-background-color: transparent;");
            
            Scene scene = new Scene(popupContent);
            scene.setFill(Color.TRANSPARENT);
            popup.setScene(scene);
            
            // Posicionar en la esquina inferior derecha
            Window window = grid.getScene().getWindow();
            popup.setX(window.getX() + window.getWidth() - 320);
            popup.setY(window.getY() + window.getHeight() - 120);
            
            popup.show();
            
            // Cerrar después de 3 segundos
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> popup.close());
            delay.play();
        } catch (Exception e) {
            System.err.println("Error al mostrar notificación: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    /**
     * Actualiza el contador visual del carrito con la cantidad de productos.
     */
    private void actualizarContadorCarrito() {
        if (contadorCarrito != null) {
            contadorCarrito.setText(String.valueOf(carrito.getCantidadProductos()));
        }
    }

    /**
     * Actualiza el contador visual de la lista de deseos para el usuario actual.
     */
    private void actualizarContadorListaDeseos() {
        try {
            int usuarioId = 1;
            ListaDeseos lista = listaDeseosService.obtenerListaDeseos(usuarioId);
            int cantidad = (lista != null) ? lista.getCantidadProductos() : 0;
            if (contadorListaDeseos != null) {
                contadorListaDeseos.setText(String.valueOf(cantidad));
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar contador de lista de deseos: " + e.getMessage());
        }
    }

    /**
     * Aplica una animación suave al hacer hover sobre las tarjetas de producto.
     */
    private void animarHover(VBox box, boolean hover) {
        if (box == null) return;

        double toScale = hover ? 1.03 : 1.0;
        ScaleTransition st = new ScaleTransition(Duration.millis(150), box);
        st.setToX(toScale);
        st.setToY(toScale);
        st.play();
    }

    @FXML
    private void finalizarCompra() {
        if (carrito.estaVacio()) {
            mostrarAlerta("Carrito vacío", "No hay productos en el carrito.");
            return;
        }

        try {
            // Cargar el diálogo personalizado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tiendav2/fxml/dialogo-finalizar-compra.fxml"));
            Parent root = loader.load();
            
            // Obtener el controlador y pasar datos del carrito
            DialogoFinalizarCompraController dialogoController = loader.getController();
            dialogoController.setDatosCarrito(carrito);
            
            // Crear y mostrar el diálogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Finalizar Compra");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            
            // Verificar si se confirmó la compra
            if (!dialogoController.isConfirmado()) {
                return; // Usuario canceló
            }
            
            String direccion = dialogoController.getDireccion();
            String metodoPago = dialogoController.getMetodoPago();
            
            if (!validarDatosCompra(carrito, direccion, metodoPago)) {
                return;
            }

            // Crear el pedido
            Pedido pedido = pedidoService.crearPedido(carrito, direccion, metodoPago);
            if (pedido == null) {
                mostrarAlerta("Error", "No se pudo crear el pedido. Verifica los datos ingresados.");
                return;
            }
            
            // Mostrar confirmación con diálogo personalizado
            mostrarConfirmacionCompra(pedido, direccion, metodoPago);
            
            // Limpiar el carrito después de la compra
            carrito.vaciarCarrito();
            actualizarVistaCarrito();
            
            // Mostrar estado de la cola de pedidos
            pedidoService.mostrarEstadoCola();
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el dialogo de compra: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Ocurrió un error al procesar el pedido: " + e.getMessage());
        }
    }
    
    private void mostrarConfirmacionCompra(Pedido pedido, String direccion, String metodoPago) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tiendav2/fxml/dialogo-confirmacion-compra.fxml"));
            Parent root = loader.load();
            
            // Pasar datos al controlador de confirmación
            var controller = loader.getController();
            if (controller instanceof DialogoConfirmacionCompraController) {
                ((DialogoConfirmacionCompraController) controller).setDatosPedido(pedido, direccion, metodoPago);
            }
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Compra Exitosa");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            // Si falla el diálogo personalizado, usar uno básico
            Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
            confirmacion.setTitle("Compra exitosa");
            confirmacion.setHeaderText("¡Gracias por tu compra!");
            confirmacion.setContentText(String.format(
                "Pedido #%s realizado con éxito.\n" +
                "Total: $%,.2f\n" +
                "Dirección de envío: %s\n" +
                "Método de pago: %s",
                pedido.getId().substring(0, 8).toUpperCase(),
                pedido.getTotal(),
                direccion,
                metodoPago
            ));
            confirmacion.showAndWait();
        }
    }
    

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Validación adicional para evitar errores de NullPointerException en finalizarCompra
    private boolean validarDatosCompra(Carrito carrito, String direccion, String metodoPago) {
        if (carrito == null || carrito.estaVacio()) {
            mostrarAlerta("Error", "El carrito no puede estar vacío");
            return false;
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            mostrarAlerta("Error", "La dirección de envío es obligatoria");
            return false;
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            mostrarAlerta("Error", "Debe seleccionar un método de pago");
            return false;
        }
        return true;
    }
    
    @FXML
    private void vaciarCarrito() {
        carrito.vaciarCarrito();
        actualizarVistaCarrito();
        mostrarNotificacion("Carrito vaciado");
    }

    @FXML
    private void mostrarListaDeseos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tiendav2/fxml/ListaDeseosView.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Lista de Deseos");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la lista de deseos: " + e.getMessage());
        }
    }

    @FXML
    private void filtrarHombres() {
        filtrarPorCategoria("hombres");
    }

    @FXML
    private void filtrarMujeres() {
        filtrarPorCategoria("mujeres");
    }

    @FXML
    private void filtrarGorras() {
        filtrarPorCategoria("gorras");
    }

    private void filtrarPorCategoria(String categoria) {
        // Limpiar resultados anteriores
        productosFiltrados.limpiar();
        
        // Si la categoría es vacía o "todos", limpiar el filtro (simplemente dejamos productosFiltrados vacío)
        // y mostrarProductos() usará la lista completa
        if (categoria == null || categoria.isEmpty() || categoria.equalsIgnoreCase("todos")) {
            mostrarProductos();
            mostrarNotificacion("Mostrando todos los productos");
            return;
        }

        // Recorrer todos los productos disponibles
        productosDisponibles.irAlPrimero();
        for(int i = 0; i < productosDisponibles.tamaño(); i++) {
            Producto p = productosDisponibles.obtenerActual();
            
            if (p != null) {
                String nombre = p.getNombre().toLowerCase();
                String desc = p.getDescripcion() != null ? p.getDescripcion().toLowerCase() : "";
                String cat = categoria.toLowerCase();
                
                boolean coincide = false;
                
                // Lógica de coincidencia según categoría
                if (cat.equals("gorras") && (nombre.contains("gorra") || nombre.contains("cap") || nombre.contains("hat"))) {
                    coincide = true;
                } else if (cat.equals("hombres") && (nombre.contains("hombre") || nombre.contains("men") || desc.contains("hombre"))) {
                    coincide = true;
                } else if (cat.equals("mujeres") && (nombre.contains("mujer") || nombre.contains("women") || nombre.contains("top") || nombre.contains("dress") || desc.contains("mujer"))) {
                    coincide = true;
                }
                
                if (coincide) {
                    productosFiltrados.agregar(p);
                }
            }
            
            productosDisponibles.siguiente();
        }
        
        // Verificar si se encontraron productos
        if (productosFiltrados.tamaño() > 0) {
            mostrarNotificacion("Filtrado por: " + categoria + " (" + productosFiltrados.tamaño() + " productos)");
        } else {
            mostrarNotificacion("No se encontraron productos en la categoría: " + categoria);
        }
        
        mostrarProductos();
    }

    @FXML
    private void mostrarHistorialCompras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tiendav2/fxml/HistorialComprasView.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Historial de Compras");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el historial de compras: " + e.getMessage());
        }
    }
}
