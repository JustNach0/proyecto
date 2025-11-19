package org.example.datastructures;

import org.example.model.Producto;

public class ListaCircularDoble {
    private Nodo cabeza;

    private static class Nodo {
        Producto producto;
        Nodo siguiente;
        Nodo anterior;
        Nodo(Producto producto) {
            this.producto = producto;
            this.siguiente = this.anterior = this;
        }
    }

    public ListaCircularDoble() {
        cabeza = null;
    }

    public void agregar(Producto producto) {
        Nodo nuevo = new Nodo(producto);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo ultimo = cabeza.anterior;
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
        }
    }

    public boolean eliminar(int idProducto) {
        if (cabeza == null) return false;
        Nodo actual = cabeza;
        do {
            if (actual.producto.getId() == idProducto) {
                if (actual.siguiente == actual) {
                    cabeza = null;
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                    if (actual == cabeza) cabeza = actual.siguiente;
                }
                return true;
            }
            actual = actual.siguiente;
        } while (actual != cabeza);
        return false;
    }

    public Producto buscar(int idProducto) {
        if (cabeza == null) return null;
        Nodo actual = cabeza;
        do {
            if (actual.producto.getId() == idProducto) return actual.producto;
            actual = actual.siguiente;
        } while (actual != cabeza);
        return null;
    }
}
