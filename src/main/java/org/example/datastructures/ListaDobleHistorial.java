package org.example.datastructures;

import org.example.model.Compra;

public class ListaDobleHistorial {
    private Nodo cabeza;
    private Nodo cola;

    private static class Nodo {
        Compra compra;
        Nodo anterior;
        Nodo siguiente;
        Nodo(Compra compra) {
            this.compra = compra;
            this.anterior = null;
            this.siguiente = null;
        }
    }

    public ListaDobleHistorial() {
        cabeza = null;
        cola = null;
    }

    public void agregar(Compra compra) {
        Nodo nuevo = new Nodo(compra);
        if (cabeza == null) {
            cabeza = cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
    }

    public boolean eliminar(int idCompra) {
        Nodo actual = cabeza;
        while (actual != null && actual.compra.getId() != idCompra) {
            actual = actual.siguiente;
        }
        if (actual == null) return false;
        if (actual.anterior != null) actual.anterior.siguiente = actual.siguiente;
        else cabeza = actual.siguiente;
        if (actual.siguiente != null) actual.siguiente.anterior = actual.anterior;
        else cola = actual.anterior;
        return true;
    }

    public Compra buscar(int idCompra) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.compra.getId() == idCompra) return actual.compra;
            actual = actual.siguiente;
        }
        return null;
    }
}
