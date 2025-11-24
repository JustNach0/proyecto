package org.example.tiendav2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidad para el registro de logs en la aplicación.
 * Proporciona métodos estáticos para registrar mensajes en diferentes niveles de severidad.
 */
public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
    
    private LogUtil() {
        // Constructor privado para evitar instanciación
    }
    
    /**
     * Registra un mensaje de depuración.
     * @param mensaje Mensaje a registrar
     * @param args Argumentos para formatear el mensaje
     */
    public static void debug(String mensaje, Object... args) {
        logger.debug(mensaje, args);
    }
    
    /**
     * Registra un mensaje informativo.
     * @param mensaje Mensaje a registrar
     * @param args Argumentos para formatear el mensaje
     */
    public static void info(String mensaje, Object... args) {
        logger.info(mensaje, args);
    }
    
    /**
     * Registra una advertencia.
     * @param mensaje Mensaje a registrar
     * @param args Argumentos para formatear el mensaje
     */
    public static void warn(String mensaje, Object... args) {
        logger.warn(mensaje, args);
    }
    
    /**
     * Registra un error.
     * @param mensaje Mensaje a registrar
     * @param args Argumentos para formatear el mensaje
     */
    public static void error(String mensaje, Object... args) {
        logger.error(mensaje, args);
    }
    
    /**
     * Registra un error con su excepción asociada.
     * @param mensaje Mensaje a registrar
     * @param ex Excepción a registrar
     */
    public static void error(String mensaje, Throwable ex) {
        logger.error(mensaje, ex);
    }
    
    /**
     * Registra el inicio de una operación importante.
     * @param operacion Nombre de la operación que inicia
     */
    public static void inicioOperacion(String operacion) {
        info("INICIO: {}", operacion);
    }
    
    /**
     * Registra el final de una operación importante.
     * @param operacion Nombre de la operación que finaliza
     */
    public static void finOperacion(String operacion) {
        info("FIN: {}", operacion);
    }
}
