package org.example.tiendav2.service;

import org.example.tiendav2.model.Rol;
import org.example.tiendav2.model.Usuario;

public class AuthService {
    private static Usuario usuarioActual = null;

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean estaAutenticado() {
        return usuarioActual != null;
    }

    public static boolean tieneRol(Rol rol) {
        return estaAutenticado() && usuarioActual.getRol() == rol;
    }

    public static boolean esAdmin() {
        return tieneRol(Rol.ADMINISTRADOR);
    }
}
