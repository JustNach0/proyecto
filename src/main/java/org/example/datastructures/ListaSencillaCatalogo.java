package org.example.datastructures;

import org.example.model.Producto;

public class ListaSencillaCatalogo {
    private Nodo cabeza;

    private static class Nodo {
        Producto producto;
        Nodo siguiente;
        Nodo(Producto producto) {
            this.producto = producto;
            this.siguiente = null;
        }
    }

    public ListaSencillaCatalogo() {
        cabeza = null;
    }

    public void agregar(Producto producto) {
        Nodo nuevo = new Nodo(producto);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }

    public boolean eliminar(int idProducto) {
        if (cabeza == null) return false;
        if (cabeza.producto.getId() == idProducto) {
            cabeza = cabeza.siguiente;
            return true;
        }
        Nodo actual = cabeza;
        while (actual.siguiente != null && actual.siguiente.producto.getId() != idProducto) {
            actual = actual.siguiente;
        }
        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
            return true;
        }
        return false;
    }

    public Producto buscar(int idProducto) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.producto.getId() == idProducto) {
                return actual.producto;
            }
            actual = actual.siguiente;
        }
        return null;
    }
}
