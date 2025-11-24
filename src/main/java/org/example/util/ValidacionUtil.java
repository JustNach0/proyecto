package org.example.util;

import java.util.regex.Pattern;

public class ValidacionUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean esCorreoValido(String correo) {
        return correo != null && EMAIL_PATTERN.matcher(correo).matches();
    }

    public static boolean esPrecioValido(double precio) {
        return precio >= 0;
    }

    public static boolean esCantidadValida(int cantidad) {
        return cantidad >= 0;
    }

    public static boolean hayStockSuficiente(int stock, int cantidadSolicitada) {
        return stock >= cantidadSolicitada;
    }
}
