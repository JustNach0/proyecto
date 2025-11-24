package org.example.tiendav2.util;

import org.example.tiendav2.model.Compra;
import org.example.tiendav2.model.Producto;
import org.example.tiendav2.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utilidad para la persistencia de datos en archivos.
 * Maneja la serialización y deserialización de objetos a archivos.
 */
public final class PersistenciaUtil {
    private static final Logger logger = LoggerFactory.getLogger(PersistenciaUtil.class);
    private static final String DATA_DIR = System.getProperty("user.home") + "/tiendav2/data/";

    // Bloque de inicialización estático
    static {
        try {
            Path path = Paths.get(DATA_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Directorio de datos creado en: {}", path.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Error al crear el directorio de datos", e);
            throw new RuntimeException("No se pudo inicializar el directorio de datos", e);
        }
    }

    private PersistenciaUtil() {
        // Constructor privado para evitar instanciación
    }

    // ===== MÉTODOS PÚBLICOS =====

    /**
     * Carga la lista de productos desde el archivo.
     * @return Lista de productos cargados o lista vacía si hay error
     */
    @SuppressWarnings("unchecked")
    public static List<Producto> cargarProductos() {
        return (List<Producto>) cargarDatos("productos.dat", Producto.class);
    }

    /**
     * Carga la lista de usuarios desde el archivo.
     * @return Lista de usuarios cargados o lista vacía si hay error
     */
    @SuppressWarnings("unchecked")
    public static List<Usuario> cargarUsuarios() {
        return (List<Usuario>) cargarDatos("usuarios.dat", Usuario.class);
    }

    /**
     * Carga la lista de compras desde el archivo.
     * @return Lista de compras cargadas o lista vacía si hay error
     */
    @SuppressWarnings("unchecked")
    public static List<Compra> cargarCompras() {
        return (List<Compra>) cargarDatos("compras.dat", Compra.class);
    }

    /**
     * Guarda la lista de productos en el archivo.
     * @param productos Lista de productos a guardar
     */
    public static void guardarProductos(List<Producto> productos) {
        if (productos == null) {
            throw new IllegalArgumentException("La lista de productos no puede ser nula");
        }
        guardarDatos(new ArrayList<>(productos), "productos.dat");
    }

    /**
     * Guarda la lista de usuarios en el archivo.
     * @param usuarios Lista de usuarios a guardar
     */
    public static void guardarUsuarios(List<Usuario> usuarios) {
        if (usuarios == null) {
            throw new IllegalArgumentException("La lista de usuarios no puede ser nula");
        }
        guardarDatos(new ArrayList<>(usuarios), "usuarios.dat");
    }

    /**
     * Guarda la lista de compras en el archivo.
     * @param compras Lista de compras a guardar
     */
    public static void guardarCompras(List<Compra> compras) {
        if (compras == null) {
            throw new IllegalArgumentException("La lista de compras no puede ser nula");
        }
        guardarDatos(new ArrayList<>(compras), "compras.dat");
    }

    /**
     * Realiza una copia de seguridad de todos los archivos de datos.
     * @throws RuntimeException si ocurre un error durante el proceso de copia de seguridad
     */
    public static void realizarBackup() {
        Path sourceDir = Paths.get(DATA_DIR);
        Path backupDir = Paths.get(DATA_DIR, "backup", String.valueOf(System.currentTimeMillis()));

        try {
            if (!Files.exists(sourceDir)) {
                logger.warn("No se puede hacer backup: el directorio fuente no existe: {}", sourceDir);
                return;
            }

            Files.createDirectories(backupDir);

            Files.walk(sourceDir)
                .filter(Files::isRegularFile)
                .filter(path -> !path.getFileName().toString().endsWith(".tmp"))
                .filter(path -> !path.startsWith(Paths.get(DATA_DIR, "backup")))
                .forEach(archivo -> {
                    try {
                        Path relPath = sourceDir.relativize(archivo);
                        Path dest = backupDir.resolve(relPath);
                        Files.createDirectories(dest.getParent());
                        Files.copy(archivo, dest, StandardCopyOption.REPLACE_EXISTING);
                        logger.debug("Backup realizado: {} -> {}", archivo, dest);
                    } catch (IOException e) {
                        logger.error("Error al hacer backup de " + archivo, e);
                    }
                });

            logger.info("Copia de seguridad completada en: {}", backupDir);

        } catch (IOException e) {
            logger.error("Error al realizar la copia de seguridad", e);
            throw new RuntimeException("Error al realizar backup", e);
        }
    }

