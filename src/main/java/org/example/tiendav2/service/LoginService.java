package org.example.tiendav2.service;

import org.example.tiendav2.model.Usuario;

import java.util.HashMap;
import java.util.Map;

public class LoginService {
    private final Map<String, Usuario> usuariosRegistrados;
    private Usuario usuarioActual;

    public LoginService() {
        this.usuariosRegistrados = new HashMap<>();
        // Usuario administrador por defecto
        registrarUsuario("admin@example.com", "admin", "Administrador");
    }

    public boolean registrarUsuario(String email, String password, String nombre) {
        if (usuariosRegistrados.containsKey(email)) {
            return false;
        }

        int nuevoId = usuariosRegistrados.size() + 1;
        Usuario nuevoUsuario = new Usuario(nuevoId, nombre, email, password);
        usuariosRegistrados.put(email, nuevoUsuario);
        return true;
    }

    public boolean autenticarUsuario(String email, String password) {
        System.out.println("DEBUG EMAIL: " + email);
        System.out.println("DEBUG PASS: " + password);
        System.out.println("USUARIOS REGISTRADOS: " + usuariosRegistrados.keySet());

        Usuario usuario = usuariosRegistrados.get(email);
        if (usuario != null && usuario.getPassword().equals(password)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean estaAutenticado() {
        return usuarioActual != null;
    }
}
