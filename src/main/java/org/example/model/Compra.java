package org.example.model;

import java.util.Date;

public class Compra {
    private int id;
    private int usuarioId;
    private Date fecha;
    private double total;

    public Compra(int id, int usuarioId, Date fecha, double total) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.total = total;
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public Date getFecha() { return fecha; }
    public double getTotal() { return total; }

    public void setId(int id) { this.id = id; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setTotal(double total) { this.total = total; }
}
