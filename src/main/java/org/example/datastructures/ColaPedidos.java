package org.example.datastructures;

import java.util.LinkedList;
import java.util.Queue;

public class ColaPedidos<T> {
    private Queue<T> cola;

    public ColaPedidos() {
        cola = new LinkedList<>();
    }

    public void encolar(T elemento) {
        cola.offer(elemento);
    }

    public T desencolar() {
        return cola.isEmpty() ? null : cola.poll();
    }

    public T frente() {
        return cola.isEmpty() ? null : cola.peek();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }
}
