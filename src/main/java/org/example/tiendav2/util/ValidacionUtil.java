package org.example.tiendav2.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Pattern;

public final class ValidacionUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    private static final Pattern SOLO_LETRAS = Pattern.compile("^[\\p{L} .'-]+$");
    private static final Pattern SOLO_NUMEROS = Pattern.compile("^\\d+$");
    private static final Pattern PRECIO_VALIDO = Pattern.compile("^\\d+([.,]\\d{1,2})?$");
    
    private ValidacionUtil() {
    }

    public static boolean esCorreoValido(String correo) {
        return StringUtils.isNotBlank(correo) && 
               EMAIL_PATTERN.matcher(correo).matches();
    }

    public static boolean esPrecioValido(double precio) {
        return !Double.isNaN(precio) && precio >= 0;
    }
    
    public static boolean esPrecioValido(String precio) {
        return StringUtils.isNotBlank(precio) && 
               PRECIO_VALIDO.matcher(precio).matches();
    }

    public static boolean esCantidadValida(int cantidad) {
        return cantidad >= 0;
    }
    
    public static boolean esCantidadValida(String cantidad) {
        return StringUtils.isNumeric(cantidad) && 
               Integer.parseInt(cantidad) >= 0;
    }

    public static boolean hayStockSuficiente(int stockDisponible, int cantidadSolicitada) {
        return stockDisponible >= cantidadSolicitada && cantidadSolicitada > 0;
    }

    public static boolean esTextoValido(String texto) {
        return StringUtils.isNotBlank(texto) && 
               SOLO_LETRAS.matcher(texto).matches();
    }
    
    public static boolean esNumeroValido(String numero) {
        return StringUtils.isNotBlank(numero) && 
               NumberUtils.isParsable(numero);
    }

    public static boolean esContrasenaValida(String contrasena) {
        return StringUtils.isNotBlank(contrasena) && 
               contrasena.length() >= 8 &&
               contrasena.matches(".*[A-Z].*") &&
               contrasena.matches(".*[a-z].*") &&
               contrasena.matches(".*\\d.*");
    }

    public static boolean esFechaValida(String fecha, String formato) {
        try {
            java.time.format.DateTimeFormatter formatter = 
                java.time.format.DateTimeFormatter.ofPattern(formato);
            formatter.parse(fecha);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
