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

public final class PersistenciaUtil {
    private static final Logger logger = LoggerFactory.getLogger(PersistenciaUtil.class);
    private static final String DATA_DIR = System.getProperty("user.home") + "/tiendav2/data/";

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
    }

    @SuppressWarnings("unchecked")
    public static List<Producto> cargarProductos() {
        return (List<Producto>) cargarDatos("productos.dat", Producto.class);
    }

    @SuppressWarnings("unchecked")
    public static List<Usuario> cargarUsuarios() {
        return (List<Usuario>) cargarDatos("usuarios.dat", Usuario.class);
    }

    @SuppressWarnings("unchecked")
    public static List<Compra> cargarCompras() {
        return (List<Compra>) cargarDatos("compras.dat", Compra.class);
    }

    public static void guardarProductos(List<Producto> productos) {
        if (productos == null) {
            throw new IllegalArgumentException("La lista de productos no puede ser nula");
        }
        guardarDatos(new ArrayList<>(productos), "productos.dat");
    }

    public static void guardarUsuarios(List<Usuario> usuarios) {
        if (usuarios == null) {
            throw new IllegalArgumentException("La lista de usuarios no puede ser nula");
        }
        guardarDatos(new ArrayList<>(usuarios), "usuarios.dat");
    }

    public static void guardarCompras(List<Compra> compras) {
        if (compras == null) {
            throw new IllegalArgumentException("La lista de compras no puede ser nula");
        }
        guardarDatos(new ArrayList<>(compras), "compras.dat");
    }

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

    private static <T extends Serializable> void guardarDatos(List<T> datos, String nombreArchivo) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(datos);
        } catch (IOException e) {
            throw new IllegalArgumentException("Los datos no son serializables", e);
        }

        Path rutaArchivo = Paths.get(DATA_DIR, nombreArchivo);
        Path archivoTemporal = Paths.get(DATA_DIR, nombreArchivo + ".tmp");

        try {
            Files.createDirectories(rutaArchivo.getParent());

            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            Files.newOutputStream(archivoTemporal)))) {
                out.writeObject(datos);
                logger.debug("Datos guardados temporalmente en: {}", archivoTemporal);
            }

            Files.move(archivoTemporal, rutaArchivo,
                     StandardCopyOption.REPLACE_EXISTING,
                     StandardCopyOption.ATOMIC_MOVE);

            logger.debug("Datos guardados correctamente en: {}", rutaArchivo);

        } catch (IOException e) {
            try {
                Files.deleteIfExists(archivoTemporal);
            } catch (IOException ex) {
                logger.error("Error al limpiar archivo temporal", ex);
            }
            logger.error("Error al guardar datos en " + rutaArchivo, e);
            throw new RuntimeException("Error al guardar datos", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> cargarDatos(String nombreArchivo, Class<T> tipoClase) {
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