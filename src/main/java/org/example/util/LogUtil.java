package org.example.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {
    private static final String LOG_FILE = "acciones_tienda.log";

    public static void registrarAccion(String mensaje) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write("[" + timestamp + "] " + mensaje + System.lineSeparator());
        } catch (IOException e) {
            // Manejo simple de error de escritura
            System.err.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }
}
