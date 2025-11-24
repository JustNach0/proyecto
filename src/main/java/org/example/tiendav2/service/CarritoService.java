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

    /**
     * Obtiene el carrito de un usuario, creándolo si no existe
     */
    public Carrito obtenerCarrito(int usuarioId) {
        return carritosPorUsuario.computeIfAbsent(usuarioId, Carrito::new);
    }

    /**
     * Agrega un producto al carrito del usuario
     */
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

    /**
     * Elimina un producto del carrito del usuario
     */
    public boolean eliminarDelCarrito(int usuarioId, int productoId) {
        Carrito carrito = carritosPorUsuario.get(usuarioId);
        if (carrito == null) return false;
        
        return carrito.getProductos().removeIf(p -> p.getId() == productoId);
    }

    /**
     * Actualiza la cantidad de un producto en el carrito
     */
    public boolean actualizarCantidad(int usuarioId, int productoId, int nuevaCantidad) {
        if (nuevaCantidad < 0) return false;
        
        Carrito carrito = carritosPorUsuario.get(usuarioId);
        if (carrito == null) return false;
        
        // Primero eliminamos todas las ocurrencias del producto
        carrito.getProductos().removeIf(p -> p.getId() == productoId);
        
        // Luego agregamos la nueva cantidad
        return agregarAlCarrito(usuarioId, productoId, nuevaCantidad);
    }

    /**
     * Vacía el carrito de un usuario
     */
    public void vaciarCarrito(int usuarioId) {
        Carrito carrito = carritosPorUsuario.get(usuarioId);
        if (carrito != null) {
            carrito.vaciarCarrito();
        }
    }

    /**
     * Obtiene el total del carrito de un usuario
     */
    public double obtenerTotalCarrito(int usuarioId) {
        Carrito carrito = carritosPorUsuario.get(usuarioId);
        return carrito != null ? carrito.getTotal() : 0.0;
    }
}
