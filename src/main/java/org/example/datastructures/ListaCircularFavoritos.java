package org.example.datastructures;

import org.example.model.Favorito;

public class ListaCircularFavoritos {
    private Nodo cabeza;

    private static class Nodo {
        Favorito favorito;
        Nodo siguiente;
        Nodo(Favorito favorito) {
            this.favorito = favorito;
            this.siguiente = null;
        }
    }

    public ListaCircularFavoritos() {
        cabeza = null;
    }

    public void agregar(Favorito favorito) {
        Nodo nuevo = new Nodo(favorito);
        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != cabeza) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
            nuevo.siguiente = cabeza;
        }
    }

    public boolean eliminar(int usuarioId, int productoId) {
        if (cabeza == null) return false;
        Nodo actual = cabeza;
        Nodo previo = null;
        do {
            if (actual.favorito.getUsuarioId() == usuarioId && actual.favorito.getProductoId() == productoId) {
                if (previo == null) {
                    // Eliminar cabeza
                    if (cabeza.siguiente == cabeza) {
                        cabeza = null;
                    } else {
                        Nodo ultimo = cabeza;
                        while (ultimo.siguiente != cabeza) {
                            ultimo = ultimo.siguiente;
                        }
                        cabeza = cabeza.siguiente;
                        ultimo.siguiente = cabeza;
                    }
                } else {
                    previo.siguiente = actual.siguiente;
                }
                return true;
            }
            previo = actual;
            actual = actual.siguiente;
        } while (actual != cabeza);
        return false;
    }

    public Favorito buscar(int usuarioId, int productoId) {
        if (cabeza == null) return null;
        Nodo actual = cabeza;
        do {
            if (actual.favorito.getUsuarioId() == usuarioId && actual.favorito.getProductoId() == productoId) {
                return actual.favorito;
            }
            actual = actual.siguiente;
        } while (actual != cabeza);
        return null;
    }
}
