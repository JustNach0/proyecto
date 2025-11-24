package org.example.tiendav2.model;

import java.io.Serial;
import java.io.Serializable;

public class Producto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private double precio;
    private String descripcion;

    public Producto(int id, String nombre, double precio, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getDescripcion() { return descripcion; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
