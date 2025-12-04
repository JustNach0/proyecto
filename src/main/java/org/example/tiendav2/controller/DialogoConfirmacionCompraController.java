package org.example.tiendav2.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.tiendav2.model.Pedido;

public class DialogoConfirmacionCompraController {

    @FXML
    private Label lblNumeroPedido;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblDireccion;
    @FXML
    private Label lblMetodoPago;

    public void setDatosPedido(Pedido pedido, String direccion, String metodoPago) {
        if (pedido != null) {
            lblNumeroPedido.setText("#" + pedido.getId().substring(0, 8).toUpperCase());
            lblTotal.setText(String.format("$%,.2f", pedido.getTotal()));
        }
        
        if (direccion != null) {
            lblDireccion.setText(direccion);
        }
        
        if (metodoPago != null) {
            lblMetodoPago.setText(metodoPago);
        }
    }

    @FXML
    private void cerrar() {
        Stage stage = (Stage) lblNumeroPedido.getScene().getWindow();
        stage.close();
    }
}
