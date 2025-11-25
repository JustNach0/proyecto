package org.example.tiendav2.model;

public class Carrito {
    private int usuarioId;
    private ListaEnlazadaSimple<Producto> productos;
    private double total;

    public Carrito(int usuarioId) {
        this.usuarioId = usuarioId;
        this.productos = new ListaEnlazadaSimple<>();
        this.total = 0.0;
    }

    public int getUsuarioId() { 
        return usuarioId; 
    }
    
    public void setUsuarioId(int usuarioId) { 
        this.usuarioId = usuarioId; 
    }

    public int getCantidadProductos() {
        return productos.tamaño();
    }
    

    public void agregarProducto(Producto producto) { 
        if (producto != null) {
            this.productos.agregar(producto);
            this.total += producto.getPrecio();
        }
    }

    public boolean removerProducto(Producto producto) { 
        if (producto != null && productos.eliminar(producto)) {
            this.total -= producto.getPrecio();
            return true;
        }
        return false;
    }

    public boolean removerProducto(int idProducto) {
        for (int i = 0; i < productos.tamaño(); i++) {
            Producto producto = productos.obtener(i);
            if (producto.getId() == idProducto) {
                this.total -= producto.getPrecio();
                return productos.eliminar(producto);
            }
        }
        return false;
    }

    public void vaciarCarrito() { 
        this.productos = new ListaEnlazadaSimple<>();
        this.total = 0.0;
    }

    public double getTotal() {
        return total;
    }

    public boolean estaVacio() {
        return productos.estaVacia();
    }

    public Producto obtenerProducto(int indice) {
        return productos.obtener(indice);
    }
}
