package org.example.service;

import org.example.model.Usuario;
import java.util.HashMap;
import java.util.Map;

public class LoginService {
    private Map<String, String> usuarios; // email -> password
    private Map<String, String> admins;   // email -> password
    private Map<String, Usuario> datosUsuarios; // email -> Usuario

    public LoginService() {
        usuarios = new HashMap<>();
        admins = new HashMap<>();
        datosUsuarios = new HashMap<>();
        // Usuarios y admins de ejemplo
        usuarios.put("user@correo.com", "1234");
        datosUsuarios.put("user@correo.com", new Usuario(1, "Usuario", "user@correo.com"));
        admins.put("admin@correo.com", "admin");
        datosUsuarios.put("admin@correo.com", new Usuario(0, "Administrador", "admin@correo.com"));
    }

    public boolean loginUsuario(String email, String password) {
        return usuarios.containsKey(email) && usuarios.get(email).equals(password);
    }

    public boolean loginAdmin(String email, String password) {
        return admins.containsKey(email) && admins.get(email).equals(password);
    }

    public Usuario getUsuario(String email) {
        return datosUsuarios.get(email);
    }
}
