package org.example.tiendav2.model;

import java.io.Serial;
import java.io.Serializable;

public class Usuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String email;
    private String password;

    public Usuario(int id, String nombre, String email, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
