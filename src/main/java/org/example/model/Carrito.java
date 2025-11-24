package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private int usuarioId;
    private List<Producto> productos;

    public Carrito(int usuarioId) {
        this.usuarioId = usuarioId;
        this.productos = new ArrayList<>();
    }

    public int getUsuarioId() { return usuarioId; }
    public List<Producto> getProductos() { return productos; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
    public void agregarProducto(Producto producto) { this.productos.add(producto); }
    public void removerProducto(Producto producto) { this.productos.remove(producto); }
    public void vaciarCarrito() { this.productos.clear(); }
}
