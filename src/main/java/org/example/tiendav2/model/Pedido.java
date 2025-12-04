package org.example.tiendav2.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pedido {
    private final String id;
    private final Carrito carrito;
    private final LocalDateTime fechaCreacion;
    private EstadoPedido estado;
    private String direccionEnvio;
    private String metodoPago; // Almacena el método de pago como String

    public enum EstadoPedido {
        PENDIENTE,
        EN_PROCESO,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }

    public Pedido(Carrito carrito, String direccionEnvio, String metodoPago) {
        if (carrito == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo");
        }
        if (direccionEnvio == null || direccionEnvio.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección de envío es requerida");
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago es requerido");
        }
        
        this.id = UUID.randomUUID().toString();
        this.carrito = new Carrito(carrito.getUsuarioId());
        for (int i = 0; i < carrito.getCantidadProductos(); i++) {
            this.carrito.agregarProducto(carrito.obtenerProducto(i));
        }
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.direccionEnvio = direccionEnvio.trim();
        this.metodoPago = metodoPago.toString().trim(); // Aseguramos que sea un String
    }

    public String getId() {
        return id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getTotal() {
        return carrito.getTotal();
    }

    @Override
    public String toString() {
        return String.format("Pedido #%s - Estado: %s - Total: $%,.2f - Fecha: %s",
                id.substring(0, 8).toUpperCase(),
                estado,
                getTotal(),
                fechaCreacion);
    }
}
