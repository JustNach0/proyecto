package org.example.tiendav2.model;

public class NodoCola {
    private Pedido pedido;
    private NodoCola siguiente;

    public NodoCola(Pedido pedido) {
        this.pedido = pedido;
        this.siguiente = null;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public NodoCola getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoCola siguiente) {
        this.siguiente = siguiente;
    }
}
