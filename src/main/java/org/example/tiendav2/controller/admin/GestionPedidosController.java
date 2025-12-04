package org.example.tiendav2.controller.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.tiendav2.model.Pedido;
import org.example.tiendav2.service.PedidoService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GestionPedidosController {

    @FXML
    private TableView<Pedido> tablaPedidos;
    @FXML
    private TableColumn<Pedido, String> colId;
    @FXML
    private TableColumn<Pedido, String> colUsuario;
    @FXML
    private TableColumn<Pedido, String> colFecha;
    @FXML
    private TableColumn<Pedido, String> colTotal;
    @FXML
    private TableColumn<Pedido, String> colDireccion;
    @FXML
    private TableColumn<Pedido, String> colMetodoPago;
    @FXML
    private TableColumn<Pedido, String> colEstado;
    @FXML
    private TableColumn<Pedido, Void> colAcciones;
    
    @FXML
    private ComboBox<String> cmbFiltroEstado;
    @FXML
    private Label lblTotalPedidos;
    @FXML
    private Label lblPendientes;
    @FXML
    private Label lblEnProceso;
    @FXML
    private Label lblEntregados;

    private final PedidoService pedidoService = PedidoService.getInstancia();
    private ObservableList<Pedido> todosPedidos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar items del ComboBox
        cmbFiltroEstado.getItems().addAll("Todos", "PENDIENTE", "EN_PROCESO", "ENVIADO", "ENTREGADO", "CANCELADO");
        cmbFiltroEstado.setValue("Todos");
        
        configurarTabla();
        cargarPedidos();
        actualizarEstadisticas();
    }

    private void configurarTabla() {
        // Configurar columnas
        colId.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getId().substring(0, 8).toUpperCase()));
        
        colUsuario.setCellValueFactory(data -> 
            new SimpleStringProperty("Usuario #" + data.getValue().getCarrito().getUsuarioId()));
        
        colFecha.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getFechaCreacion()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        
        colTotal.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("$%,.2f", data.getValue().getTotal())));
        
        colDireccion.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getDireccionEnvio()));
        
        colMetodoPago.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getMetodoPago()));
        
        colEstado.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getEstado().toString()));
        
        // Columna de acciones con botones
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<Pedido.EstadoPedido> cmbEstado = new ComboBox<>();
            private final Button btnGuardar = new Button("💾");

            {
                cmbEstado.getItems().addAll(Pedido.EstadoPedido.values());
                cmbEstado.setStyle("-fx-font-size: 11px;");
                cmbEstado.setPrefWidth(120);
                
                btnGuardar.setStyle(
                    "-fx-background-color: #27ae60; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 5 10;"
                );
                
                btnGuardar.setOnAction(event -> {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    if (cmbEstado.getValue() != null) {
                        pedido.setEstado(cmbEstado.getValue());
                        actualizarTabla();
                        mostrarAlerta("Éxito", "Estado del pedido actualizado", Alert.AlertType.INFORMATION);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    cmbEstado.setValue(pedido.getEstado());
                    
                    HBox hbox = new HBox(5, cmbEstado, btnGuardar);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void cargarPedidos() {
        // Obtener todos los pedidos de la cola
        todosPedidos.clear();
        
        // Aquí deberías obtener los pedidos del servicio
        // Por ahora, obtenemos los de la cola
        var cola = pedidoService.getColaPedidos();
        
        // Nota: Necesitarías un método en PedidoService para obtener todos los pedidos
        // Por ahora usamos los que están en la cola
        System.out.println("Cargando pedidos desde el servicio...");
        
        tablaPedidos.setItems(todosPedidos);
    }

    @FXML
    private void actualizarLista() {
        cargarPedidos();
        actualizarEstadisticas();
    }

    @FXML
    private void filtrarPorEstado() {
        String filtro = cmbFiltroEstado.getValue();
        
        if (filtro == null || filtro.equals("Todos")) {
            tablaPedidos.setItems(todosPedidos);
        } else {
            Pedido.EstadoPedido estado = Pedido.EstadoPedido.valueOf(filtro);
            ObservableList<Pedido> filtrados = todosPedidos.stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tablaPedidos.setItems(filtrados);
        }
    }

    private void actualizarTabla() {
        tablaPedidos.refresh();
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        int total = todosPedidos.size();
        long pendientes = todosPedidos.stream()
            .filter(p -> p.getEstado() == Pedido.EstadoPedido.PENDIENTE)
            .count();
        long enProceso = todosPedidos.stream()
            .filter(p -> p.getEstado() == Pedido.EstadoPedido.EN_PROCESO)
            .count();
        long entregados = todosPedidos.stream()
            .filter(p -> p.getEstado() == Pedido.EstadoPedido.ENTREGADO)
            .count();

        lblTotalPedidos.setText(String.valueOf(total));
        lblPendientes.setText(String.valueOf(pendientes));
        lblEnProceso.setText(String.valueOf(enProceso));
        lblEntregados.setText(String.valueOf(entregados));
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
