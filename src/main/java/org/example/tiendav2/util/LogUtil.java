package org.example.tiendav2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
    
    private LogUtil() {
    }

    public static void debug(String mensaje, Object... args) {
        logger.debug(mensaje, args);
    }

    public static void info(String mensaje, Object... args) {
        logger.info(mensaje, args);
    }

    public static void warn(String mensaje, Object... args) {
        logger.warn(mensaje, args);
    }

    public static void error(String mensaje, Object... args) {
        logger.error(mensaje, args);
    }

    public static void error(String mensaje, Throwable ex) {
        logger.error(mensaje, ex);
    }

    public static void inicioOperacion(String operacion) {
        info("INICIO: {}", operacion);
    }

    public static void finOperacion(String operacion) {
        info("FIN: {}", operacion);
    }
}
