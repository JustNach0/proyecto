package org.example.tiendav2.model;

public class ColaPedidos {
    private NodoCola frente;
    private NodoCola fin;
    private int tamaño;

    public ColaPedidos() {
        this.frente = null;
        this.fin = null;
        this.tamaño = 0;
    }

    public void encolar(Pedido pedido) {
        NodoCola nuevoNodo = new NodoCola(pedido);
        
        if (estaVacia()) {
            frente = nuevoNodo;
        } else {
            fin.setSiguiente(nuevoNodo);
        }
        
        fin = nuevoNodo;
        tamaño++;
        
        System.out.println("Pedido encolado: " + pedido);
    }

    public Pedido desencolar() {
        if (estaVacia()) {
            System.out.println("La cola de pedidos está vacía");
            return null;
        }
        
        Pedido pedido = frente.getPedido();
        frente = frente.getSiguiente();

        if (frente == null) {
            fin = null;
        }
        
        tamaño--;
        System.out.println("Pedido desencolado: " + pedido);
        return pedido;
    }

    public Pedido verFrente() {
        if (estaVacia()) {
            return null;
        }
        return frente.getPedido();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamaño() {
        return tamaño;
    }

    public Pedido procesarSiguientePedido() {
        Pedido pedido = verFrente();
        if (pedido != null) {
            pedido.setEstado(Pedido.EstadoPedido.EN_PROCESO);
            System.out.println("Procesando pedido: " + pedido);
            return desencolar();
        }
        return null;
    }

    public void mostrarEstadoCola() {
        if (estaVacia()) {
            System.out.println("La cola de pedidos está vacía");
            return;
        }
        
        System.out.println("\n=== ESTADO DE LA COLA DE PEDIDOS ===");
        System.out.println("Pedidos en cola: " + tamaño());
        System.out.println("Próximo pedido a procesar: " + (frente != null ? frente.getPedido() : "Ninguno"));
        System.out.println("=================================\n");
    }
}
