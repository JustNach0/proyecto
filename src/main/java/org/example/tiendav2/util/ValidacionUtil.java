package org.example.tiendav2.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Pattern;

/**
 * Utilidad para validaciones comunes en la aplicación.
 * Proporciona métodos estáticos para validar diferentes tipos de datos.
 */
public final class ValidacionUtil {
    // Patrones de expresiones regulares
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    private static final Pattern SOLO_LETRAS = Pattern.compile("^[\\p{L} .'-]+$");
    private static final Pattern SOLO_NUMEROS = Pattern.compile("^\\d+$");
    private static final Pattern PRECIO_VALIDO = Pattern.compile("^\\d+([.,]\\d{1,2})?$");
    
    private ValidacionUtil() {
        // Constructor privado para evitar instanciación
    }
    
    // Validaciones de correo electrónico
    public static boolean esCorreoValido(String correo) {
        return StringUtils.isNotBlank(correo) && 
               EMAIL_PATTERN.matcher(correo).matches();
    }
    
    // Validaciones de precios
    public static boolean esPrecioValido(double precio) {
        return !Double.isNaN(precio) && precio >= 0;
    }
    
    public static boolean esPrecioValido(String precio) {
        return StringUtils.isNotBlank(precio) && 
               PRECIO_VALIDO.matcher(precio).matches();
    }
    
    // Validaciones de cantidades
    public static boolean esCantidadValida(int cantidad) {
        return cantidad >= 0;
    }
    
    public static boolean esCantidadValida(String cantidad) {
        return StringUtils.isNumeric(cantidad) && 
               Integer.parseInt(cantidad) >= 0;
    }
    
    // Validación de stock
    public static boolean hayStockSuficiente(int stockDisponible, int cantidadSolicitada) {
        return stockDisponible >= cantidadSolicitada && cantidadSolicitada > 0;
    }
    
    // Validaciones de texto
    public static boolean esTextoValido(String texto) {
        return StringUtils.isNotBlank(texto) && 
               SOLO_LETRAS.matcher(texto).matches();
    }
    
    public static boolean esNumeroValido(String numero) {
        return StringUtils.isNotBlank(numero) && 
               NumberUtils.isParsable(numero);
    }
    
    // Validación de contraseña
    public static boolean esContrasenaValida(String contrasena) {
        // Al menos 8 caracteres, una mayúscula, una minúscula y un número
        return StringUtils.isNotBlank(contrasena) && 
               contrasena.length() >= 8 &&
               contrasena.matches(".*[A-Z].*") &&
               contrasena.matches(".*[a-z].*") &&
               contrasena.matches(".*\\d.*");
    }
    
    // Validación de formato de fechas (puedes implementar según tus necesidades)
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
