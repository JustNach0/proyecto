package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.tiendav2.model.Carrito;

public class DialogoFinalizarCompraController {

    @FXML
    private Label lblCantidadProductos;
    @FXML
    private Label lblTotal;
    @FXML
    private TextField txtDireccion;
    @FXML
    private RadioButton rbTarjetaCredito;
    @FXML
    private RadioButton rbTarjetaDebito;
    @FXML
    private RadioButton rbPayPal;
    @FXML
    private RadioButton rbEfectivo;
    @FXML
    private ToggleGroup grupoMetodoPago;

    private boolean confirmado = false;
    private String direccion;
    private String metodoPago;

    @FXML
    public void initialize() {
        // Configurar valor por defecto de dirección
        txtDireccion.setText("Calle Falsa 123");
    }

    public void setDatosCarrito(Carrito carrito) {
        if (carrito != null) {
            lblCantidadProductos.setText(String.valueOf(carrito.getCantidadProductos()));
            lblTotal.setText(String.format("$%,.2f", carrito.getTotal()));
        }
    }

    @FXML
    private void confirmar() {
        // Validar dirección
        if (txtDireccion.getText() == null || txtDireccion.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Por favor ingresa una direccion de envio");
            return;
        }

        // Obtener método de pago seleccionado
        RadioButton seleccionado = (RadioButton) grupoMetodoPago.getSelectedToggle();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Por favor selecciona un metodo de pago");
            return;
        }

        // Guardar datos
        direccion = txtDireccion.getText().trim();
        metodoPago = seleccionado.getText();
        confirmado = true;

        // Cerrar ventana
        cerrarVentana();
    }

    @FXML
    private void cancelar() {
        confirmado = false;
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtDireccion.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }
}
