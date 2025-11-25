package org.example.tiendav2.model;

public class ListaDoblementeEnlazada<T> {
    private NodoDoble<T> primero;
    private NodoDoble<T> ultimo;
    private NodoDoble<T> actual;
    private int tamaño;

    public ListaDoblementeEnlazada() {
        this.primero = null;
        this.ultimo = null;
        this.actual = null;
        this.tamaño = 0;
    }

    public void agregar(T dato) {
        NodoDoble<T> nuevoNodo = new NodoDoble<>(dato);
        
        if (estaVacia()) {
            primero = nuevoNodo;
            ultimo = nuevoNodo;
            actual = nuevoNodo;
        } else {
            ultimo.setSiguiente(nuevoNodo);
            nuevoNodo.setAnterior(ultimo);
            ultimo = nuevoNodo;
        }
        
        tamaño++;
    }

    public T obtenerActual() {
        return (actual != null) ? actual.getDato() : null;
    }

    public boolean siguiente() {
        if (actual != null && actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
            return true;
        }
        return false;
    }

    public boolean anterior() {
        if (actual != null && actual.getAnterior() != null) {
            actual = actual.getAnterior();
            return true;
        }
        return false;
    }

    public void irAlPrimero() {
        actual = primero;
    }

    public void irAlUltimo() {
        actual = ultimo;
    }

    public boolean estaVacia() {
        return primero == null;
    }

    public int tamaño() {
        return tamaño;
    }

    public boolean buscar(T dato) {
        NodoDoble<T> actualTemp = primero;
        while (actualTemp != null) {
            if (actualTemp.getDato().equals(dato)) {
                actual = actualTemp;
                return true;
            }
            actualTemp = actualTemp.getSiguiente();
        }
        return false;
    }

    public int obtenerIndiceActual() {
        if (actual == null) return -1;
        
        int indice = 0;
        NodoDoble<T> temp = primero;
        
        while (temp != null) {
            if (temp == actual) {
                return indice;
            }
            temp = temp.getSiguiente();
            indice++;
        }
        
        return -1;
    }

    public T obtenerPorIndice(int indice) {
        if (indice < 0 || indice >= tamaño) {
            return null;
        }
        
        NodoDoble<T> temp = primero;
        for (int i = 0; i < indice; i++) {
            temp = temp.getSiguiente();
        }
        
        return temp.getDato();
    }
}