    // ===== MÉTODOS PRIVADOS =====

    /**
     * Guarda los datos en un archivo de forma segura.
     * @param <T> Tipo de los datos a guardar (debe ser serializable)
     * @param datos Lista de datos a guardar
     * @param nombreArchivo Nombre del archivo donde se guardarán los datos
     */
    private static <T extends Serializable> void guardarDatos(List<T> datos, String nombreArchivo) {
        // Validación de parámetros
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }
        
        // Verificar que los datos sean serializables
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(datos);
        } catch (IOException e) {
            throw new IllegalArgumentException("Los datos no son serializables", e);
        }

        Path rutaArchivo = Paths.get(DATA_DIR, nombreArchivo);
        Path archivoTemporal = Paths.get(DATA_DIR, nombreArchivo + ".tmp");

        try {
            // Crear directorio si no existe
            Files.createDirectories(rutaArchivo.getParent());

            // Escribir en archivo temporal primero
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            Files.newOutputStream(archivoTemporal)))) {
                out.writeObject(datos);
                logger.debug("Datos guardados temporalmente en: {}", archivoTemporal);
            }

            // Mover archivo temporal a destino final (operación atómica)
            Files.move(archivoTemporal, rutaArchivo,
                     StandardCopyOption.REPLACE_EXISTING,
                     StandardCopyOption.ATOMIC_MOVE);

            logger.debug("Datos guardados correctamente en: {}", rutaArchivo);

        } catch (IOException e) {
            // Limpiar archivo temporal en caso de error
            try {
                Files.deleteIfExists(archivoTemporal);
            } catch (IOException ex) {
                logger.error("Error al limpiar archivo temporal", ex);
            }
            logger.error("Error al guardar datos en " + rutaArchivo, e);
            throw new RuntimeException("Error al guardar datos", e);
        }
    }

    /**
     * Carga los datos desde un archivo.
     * @param <T> Tipo de los datos a cargar
     * @param nombreArchivo Nombre del archivo desde donde cargar los datos
     * @param tipoClase Clase del tipo de datos a cargar (para validación)
     * @return Lista de datos cargados o lista vacía si hay error
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> cargarDatos(String nombreArchivo, Class<T> tipoClase) {
        // Validación de parámetros
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }
        if (tipoClase == null) {
            throw new IllegalArgumentException("El tipo de clase no puede ser nulo");
        }

        Path rutaArchivo = Paths.get(DATA_DIR, nombreArchivo);

        if (!Files.exists(rutaArchivo)) {
            logger.debug("El archivo {} no existe, se devolverá una lista vacía", rutaArchivo);
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(rutaArchivo)))) {

            Object obj = in.readObject();
            
            if (!(obj instanceof List)) {
                logger.warn("El archivo {} no contiene una lista válida", rutaArchivo);
                return new ArrayList<>();
            }
            
            List<?> lista = (List<?>) obj;
            
            // Verificar que todos los elementos sean del tipo esperado
            if (!lista.isEmpty() && !tipoClase.isInstance(lista.get(0))) {
                logger.error("Tipo de datos inesperado en el archivo {}. Esperado: {}, Encontrado: {}",
                        rutaArchivo, tipoClase.getName(), 
                        lista.get(0).getClass().getName());
                return new ArrayList<>();
            }
            
            logger.debug("Datos cargados correctamente desde: {}", rutaArchivo);
            return (List<T>) lista;

        } catch (ClassNotFoundException e) {
            logger.error("Clase no encontrada al cargar datos desde {}", rutaArchivo, e);
        } catch (IOException e) {
            logger.error("Error de E/S al cargar datos desde {}", rutaArchivo, e);
        } catch (ClassCastException e) {
            logger.error("Error de tipo al cargar datos desde {}", rutaArchivo, e);
        } catch (Exception e) {
            logger.error("Error inesperado al cargar datos desde {}", rutaArchivo, e);
        }
        
        return new ArrayList<>();
    }
}