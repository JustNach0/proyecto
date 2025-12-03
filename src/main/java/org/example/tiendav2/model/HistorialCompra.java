package org.example.tiendav2.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistorialCompra implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int usuarioId;
    private LocalDateTime fechaCompra;
    private double total;
    private List<Producto> productos;
    private String direccionEnvio;
    private String estado;

    public HistorialCompra(int id, int usuarioId, double total, String direccionEnvio) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        this.fechaCompra = LocalDateTime.now();
        this.productos = new ArrayList<>();
        this.estado = "En proceso";
    }

    // Getters y setters
    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public double getTotal() { return total; }
    public List<Producto> getProductos() { return productos; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public String getEstado() { return estado; }
    
    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
