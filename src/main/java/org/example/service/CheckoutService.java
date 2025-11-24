package org.example.service;

import org.example.model.Carrito;
import org.example.model.Compra;
import org.example.model.Producto;
import org.example.model.Inventario;
import java.util.Date;
import java.util.List;

public class CheckoutService {
    private InventarioService inventarioService;
    private List<Compra> historial;

    public CheckoutService(InventarioService inventarioService, List<Compra> historial) {
        this.inventarioService = inventarioService;
        this.historial = historial;
    }

    public boolean checkout(Carrito carrito) {
        double total = 0;
        for (Producto producto : carrito.getProductos()) {
            int cantidadDisponible = inventarioService.getCantidad(producto.getId());
            if (cantidadDisponible < 1) {
                return false; // No hay stock suficiente
            }
        }
        for (Producto producto : carrito.getProductos()) {
            inventarioService.actualizarCantidad(producto.getId(), inventarioService.getCantidad(producto.getId()) - 1);
            total += producto.getPrecio();
        }
        Compra compra = new Compra(historial.size() + 1, carrito.getUsuarioId(), new Date(), total);
        historial.add(compra);
        carrito.vaciarCarrito();
        return true;
    }
}
