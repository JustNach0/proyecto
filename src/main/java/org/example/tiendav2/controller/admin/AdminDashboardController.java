package org.example.tiendav2.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.tiendav2.service.AuthService;
import org.example.tiendav2.service.HistorialService;
import org.example.tiendav2.service.PedidoService;
import org.example.tiendav2.service.ProductoService;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Label lblNombreUsuario;
    
    @FXML
    private Label lblRol;
    
    @FXML
    private StackPane contentArea;
    
    @FXML
    private Label lblTotalVentas;
    
    @FXML
    private Label lblTotalPedidos;
    
    @FXML
    private Label lblTotalUsuarios;
    
    @FXML
    private Label lblTotalProductos;
    
    private final PedidoService pedidoService = PedidoService.getInstancia();
    private final ProductoService productoService = new ProductoService();
    private final HistorialService historialService = HistorialService.getInstancia();
    
    @FXML
    public void initialize() {
        // Mostrar información del usuario actual
        if (AuthService.getUsuarioActual() != null) {
            lblNombreUsuario.setText(AuthService.getUsuarioActual().getNombre());
            lblRol.setText(AuthService.getUsuarioActual().getRol().toString());
        }
        
        // Cargar estadísticas
        cargarEstadisticas();
    }
    
    private void cargarEstadisticas() {
        // Total de pedidos
        int totalPedidos = pedidoService.obtenerNumeroPedidosPendientes();
        lblTotalPedidos.setText(String.valueOf(totalPedidos));
        
        // Total de productos
        int totalProductos = productoService.obtenerTodosLosProductos().size();
        lblTotalProductos.setText(String.valueOf(totalProductos));
        
        // Total de ventas (suma de todos los historiales)
        double totalVentas = historialService.obtenerHistorialUsuario(1).stream()
            .mapToDouble(compra -> compra.getTotal())
            .sum();
        lblTotalVentas.setText(String.format("$%,.2f", totalVentas));
        
        // Total de usuarios (por ahora fijo, necesitarías un UserService)
        lblTotalUsuarios.setText("2"); // Admin + Usuario normal
    }
    
    @FXML
    private void handleGestionPedidos() {
        System.out.println("=== BOTÓN GESTIÓN PEDIDOS PRESIONADO ===");
        loadView("Gestión de Pedidos", "/org/example/tiendav2/fxml/admin/gestion-pedidos.fxml");
    }
    
    @FXML
    private void handleGestionUsuarios() {
        System.out.println("=== BOTÓN GESTIÓN USUARIOS PRESIONADO ===");
        loadView("Gestionar Usuarios", "/org/example/tiendav2/fxml/admin/usuarios.fxml");
    }
    
    @FXML
    private void handleGestionProductos() {
        System.out.println("=== BOTÓN GESTIÓN PRODUCTOS PRESIONADO ===");
        loadView("Gestionar Productos", "/org/example/tiendav2/fxml/admin/productos.fxml");
    }
    
    @FXML
    private void handleReportes() {
        System.out.println("=== BOTÓN REPORTES PRESIONADO ===");
        loadView("Reportes", "/org/example/tiendav2/fxml/admin/reportes.fxml");
    }
    
    @FXML
    private void handleCerrarSesion() {
        try {
            // Cerrar sesión
            AuthService.cerrarSesion();
            
            // Cargar la pantalla de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tiendav2/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) lblNombreUsuario.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadView(String title, String fxmlPath) {
        try {
            System.out.println("Intentando cargar vista: " + fxmlPath);
            
            // Verificar si el recurso existe
            if (getClass().getResource(fxmlPath) == null) {
                System.err.println("ERROR: No se encontró el archivo FXML: " + fxmlPath);
                mostrarMensajeEnContentArea("⚠️ Vista no disponible", 
                    "La vista '" + title + "' aún no está implementada.\n\nArchivo: " + fxmlPath);
                return;
            }
            
            // Limpiar el área de contenido
            contentArea.getChildren().clear();
            
            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            // Agregar la vista al área de contenido
            contentArea.getChildren().add(view);
            
            // Actualizar el título de la ventana
            Stage stage = (Stage) contentArea.getScene().getWindow();
            if (stage != null) {
                stage.setTitle("Panel de Administración - " + title);
            }
            
            System.out.println("Vista cargada exitosamente: " + title);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista: " + fxmlPath);
            System.err.println("Mensaje de error: " + e.getMessage());
            mostrarMensajeEnContentArea("❌ Error al cargar", 
                "No se pudo cargar la vista '" + title + "'.\n\nError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error inesperado: " + e.getMessage());
            mostrarMensajeEnContentArea("❌ Error inesperado", 
                "Ocurrió un error al cargar '" + title + "'.\n\nError: " + e.getMessage());
        }
    }
    
    private void mostrarMensajeEnContentArea(String titulo, String mensaje) {
        contentArea.getChildren().clear();
        
        javafx.scene.layout.VBox messageBox = new javafx.scene.layout.VBox(20);
        messageBox.setAlignment(javafx.geometry.Pos.CENTER);
        messageBox.setStyle("-fx-padding: 50;");
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        
        Label lblMensaje = new Label(mensaje);
        lblMensaje.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-text-alignment: center;");
        lblMensaje.setWrapText(true);
        lblMensaje.setMaxWidth(600);
        
        messageBox.getChildren().addAll(lblTitulo, lblMensaje);
        contentArea.getChildren().add(messageBox);
    }
}
