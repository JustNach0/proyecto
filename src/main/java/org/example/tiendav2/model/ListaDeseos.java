package org.example.tiendav2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaDeseos implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int usuarioId;
    private List<Producto> productos;

    public ListaDeseos(int usuarioId) {
        this.usuarioId = usuarioId;
        this.productos = new ArrayList<>();
    }

    // Getters
    public int getUsuarioId() { return usuarioId; }
    public List<Producto> getProductos() { return new ArrayList<>(productos); }
    public boolean estaVacia() { return productos.isEmpty(); }
    public int getCantidadProductos() { return productos.size(); }

    // Métodos para gestionar la lista de deseos
    public void agregarProducto(Producto producto) {
        if (producto != null && !contieneProducto(producto.getId())) {
            productos.add(producto);
        }
    }

    public boolean eliminarProducto(int productoId) {
        return productos.removeIf(p -> p.getId() == productoId);
    }

    public boolean contieneProducto(int productoId) {
        return productos.stream().anyMatch(p -> p.getId() == productoId);
    }

    public void vaciar() {
        productos.clear();
    }

    public boolean moverACarrito(org.example.tiendav2.model.Carrito carrito, int productoId) {
        for (Producto producto : productos) {
            if (producto.getId() == productoId) {
                carrito.agregarProducto(producto);
                return eliminarProducto(productoId);
            }
        }
        return false;
    }
}
