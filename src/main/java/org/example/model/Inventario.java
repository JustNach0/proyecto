package org.example.model;

public class Inventario {
    private int productoId;
    private int cantidad;

    public Inventario(int productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public int getProductoId() { return productoId; }
    public int getCantidad() { return cantidad; }
    public void setProductoId(int productoId) { this.productoId = productoId; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void disminuir(int cantidad) { this.cantidad -= cantidad; }
    public void aumentar(int cantidad) { this.cantidad += cantidad; }
}
