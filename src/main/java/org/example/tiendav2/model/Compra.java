package org.example.tiendav2.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.io.Serial;

public class Compra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private int usuarioId;
    private LocalDateTime fecha;
    private double total;
    private List<Producto> productos;

    public Compra(int id, int usuarioId, LocalDateTime fecha, double total, List<Producto> productos) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.total = total;
        this.productos = productos;
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public LocalDateTime getFecha() { return fecha; }
    public double getTotal() { return total; }
    public List<Producto> getProductos() { return productos; }

    public void setId(int id) { this.id = id; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setTotal(double total) { this.total = total; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}
