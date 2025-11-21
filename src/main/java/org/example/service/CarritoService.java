package org.example.service;

import org.example.model.Carrito;
import org.example.model.Producto;
import java.util.List;

public class CarritoService {
    public void agregarProducto(Carrito carrito, Producto producto) {
        carrito.agregarProducto(producto);
    }

    public void eliminarProducto(Carrito carrito, Producto producto) {
        carrito.removerProducto(producto);
    }

    public void limpiarCarrito(Carrito carrito) {
        carrito.vaciarCarrito();
    }

    public double calcularTotal(Carrito carrito) {
        double total = 0;
        List<Producto> productos = carrito.getProductos();
        for (Producto producto : productos) {
            total += producto.getPrecio();
        }
        return total;
    }

    public int cantidadProductos(Carrito carrito) {
        return carrito.getProductos().size();
    }
}
