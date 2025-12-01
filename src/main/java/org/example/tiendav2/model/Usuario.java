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
    private Rol rol;

    public Usuario(int id, String nombre, String email, String password, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
    
    // Constructor por defecto para facilitar la creación de usuarios con rol USUARIO
    public Usuario(int id, String nombre, String email, String password) {
        this(id, nombre, email, password, Rol.USUARIO);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Rol getRol() { return rol; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(Rol rol) { this.rol = rol; }
    
    public boolean esAdmin() {
        return this.rol == Rol.ADMINISTRADOR;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol +
                '}';
    }
}
