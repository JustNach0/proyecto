package org.example.tiendav2.service;

import org.example.tiendav2.model.Carrito;
import org.example.tiendav2.model.ColaPedidos;
import org.example.tiendav2.model.Pedido;

public class PedidoService {
    private static PedidoService instancia;
    private final ColaPedidos colaPedidos;

    private PedidoService() {
        this.colaPedidos = new ColaPedidos();
    }

    public static synchronized PedidoService getInstancia() {
        if (instancia == null) {
            instancia = new PedidoService();
        }
        return instancia;
    }

    public Pedido crearPedido(Carrito carrito, String direccionEnvio, String metodoPago) {
        if (carrito == null || carrito.estaVacio()) {
            throw new IllegalArgumentException("El carrito no puede estar vacío");
        }
        
        if (direccionEnvio == null || direccionEnvio.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección de envío es requerida");
        }
        
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago es requerido");
        }
        
        Pedido pedido = new Pedido(carrito, direccionEnvio, metodoPago);
        colaPedidos.encolar(pedido);
        
        return pedido;
    }

    public Pedido procesarSiguientePedido() {
        Pedido pedido = colaPedidos.procesarSiguientePedido();
        if (pedido != null) {
            System.out.println("Pedido en proceso: " + pedido);
        }
        return pedido;
    }

    public ColaPedidos getColaPedidos() {
        return colaPedidos;
    }

    public int obtenerNumeroPedidosPendientes() {
        return colaPedidos.tamaño();
    }

    public void mostrarEstadoCola() {
        colaPedidos.mostrarEstadoCola();
    }
}
