package org.example.tiendav2.service;

import org.example.tiendav2.model.Carrito;
import org.example.tiendav2.model.Producto;

import java.util.HashMap;
import java.util.Map;

public class CarritoService {
    private final Map<Integer, Carrito> carritosPorUsuario;
    private final ProductoService productoService;

    public CarritoService(ProductoService productoService) {
        this.carritosPorUsuario = new HashMap<>();
        this.productoService = productoService;
    }

    public Carrito obtenerCarrito(int usuarioId) {
        return carritosPorUsuario.computeIfAbsent(usuarioId, Carrito::new);
    }

    public boolean agregarAlCarrito(int usuarioId, int productoId, int cantidad) {
        return productoService.obtenerProductoPorId(productoId)
                .map(producto -> {
                    Carrito carrito = obtenerCarrito(usuarioId);
                    for (int i = 0; i < cantidad; i++) {
                        carrito.agregarProducto(producto);
                    }
                    return true;
                })
                .orElse(false);
    }

    public boolean eliminarDelCarrito(int usuarioId, int productoId) {
        Carrito carrito = carritosPorUsuario.get(usuarioId);
        if (carrito == null) return false;

        for (int i = 0; i < carrito.getCantidadProductos(); i++) {
            if (carrito.obtenerProducto(i).getId() == productoId) {
                carrito.removerProducto(productoId);
                return true;
            }
        }
        return false;
    }

    public boolean actualizarCantidad(int usuarioId, int productoId, int nuevaCantidad) {
        if (nuevaCantidad < 0) return false;
        
        Carrito carrito = carritosPorUsuario.get(usuarioId);
        if (carrito == null) return false;

        while (true) {
            boolean encontrado = false;
            for (int i = 0; i < carrito.getCantidadProductos(); i++) {
                if (carrito.obtenerProducto(i).getId() == productoId) {
                    carrito.removerProducto(productoId);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) break;
        }

        return agregarAlCarrito(usuarioId, productoId, nuevaCantidad);
    }

    public void vaciarCarrito(int usuarioId) {
        Carrito carrito = carritosPorUsuario.get(usuarioId);
        if (carrito != null) {
            carrito.vaciarCarrito();
        }
    }

    public double obtenerTotalCarrito(int usuarioId) {
        Carrito carrito = carritosPorUsuario.get(usuarioId);
        return carrito != null ? carrito.getTotal() : 0.0;
    }
}
