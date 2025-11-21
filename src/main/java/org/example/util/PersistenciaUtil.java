package org.example.util;

import org.example.model.Producto;
import org.example.model.Usuario;
import org.example.model.Compra;
import java.io.*;
import java.util.*;

public class PersistenciaUtil {
    public static void guardarProductos(List<Producto> productos, String archivo) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(new ArrayList<>(productos));
        }
    }

    public static List<Producto> cargarProductos(String archivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Producto>) in.readObject();
        }
    }

    public static void guardarUsuarios(List<Usuario> usuarios, String archivo) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(new ArrayList<>(usuarios));
        }
    }

    public static List<Usuario> cargarUsuarios(String archivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Usuario>) in.readObject();
        }
    }

    public static void guardarCompras(List<Compra> compras, String archivo) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(new ArrayList<>(compras));
        }
    }

    public static List<Compra> cargarCompras(String archivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Compra>) in.readObject();
        }
    }
}
