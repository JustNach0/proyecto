package org.example.tiendav2.service;

import org.example.tiendav2.model.Rol;
import org.example.tiendav2.model.Usuario;

import java.util.HashMap;
import java.util.Map;

public class LoginService {
    private final Map<String, Usuario> usuariosRegistrados;
    private Usuario usuarioActual;

    public LoginService() {
        this.usuariosRegistrados = new HashMap<>();
        // Usuario administrador por defecto
        boolean adminCreado = registrarUsuario("admin@example.com", "admin", "Administrador", Rol.ADMINISTRADOR);
        System.out.println("=== INICIALIZACIÓN DE USUARIOS ===");
        System.out.println("Admin creado: " + adminCreado);
        System.out.println("Email: admin@example.com");
        System.out.println("Contraseña: admin");
        System.out.println("Rol: " + Rol.ADMINISTRADOR);
        
        // Usuario normal por defecto
        boolean usuarioCreado = registrarUsuario("usuario@example.com", "usuario", "Usuario Normal");
        System.out.println("\nUsuario normal creado: " + usuarioCreado);
        System.out.println("Email: usuario@example.com");
        System.out.println("Contraseña: usuario");
        System.out.println("Rol: " + Rol.USUARIO);
        
        // Mostrar usuarios registrados
        System.out.println("\n=== USUARIOS REGISTRADOS ===");
        if (usuariosRegistrados.isEmpty()) {
            System.out.println("¡No hay usuarios registrados!");
        } else {
            usuariosRegistrados.forEach((email, usuario) -> {
                System.out.println("--------------------------------");
                System.out.println("Email: " + email);
                System.out.println("Nombre: " + usuario.getNombre());
                System.out.println("Rol: " + usuario.getRol());
                System.out.println("Contraseña almacenada: " + usuario.getPassword());
            });
        }
        System.out.println("==============================");
    }

    public boolean registrarUsuario(String email, String password, String nombre) {
        return registrarUsuario(email, password, nombre, Rol.USUARIO);
    }
    
    public boolean registrarUsuario(String email, String password, String nombre, Rol rol) {
        if (usuariosRegistrados.containsKey(email)) {
            return false;
        }

        int nuevoId = usuariosRegistrados.size() + 1;
        Usuario nuevoUsuario = new Usuario(nuevoId, nombre, email, password, rol);
        usuariosRegistrados.put(email, nuevoUsuario);
        return true;
    }

    public boolean autenticarUsuario(String email, String password) {
        System.out.println("\n=== INTENTO DE AUTENTICACIÓN ===");
        System.out.println("Email proporcionado: '" + email + "'");
        System.out.println("Contraseña proporcionada: '" + password + "'");
        
        System.out.println("\nUsuarios registrados: " + (usuariosRegistrados.isEmpty() ? "Ninguno" : ""));
        usuariosRegistrados.forEach((e, u) -> 
            System.out.println("- " + e + " (Nombre: " + u.getNombre() + ", Rol: " + u.getRol() + ")")
        );

        Usuario usuario = usuariosRegistrados.get(email);
        
        if (usuario == null) {
            System.out.println("\nError: No existe un usuario con el email '" + email + "'");
            System.out.println("Emails disponibles: " + usuariosRegistrados.keySet());
            return false;
        }
        
        System.out.println("\nUsuario encontrado:");
        System.out.println("- Nombre: " + usuario.getNombre());
        System.out.println("- Email: " + usuario.getEmail());
        System.out.println("- Rol: " + usuario.getRol());
        System.out.println("- Contraseña almacenada: '" + usuario.getPassword() + "'");
        
        boolean contrasenaCorrecta = usuario.getPassword().equals(password);
        System.out.println("Contraseña " + (contrasenaCorrecta ? "CORRECTA" : "INCORRECTA"));
        
        if (contrasenaCorrecta) {
            this.usuarioActual = usuario;
            AuthService.iniciarSesion(usuario);
            System.out.println("=== AUTENTICACIÓN EXITOSA ===\n");
            return true;
        }
        
        System.out.println("=== AUTENTICACIÓN FALLIDA ===\n");
        return false;
    }

    public void cerrarSesion() {
        AuthService.cerrarSesion();
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean estaAutenticado() {
        return usuarioActual != null;
    }
}
