package org.example.datastructures;

import java.util.Stack;

public class PilaNavegacion<T> {
    private Stack<T> pila;

    public PilaNavegacion() {
        pila = new Stack<>();
    }

    public void push(T elemento) {
        pila.push(elemento);
    }

    public T pop() {
        return pila.isEmpty() ? null : pila.pop();
    }

    public T peek() {
        return pila.isEmpty() ? null : pila.peek();
    }

    public boolean estaVacia() {
        return pila.isEmpty();
    }
}
